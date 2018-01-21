package org.nsu.dcis.amv.core.util;

import org.apache.log4j.Logger;

/**
 * Created by jorgej2 on 12/28/2017.
 */
public class OutsideRelation {
    private TraceMethod context;
    private TraceMethod firstPart;
    private TraceMethod secondPart;

    private Logger log = Logger.getLogger(getClass().getName());

    public OutsideRelation(TraceMethod firstPart, TraceMethod secondPart) {
        this.firstPart = firstPart;
        this.secondPart = secondPart;
    }

    public OutsideRelation(TraceMethod context, TraceMethod firstPart, TraceMethod secondPart) {
        this.context = context;
        this.firstPart = firstPart;
        this.secondPart = secondPart;
    }

    public TraceMethod getContext() {
        return context;
    }

    public void setContext(TraceMethod context) {
        this.context = context;
    }

    public TraceMethod getFirstPart() {
        return firstPart;
    }

    public void setFirstPart(TraceMethod firstPart) {
        this.firstPart = firstPart;
    }

    public TraceMethod getSecondPart() {
        return secondPart;
    }

    public void setSecondPart(TraceMethod secondPart) {
        this.secondPart = secondPart;
    }

    @Override
    public String toString() {
        return "\n\n" + "outsideRelation{" +
                "\n" + "context='" + context + '\'' + "\n" +
                ", firstPart='" + firstPart + '\'' + "\n" +
                ", secondPart='" + secondPart + '\'' +
                '}';
    }

    public boolean hasCallingContext() {
        return getContext() != null;
    }

    public String getContextMethod() {
        if (context == null) {
            return null;
        }
        else {
            String contextLine = context.getLineReadFromFile();
            int startPos = contextLine.indexOf("Entering[");
            int endPos = contextLine.indexOf(")]");
            return contextLine.substring(startPos + 9, endPos + 1);
        }
    }

    public String getFirstMethod() {
        String firstPartLine = firstPart.getLineReadFromFile();
        int startPos = firstPartLine.indexOf("Entering[");
        int endPos = firstPartLine.indexOf(")]");
        return firstPartLine.substring(startPos + 9, endPos + 1);
    }

    public String getSecondMethod() {
        String secondPartLine = secondPart.getLineReadFromFile();
        int startPos = secondPartLine.indexOf("Entering[");
        int endPos = secondPartLine.indexOf(")]");
        return secondPartLine.substring(startPos + 9, endPos + 1);
    }

    public boolean equals(OutsideRelation outsideRelation) {
        if (this.getFirstMethod().equals(outsideRelation.getFirstMethod()) &&
                this.getSecondMethod().equals(outsideRelation.getSecondMethod())) {
            return true;
        }
        return false;
    }

    public boolean equalContext(OutsideRelation outsideRelation) {
        if (this.getContextMethod() != null && outsideRelation.getContextMethod() != null) {
            if (this.getContextMethod().equals(outsideRelation.getContextMethod())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
