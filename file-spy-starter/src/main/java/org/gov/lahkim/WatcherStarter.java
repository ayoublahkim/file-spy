package org.gov.lahkim;

import org.gov.lahkim.configuration.properties.FolderSpyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Ayoub LAHKIM
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties({FolderSpyProperties.class})
public class WatcherStarter {
}
