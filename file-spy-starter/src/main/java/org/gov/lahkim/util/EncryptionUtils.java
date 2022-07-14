package org.gov.lahkim.util;

import org.gov.lahkim.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @Author Ayoub LAHKIM
 */
public class EncryptionUtils {
    private static final Logger log = LoggerFactory.getLogger(EncryptionUtils.class);

    public EncryptionUtils() {
    }

    public static String generateRandomAesKey() throws ServiceException {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey aesKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(aesKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("AES.INVALID.ALGORITHME", "ENNCRYPTION ALGORITH INVALID");
        }

    }

    public static byte[] encode(byte[] input) {
        return Base64.getEncoder().encode(input);
    }

    public static RSAPrivateKey getPrivateKey(String keyPath) throws ServiceException {
        File initialFile = new File(keyPath);
        try (InputStream is = Files.newInputStream(initialFile.toPath())) {
            byte[] keyBytesArray = new byte[is.available()];
            is.read(keyBytesArray);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytesArray);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(spec);
        } catch (IOException e) {
            log.error("Error while processing private key", e);
            throw new ServiceException("ERROR.READ.PRIVATE.KEY", "Error while retrieving private key");
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid Encryption Algorithm", e);
            throw new ServiceException("PRIVATE.KEY.ALGORITHM.INVALID", "Invalid encryption algorithm for private key");
        } catch (InvalidKeySpecException e) {
            log.error("Invalid Key Spec for private key", e);
            throw new ServiceException("ERROR.INVALID.PRIVATE.KEY.SPEC", "Spec invalid for private key");
        }
    }

    public static RSAPublicKey getPublicKey(String path) throws ServiceException {
        File initialFile = new File(path);
        try (InputStream is = Files.newInputStream(initialFile.toPath())) {
            byte[] keyBytesArray = new byte[is.available()];
            is.read(keyBytesArray);
            String rsaPublicKey = new String(keyBytesArray, Charset.defaultCharset());
            rsaPublicKey = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "");
            rsaPublicKey = rsaPublicKey.replace("-----END PUBLIC KEY-----", "");
            rsaPublicKey = rsaPublicKey.replaceAll(System.lineSeparator(), "");
            rsaPublicKey = rsaPublicKey.replace("\n", "");

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(keySpec);
        } catch (IOException e) {
            log.error("Error while processing private key", e);
            throw new ServiceException("ERROR.READ.PUBLIC.KEY", "Error while retrieving public key");
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid Encryption Algorithm", e);
            throw new ServiceException("PUBLIC.KEY.ALGORITHM.INVALID", "Invalid encryption algorithm for public key");
        } catch (InvalidKeySpecException e) {
            log.error("Invalid Key Spec for public key", e);
            throw new ServiceException("ERROR.INVALID.PUBLIC.KEY.SPEC", "Spec invalid for public key");
        }
    }
}
