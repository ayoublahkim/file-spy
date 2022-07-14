package org.gov.lahkim.service;

import org.gov.lahkim.exception.ServiceException;
import org.gov.lahkim.model.FileDto;

/**
 * @Author Ayoub LAHKIM
 */
public interface IFileReceiver {
    void receive(FileDto file) throws ServiceException;
}
