package org.nsu.dcis.amv.core.util;

import org.apache.log4j.Logger;

/**
 * Created by jorgej2 on 12/10/2017.
 */
public class InsideRelation {
    private TraceMethod context;
    private TraceMethod firstPart;
    private TraceMethod secondPart;

    private Logger log = Logger.getLogger(getClass().getName());

    public InsideRelation(TraceMethod firstPart, TraceMethod secondPart) {
        this.firstPart = firstPart;
        this.secondPart = secondPart;
    }

    public InsideRelation(TraceMethod context, TraceMethod firstPart, TraceMethod secondPart) {
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
        return "\n\n" + "InsideRelation{" +
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

    public boolean equals(InsideRelation insideRelation) {
        if (this.getFirstMethod().equals(insideRelation.getFirstMethod()) &&
            this.getSecondMethod().equals(insideRelation.getSecondMethod())) {
            return true;
        }
        return false;
    }

    public boolean equalContext(InsideRelation insideRelation) {
        if (this.getContextMethod() != null && insideRelation.getContextMethod() != null) {
            if (this.getContextMethod().equals(insideRelation.getContextMethod())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
