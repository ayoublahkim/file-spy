package org.gov.lahkim.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.gov.lahkim.configuration.validator.Conditional;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @Author Ayoub LAHKIM
 */
@Validated
@ConfigurationProperties(prefix = "tatouir.file-spy")
@Setter
@Getter
@Conditional
public class FolderSpyProperties {

    @NotEmpty
    private String privateKey;
    @NotEmpty
    private String publicKey;
    @Valid
    private Watcher watcher;
    @Valid
    private Cron cron;
    @NotEmpty
    private String directoryWritePath;
    private String archiveDirectoryPath;


}
