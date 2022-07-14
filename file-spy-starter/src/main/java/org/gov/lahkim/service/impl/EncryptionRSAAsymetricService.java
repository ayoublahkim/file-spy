package org.gov.lahkim.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gov.lahkim.configuration.properties.FolderSpyProperties;
import org.gov.lahkim.exception.ServiceException;
import org.gov.lahkim.service.IEncryptionAsymetricService;
import org.gov.lahkim.util.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.gov.lahkim.util.ErrorMessages.*;

/**
 * @Author Ayoub LAHKIM
 */
@Service
@Slf4j
public class EncryptionRSAAsymetricService implements IEncryptionAsymetricService {
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    @Autowired
    public FolderSpyProperties proxyProperties;

    @Override
    public String decrypt(byte[] encrypted) throws ServiceException {
        try {
            char[] decrypted;
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, EncryptionUtils.getPrivateKey(proxyProperties.getPrivateKey()));
            decrypted = new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8).toCharArray();
            return new String(decrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            log.error("Error while rsa decryption", e);
            throw new ServiceException(RSA_DECRYPTION_ERROR_CODE, RSA_DECRYPTION_ERROR_MESSAGE);
        }
    }

    @Override
    public String encrypt(byte[] clear) throws ServiceException {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, EncryptionUtils.getPublicKey(proxyProperties.getPublicKey()));
            byte[] data = cipher.doFinal(clear);
            return Base64.getEncoder().encodeToString(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            log.error("Error while rsa encryption", e);
            throw new ServiceException(RSA_ENCRYPTION_ERROR_CODE, RSA_ENCRYPTION_ERROR_MESSAGE);
        }
    }

}
