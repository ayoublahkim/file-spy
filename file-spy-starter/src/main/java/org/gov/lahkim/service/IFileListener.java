package org.gov.lahkim.service;

import org.gov.lahkim.exception.ServiceException;
import org.gov.lahkim.model.CronFile;
import org.gov.lahkim.model.FileDto;
import org.gov.lahkim.model.FileEvent;

import java.io.IOException;
import java.util.EventListener;

/**
 * @author Ayoub LAHKIM
 */
public interface IFileListener extends EventListener {

    void onCreated(FileEvent event, FileDto fileDto) throws ServiceException, IOException;

    // to rush later
    void onModified(FileEvent event, FileDto fileDto) throws ServiceException;

    void onDeleted(FileEvent event);

    void onOverFlow();

    default void onTriggerCron(CronFile file) throws ServiceException, IOException {

    }
}
