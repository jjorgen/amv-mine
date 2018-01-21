package org.nsu.dcis.amv.core.util;

public class FileLine {
    private Integer lineNumber;
    private String line;
    private Integer endCharacterPosition;

    public FileLine(String line) {
        setEndCharacterPosition(line.length());
        this.line = line;
    }

    public String getString() {
        return line;
    }

    public Integer getEndCharacterPosition() {
        return endCharacterPosition;
    }

    public void setEndCharacterPosition(Integer endCharacter) {
        this.endCharacterPosition = endCharacter;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "FileLine{" +
                "lineNumber=" + lineNumber +
                ", line='" + line + '\'' +
                ", endCharacterPosition=" + endCharacterPosition +
                '}';
    }
}
