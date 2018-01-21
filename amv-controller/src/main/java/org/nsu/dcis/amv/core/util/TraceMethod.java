package org.nsu.dcis.amv.core.util;

/**
 * This class holds one method and the level at which this was
 * read from the file.
 */
public class TraceMethod {
    private int traceFileLineNumber;
    private int level;
    private String lineReadFromFile;

    public TraceMethod(int lineNumber, int level, String lineReadFromFile) {
        this.traceFileLineNumber = lineNumber;
        this.level = level;
        this.lineReadFromFile = lineReadFromFile;
    }

    public TraceMethod() {
        this.level = -1;
        this.lineReadFromFile = "";
    }

    public int getTraceFileLineNumber() {
        return traceFileLineNumber;
    }

    public int getLevel() {
        return level;
    }

    public String getLineReadFromFile() {
        return lineReadFromFile;
    }

    public boolean isTestMethod() {
        if (lineReadFromFile.contains(".test.")) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return level == -1;
    }

    public String getTraceMethod() {
        String contextLine = getLineReadFromFile();
        int startPos = contextLine.indexOf("Entering[");
        int endPos = contextLine.indexOf(")]");
        return contextLine.substring(startPos + 9, endPos + 1);
    }


    public boolean equals(TraceMethod traceMethod) {
        if (traceMethod != null) {
            String thisTraceMethod = this.getTraceMethod();
            String compareTraceMethod = traceMethod.getTraceMethod();
            if (thisTraceMethod.equals(compareTraceMethod)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "TraceMethod{" +
                "traceFileLineNumber=" + traceFileLineNumber +
                ", level=" + level +
                ", lineReadFromFile='" + lineReadFromFile + '\'' +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "TraceMethod{" +
//                "level=" + level +
//                ", lineReadFromFile='" + lineReadFromFile + '\'' +
//                '}';
//    }
}
