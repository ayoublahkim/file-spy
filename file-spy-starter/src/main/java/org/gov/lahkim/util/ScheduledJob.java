package org.gov.lahkim.util;

import org.gov.lahkim.configuration.properties.FolderSpyProperties;
import org.gov.lahkim.exception.ServiceException;
import org.gov.lahkim.model.CronFile;
import org.gov.lahkim.model.FileDto;
import org.gov.lahkim.service.IFileGuard;
import org.gov.lahkim.service.IFileListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Ayoub LAHKIM
 */
public class ScheduledJob {

    private FolderSpyProperties configuration;
    private IFileListener listener;
    private IFileGuard guard;

    public ScheduledJob(FolderSpyProperties configuration, IFileListener listener, IFileGuard guard) {
        this.configuration = configuration;
        this.listener = listener;
        this.guard = guard;
    }

    @Scheduled(cron = "${tatouir.file-spy.cron.pattern}")
    public void readDirectory() throws IOException, ServiceException {
        Map<String, File> files = FileFinder.getAllFiles(configuration.getCron().getDirectoryReadPath(),
                configuration.getCron().getFileNameRegexPattern());
        files.forEach((key, val) -> {
            try {
                FileDto encrypted = guard.guard(val);
                listener.onTriggerCron(new CronFile(key, encrypted));
            } catch (ServiceException | IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

}
