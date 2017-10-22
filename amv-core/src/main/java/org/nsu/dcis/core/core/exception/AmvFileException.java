package org.nsu.dcis.core.core.exception;

/**
 * Created by John Jorgensen on 3/12/2017.
 */
public class AmvFileException extends AmvException {
    public AmvFileException() {
        super();
    }

    public AmvFileException(String message) {
        super(message);
    }

    public AmvFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmvFileException(Throwable cause) {
        super(cause);
    }
}
