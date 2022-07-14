package org.gov.lahkim.service;

import org.gov.lahkim.exception.ServiceException;

/**
 * @Author Ayoub LAHKIM
 */
public interface IEncryptionAsymetricService {
    String decrypt(byte[] encrypted) throws ServiceException;

    String encrypt(byte[] clear) throws ServiceException;
}
