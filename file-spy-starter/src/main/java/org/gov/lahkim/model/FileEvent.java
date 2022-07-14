package org.gov.lahkim.model;

import java.io.File;
import java.util.EventObject;

/**
 * @author Ayoub LAHKIM
 */
public class FileEvent extends EventObject {
    public FileEvent(File file) {
        super(file);
    }

    public File getFile() {
        return (File) getSource();
    }
}
