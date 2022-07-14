package org.gov.lahkim.util;

/**
 * @Author Ayoub LAHKIM
 */
public final class ErrorMessages {

    public static final String RSA_ENCRYPTION_ERROR_CODE = "AES.ENCRYPT.ERROR";
    public static final String RSA_ENCRYPTION_ERROR_MESSAGE = "Erreur lors du déchiffrement RSA";
    public static final String RSA_DECRYPTION_ERROR_CODE = "RSA.DECRYPT.ERROR";
    public static final String RSA_DECRYPTION_ERROR_MESSAGE = "Erreur lors du déchiffrement AES";
    public static final String AES_ENCRYPTION_ERROR_CODE = "AES.ENCRYPT.ERROR";
    public static final String AES_ENCRYPTION_ERROR_MESSAGE = "Erreur lors du déchiffrement AES";
    public static final String AES_DECRYPTION_ERROR_CODE = "AES.DECRYPT.ERROR";
    public static final String AES_DECRYPTION_ERROR_MESSAGE = "Erreur lors du déchiffrement AES";
    public static final String AES_INVALID_ALGORITHM_CODE = "RSA.DECRYPT.ERROR";
    public static final String AES_INVALID_ALGORITHM_MESSAGE = "Algorithme de chiffrement invalide";
    public static final String FILE_NOT_FOUND_CODE = "FILE.NOT.FOUND";
    public static final String FILE_NOT_FOUND_MESSAGE = "Fichier non trouvé";
    public static final String ERROR_WRITE_FILE_CODE = "ERROR.WRITE.FILE";
    public static final String ERROR_WRITE_FILE_MESSAGE = "Erreur lors de l'écriture du fichier";
    public static final String ERROR_READ_FILE_CODE = "ERROR.READ.FILE";
    public static final String ERROR_READ_FILE_MESSAGE = "Erreur lors de la lécture du fichier";

    public static final String ERROR_LOCK_FILE_CODE = "ERROR.LOCK.FILE";

    public static final String ERROR_LOCK_FILE_MESSAGE = "Erreur lors du verrou du fichier";

    private ErrorMessages() {
        // Not called
    }

}
