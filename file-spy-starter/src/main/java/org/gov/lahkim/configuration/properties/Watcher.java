package org.gov.lahkim.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ayoub LAHKIM
 */

@Getter
@Setter
@Configuration
public class Watcher {
    @NotEmpty
    private String directoryReadPath;

    //change it in your project configuration if you want a regex for file retrieval (example: *.xml)
    private String fileNameRegexPattern = "*";
}
