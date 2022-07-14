package org.gov.lahkim.service.impl.events;

import org.gov.lahkim.model.FileDepositEvent;
import org.gov.lahkim.model.FileMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author Ayoub LAHKIM
 */
@Component
public class FileDepositEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(final FileMessage message) {
        FileDepositEvent customSpringEvent = new FileDepositEvent(this, message);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }
}
