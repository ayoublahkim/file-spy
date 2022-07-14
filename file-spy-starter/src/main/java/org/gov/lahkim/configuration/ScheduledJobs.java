package org.gov.lahkim.configuration;

import org.gov.lahkim.configuration.properties.FolderSpyProperties;
import org.gov.lahkim.service.IFileGuard;
import org.gov.lahkim.service.IFileListener;
import org.gov.lahkim.util.ScheduledJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Ayoub LAHKIM
 */
@Configuration
@EnableScheduling
@ConditionalOnExpression(
        "!T(org.springframework.util.StringUtils).isEmpty('${tatouir.file-spy.cron.pattern:}') " +
                "and !T(org.springframework.util.StringUtils).isEmpty('${tatouir.file-spy.cron.directory-read-path:}')"
)
public class ScheduledJobs {
    @Autowired
    private FolderSpyProperties configuration;
    @Autowired
    private IFileListener listener;
    @Autowired
    private IFileGuard guard;

    @Bean
    public ScheduledJob scheduledJob() {
        return new ScheduledJob(configuration, listener, guard);
    }
}
