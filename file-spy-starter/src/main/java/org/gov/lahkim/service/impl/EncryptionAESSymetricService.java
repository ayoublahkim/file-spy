package org.gov.lahkim.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.gov.lahkim.exception.ServiceException;
import org.gov.lahkim.service.IEncryptionSymetricService;
import org.gov.lahkim.util.ErrorMessages;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * @Author Ayoub LAHKIM
 */
@Service
@Slf4j
public class EncryptionAESSymetricService implements IEncryptionSymetricService {


    public SecretKeySpec setKey(String myKey) throws ServiceException {
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid algorithm", e);
            throw new ServiceException(ErrorMessages.AES_INVALID_ALGORITHM_CODE, ErrorMessages.AES_INVALID_ALGORITHM_MESSAGE);
        }
    }

    @Override
    public byte[] encrypt(byte[] toEncrypt, String secret) throws ServiceException {
        try {
            final SecretKeySpec secretKey = setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encode(cipher.doFinal(toEncrypt));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            log.error("Error while aes encryption", e);
            throw new ServiceException(ErrorMessages.AES_ENCRYPTION_ERROR_CODE, ErrorMessages.AES_ENCRYPTION_ERROR_MESSAGE);
        }
    }

    @Override
    public byte[] decrypt(byte[] data, String secret) throws ServiceException {
        try {
            final SecretKeySpec secretKey = setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(Base64.getDecoder().decode(data));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            log.error("Error while aes deccryption", e);
            throw new ServiceException(ErrorMessages.AES_DECRYPTION_ERROR_CODE, ErrorMessages.AES_DECRYPTION_ERROR_MESSAGE);
        }
    }
}
