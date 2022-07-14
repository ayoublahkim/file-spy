package org.gov.lahkim.service;

import org.gov.lahkim.exception.ServiceException;

/**
 * @Author Ayoub LAHKIM
 */
public interface IEncryptionSymetricService {
    byte[] encrypt(byte[] toEncrypt, String secret) throws ServiceException;

    byte[] decrypt(byte[] data, String secret) throws ServiceException;
}
