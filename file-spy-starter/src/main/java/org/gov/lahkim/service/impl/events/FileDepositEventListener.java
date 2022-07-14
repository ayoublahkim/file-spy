package org.gov.lahkim.service.impl.events;

import lombok.SneakyThrows;
import org.gov.lahkim.model.FileDepositEvent;
import org.gov.lahkim.model.FileDto;
import org.gov.lahkim.service.impl.FileGuard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * @author Ayoub LAHKIM
 */

@Component
public class FileDepositEventListener implements ApplicationListener<FileDepositEvent> {
    @Autowired
    FileGuard guard;

    @SneakyThrows
    @Override
    public void onApplicationEvent(FileDepositEvent event) {
        if (event.getMessage().getKind().equals(ENTRY_CREATE)) {
            FileDto securedFile = guard.guard(event.getMessage().getFile());
            event.getMessage().getListener().onCreated(event.getMessage().getFileEvent(), securedFile);
        }
    }
}
