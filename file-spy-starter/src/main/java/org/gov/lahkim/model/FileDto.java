package org.gov.lahkim.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
/**
 * @author Ayoub LAHKIM
 */
@Getter
@Setter
@Builder
public class FileDto {
    byte[] fileContent;
    byte[] fileName;
    String secret;
}
