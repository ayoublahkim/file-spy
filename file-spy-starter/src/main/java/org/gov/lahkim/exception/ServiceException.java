package org.gov.lahkim.exception;

/**
 * @Author Ayoub LAHKIM
 */
public class ServiceException extends Exception {

    private final String code;

    public ServiceException(String code, String errorMessage) {
        super(errorMessage);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
