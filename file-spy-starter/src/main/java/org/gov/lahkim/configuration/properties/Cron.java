package org.gov.lahkim.configuration.properties;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ayoub LAHKIM
 */

@Data
public class Cron {
    // These two attributes must be filled to activate cron job
    @NotEmpty
    private String directoryReadPath;
    @NotEmpty
    private String pattern;

    //change it in your project configuration if you want a regex for file retrieval (example: *.xml)
    private String fileNameRegexPattern = "*";
}
