package org.nsu.dcis.amv.core.util;

/**
 * Created by jorgej2 on 12/9/2017.
 */
public class TraceMethod {
    private int level;
    private String lineReadFromFile;

    public TraceMethod(int level, String lineReadFromFile) {
        this.level = level;
        this.lineReadFromFile = lineReadFromFile;
    }

    public TraceMethod() {
        this.level = -1;
        this.lineReadFromFile = "";
    }

    public int getLevel() {
        return level;
    }

    public String getLineReadFromFile() {
        return lineReadFromFile;
    }

    @Override
    public String toString() {
        return "TraceMethod{" +
                "level=" + level +
                ", lineReadFromFile='" + lineReadFromFile + '\'' +
                '}';
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
}
