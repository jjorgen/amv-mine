package org.nsu.dcis.amv.core.util;

import org.apache.commons.lang.StringUtils;

/**
 * This bean holds i/o,error and status.
 *
 * Modification Log:
 *
 * Date        Modifier             Description
 * ----------  -------------------  --------------------------------------------
 * 5/25/2017  J. Jorgensen         Initial version.
 */
public class CommandResult {

    private String command;
    private String stdOut;
    private String stdError;
    private Integer exitStatus;
    private Exception cause;

    public CommandResult(String cmd, String consoleMessage, String consoleErrorMessage,
                         Integer exitStatus) {
        this.command = cmd;
        this.stdOut = consoleMessage;
        this.stdError = consoleErrorMessage;
        this.exitStatus = exitStatus;
    }

    public CommandResult(Exception cause) {
        this.cause = cause;
    }

    public String getStdOut() {
        return stdOut;
    }

    public String getStdError() {
        return stdError;
    }

    public boolean succeeded() {
        return !failed() && goodExitStatus();
    }

    public boolean failed() {
        return (cause != null) || badExitStatus();
    }

    private boolean badExitStatus() {
        return !goodExitStatus();
    }

    private boolean goodExitStatus() {
        return (exitStatus == null) || (exitStatus == 0);
    }

    public boolean stdOutContains(String s) {
        return stdOut != null && stdOut.contains(s);
    }

    public boolean stdErrorContains(String s) {
        return stdError != null && stdError.contains(s);
    }

    public String getCommand() {
        return this.command;
    }

    public String getStdOutToken(int i) {
        if (StringUtils.isBlank(stdOut)) {
            return null;
        }
        return stdOut.split(" ")[i];
    }

    public String getStdErrToken(int i) {
        if (StringUtils.isBlank(stdError)) {
            return null;
        }
        return stdError.split(" ")[i];
    }

    public Throwable getCause() {
        return this.cause;
    }

}
