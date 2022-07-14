package org.gov.lahkim.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gov.lahkim.configuration.properties.FolderSpyProperties;
import org.gov.lahkim.exception.ServiceException;
import org.gov.lahkim.model.FileDto;
import org.gov.lahkim.service.IFileReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;

import static org.gov.lahkim.util.ErrorMessages.*;

/**
 * @Author Ayoub LAHKIM
 */
@Service
@Slf4j
public class FileReceiver implements IFileReceiver {
    @Autowired
    private EncryptionRSAAsymetricService encryptionRSAService;
    @Autowired
    private EncryptionAESSymetricService encryptionAESService;
    @Autowired
    private FolderSpyProperties configuration;

    @Override
    public void receive(FileDto file) throws ServiceException {
        if (null == file || null == file.getSecret() || null == file.getFileName() || null == file.getFileContent()) {
            throw new ServiceException(FILE_NOT_FOUND_CODE, FILE_NOT_FOUND_MESSAGE);
        }
        String decryptedAes = encryptionRSAService.decrypt(Base64.getDecoder().decode(file.getSecret()));
        log.info("Start decyrpting data with this key [{}]", decryptedAes);
        byte[] decryptedData = encryptionAESService.decrypt(file.getFileContent(), decryptedAes);
        byte[] decryptedFileName = encryptionAESService.decrypt(file.getFileName(), decryptedAes);
        String fileName = new String(Base64.getDecoder().decode(decryptedFileName), StandardCharsets.UTF_8);
        String aim = Paths.get(configuration.getDirectoryWritePath(), fileName).toString();
        try (FileOutputStream fos = new FileOutputStream(aim)) {
            fos.write(Base64.getDecoder().decode(decryptedData));
            log.info("File written to [{}]", aim);
        } catch (FileNotFoundException e) {
            log.error("File not found", e);
            throw new ServiceException(FILE_NOT_FOUND_CODE, FILE_NOT_FOUND_MESSAGE);
        } catch (IOException e) {
            log.error("Error while writing file", e);
            throw new ServiceException(ERROR_WRITE_FILE_CODE, ERROR_WRITE_FILE_MESSAGE);
        }
    }
}

