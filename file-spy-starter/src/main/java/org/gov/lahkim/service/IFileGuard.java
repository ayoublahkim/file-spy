package org.gov.lahkim.service;

import org.gov.lahkim.exception.ServiceException;
import org.gov.lahkim.model.FileDto;

import java.io.File;

public interface IFileGuard {
    FileDto guard(File file) throws ServiceException;
}
