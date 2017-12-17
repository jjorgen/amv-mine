package org.nsu.dcis.amv.core.exception;

/**
 * Created by jorgej2 on 10/24/2017.
 */
public class FileException extends AmvException {

    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }
}
