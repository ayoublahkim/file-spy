package org.gov.lahkim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @author Ayoub LAHKIM
 */

@Data
@AllArgsConstructor
public class CronFile {

    private String name;
    private FileDto fileDto;
}
