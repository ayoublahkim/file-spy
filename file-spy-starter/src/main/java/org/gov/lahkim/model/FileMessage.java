package org.gov.lahkim.model;

import lombok.Data;
import org.gov.lahkim.service.IFileListener;

import java.io.File;
import java.nio.file.WatchEvent;

/**
 * @author Ayoub LAHKIM
 */

@Data
public class FileMessage {
    private File file;
    private FileEvent fileEvent;
    private WatchEvent.Kind<?> kind;
    private IFileListener listener;

}
