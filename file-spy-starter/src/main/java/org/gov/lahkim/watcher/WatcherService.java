package org.gov.lahkim.watcher;

import com.sun.nio.file.SensitivityWatchEventModifier;
import lombok.extern.slf4j.Slf4j;
import org.gov.lahkim.configuration.properties.FolderSpyProperties;
import org.gov.lahkim.model.FileEvent;
import org.gov.lahkim.model.FileMessage;
import org.gov.lahkim.service.IFileListener;
import org.gov.lahkim.service.impl.events.FileDepositEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author Ayoub LAHKIM
 */

@Service
@Slf4j
@ConditionalOnExpression(
        "!T(org.springframework.util.StringUtils).isEmpty('${file-starter.file-spy.watcher.directory-read-path:}')"
)
public class WatcherService {

    @Autowired
    private FolderSpyProperties properties;
    @Autowired
    private IFileListener listener;
    @Autowired
    private FileDepositEventPublisher publisher;
    private WatchService watcher;
    private ExecutorService executor;
    private ConcurrentHashMap<String, String> directoryMap;
    private PathMatcher pathMatcher;

    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    @PostConstruct
    public void init() throws IOException {
        directoryMap = new ConcurrentHashMap<>();
        watcher = FileSystems.getDefault().newWatchService();
        executor = Executors.newSingleThreadExecutor();
        pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + properties.getWatcher().getFileNameRegexPattern());
        startRecursiveWatcher();
    }

    @PreDestroy
    public void cleanup() {
        try {
            watcher.close();
            log.debug("closing watcher service");
        } catch (IOException e) {
            log.error("Error closing watcher service", e);
        }
        executor.shutdown();
    }

    private void startRecursiveWatcher() throws IOException {
        log.info("Starting Recursive Watcher");
        final Map<WatchKey, Path> keys = new HashMap<>();

        Consumer<Path> register = p -> {
            if (!p.toFile().exists() || !p.toFile().isDirectory()) {
                log.error("folder " + p + " does not exist or is not a directory");
                throw new RuntimeException("folder " + p + " does not exist or is not a directory");
            }
            try {
                Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        log.info("registering " + dir + " in watcher service");
                        WatchKey watchKey = dir.register(watcher, new WatchEvent.Kind[]{ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY}, SensitivityWatchEventModifier.HIGH);
                        keys.put(watchKey, dir);
                        log.info("File Walk Tree : {}", dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Error registering path " + p);
            }
        };

        register.accept(Paths.get(properties.getWatcher().getDirectoryReadPath()));

        executor.submit(() -> {
            while (true) {
                final WatchKey key;
                try {
                    key = watcher.take(); // wait for a key to be available
                } catch (InterruptedException ex) {
                    return;
                }

                final Path dir = keys.get(key);
                log.debug("Directory " + dir);
                if (dir == null) {
                    System.err.println("WatchKey " + key + " not recognized!");
                    continue;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> eventKind = event.kind();

                    // Overflow occurs when the watch event queue is overflown with events.
                    if (eventKind.equals(OVERFLOW)) {
                        FileMessage message = new FileMessage();
                        message.setListener(listener);
                        message.setKind(OVERFLOW);
                        publisher.publish(message);
                        return;
                    }
                    WatchEvent<Path> pathEvent = cast(event);
                    Path path = pathEvent.context();
                    final Path absPath = dir.resolve(path);
                    File file = absPath.toFile();
                    if (file.isDirectory()) {
                        register.accept(absPath);
                    } else if (eventKind.equals(ENTRY_CREATE) && pathMatcher.matches(absPath.getFileName())) {
                        log.info("Detected path ENTRY_CREATE event at: {}", absPath);
                        FileMessage message = new FileMessage();
                        message.setListener(listener);
                        message.setFileEvent(new FileEvent(file));
                        message.setFile(file);
                        message.setKind(ENTRY_CREATE);
                        publisher.publish(message);

                    } else if (eventKind.equals(ENTRY_MODIFY) && pathMatcher.matches(absPath.getFileName())) {
                        //TODO
                    } else if (eventKind.equals(ENTRY_DELETE) && pathMatcher.matches(absPath.getFileName())) {
                        //TODO
                    }

                }
                // IMPORTANT: The key must be reset after processed
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        });
    }
}
