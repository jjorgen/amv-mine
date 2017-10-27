package org.nsu.dcis.amv.core.exception;

public class AmvException extends RuntimeException {

    public AmvException() {
        super();
    }

    public AmvException(String message) {
        super(message);
    }

    public AmvException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmvException(Throwable cause) {
        super(cause);
    }
}
