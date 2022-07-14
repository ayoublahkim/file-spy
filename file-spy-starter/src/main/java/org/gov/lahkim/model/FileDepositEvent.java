package org.gov.lahkim.model;

import org.springframework.context.ApplicationEvent;
/**
 * @author Ayoub LAHKIM
 */

public class FileDepositEvent extends ApplicationEvent {
    private FileMessage message;

    public FileDepositEvent(Object source, FileMessage message) {
        super(source);
        this.message = message;
    }

    public FileMessage getMessage() {
        return message;
    }


}
