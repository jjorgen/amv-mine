package org.nsu.dcis.amv.core.exception;

/**
 * Created by jorgej2 on 12/4/2017.
 */
public class ExecutionTraceException extends AmvException {

    public ExecutionTraceException() {
        super();
    }

    public ExecutionTraceException(String message) {
        super(message);
    }

    public ExecutionTraceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionTraceException(Throwable cause) {
        super(cause);
    }
}
