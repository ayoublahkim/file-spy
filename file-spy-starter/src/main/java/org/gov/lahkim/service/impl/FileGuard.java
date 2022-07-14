package org.gov.lahkim.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gov.lahkim.exception.ServiceException;
import org.gov.lahkim.model.FileDto;
import org.gov.lahkim.service.IEncryptionAsymetricService;
import org.gov.lahkim.service.IEncryptionSymetricService;
import org.gov.lahkim.service.IFileGuard;
import org.gov.lahkim.util.EncryptionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.gov.lahkim.util.ErrorMessages.ERROR_READ_FILE_CODE;
import static org.gov.lahkim.util.ErrorMessages.ERROR_READ_FILE_MESSAGE;

/**
 * @author Ayoub LAHKIM on 10/07/2022
 */
@RequiredArgsConstructor
@Slf4j
@Service
@Scope("prototype")
public class FileGuard implements IFileGuard {

    private final IEncryptionAsymetricService encryptionRSAService;
    private final IEncryptionSymetricService encryptionAESService;

    public synchronized FileDto guard(File file) throws ServiceException {
        byte[] encoded;
        try {
            byte[] bytes = toByteArray(file);
            encoded = Base64.getEncoder().encode(bytes);
        } catch (Exception e) {
            log.error(ERROR_READ_FILE_MESSAGE, e);
            throw new ServiceException(ERROR_READ_FILE_CODE, ERROR_READ_FILE_MESSAGE);
        }
        //generate a random aes key
        String aesKey = EncryptionUtils.generateRandomAesKey();

        // encrypt data with the random generated key and provide it to the receiver
        log.info("data will be encrypted with this key [{}] ", aesKey);
        byte[] encryptedData = encryptionAESService.encrypt(encoded, aesKey);

        // encrypt the file name with the same generated aes key
        byte[] fileNameBytes = file.getName().getBytes(StandardCharsets.UTF_8);
        byte[] encryptedFileName = encryptionAESService.encrypt(EncryptionUtils.encode(fileNameBytes), aesKey);

        // encrypt the aes key to provide it to the receiver
        String encryptedAes = encryptionRSAService.encrypt(aesKey.getBytes());
        return FileDto.builder()
                .fileContent(encryptedData)
                .fileName(encryptedFileName)
                .secret(encryptedAes)
                .build();
    }

    private byte[] toByteArray(File file)
            throws IOException {

        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(file);

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int) file.length()];

        // Reading file content to byte array
        // using standard read() method
        fl.read(arr);

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        fl.close();

        // Returning above byte array
        return arr;
    }
}
