package org.nsu.dcis.amv.core.domain;

/**
 * Created by jorgej2 on 2/4/2018.
 */
public class FanInConcernSeed {
    String line;
    int fanInValue = 0;

    public FanInConcernSeed(String line) {
        this.line = line;
        String fanInValueString = line.substring(line.indexOf(":") + 1, line.length());
        this.fanInValue = Integer.valueOf(fanInValueString.trim());
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getFanInValue() {
        return fanInValue;
    }

    public void setFanInValue(int fanInValue) {
        this.fanInValue = fanInValue;
    }

    @Override
    public String toString() {
        return "FanInConcernSeed{" +
                "line='" + line + '\'' +
                ", fanInValue=" + fanInValue +
                '}';
    }
}
