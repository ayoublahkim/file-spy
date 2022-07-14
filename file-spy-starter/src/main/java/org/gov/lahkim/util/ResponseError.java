package org.gov.lahkim.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author Ayoub LAHKIM
 */
@Data
@AllArgsConstructor
public class ResponseError {
    private final String code;
    private final String message;
}
