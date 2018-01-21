package org.nsu.dcis.amv.core.util;

import org.apache.log4j.Logger;

/**
 * Created by jorgej2 on 12/10/2017.
 */
public class Relation {
    private TraceMethod context;
    private TraceMethod firstPart;
    private TraceMethod secondPart;

    private Logger log = Logger.getLogger(getClass().getName());

    public Relation(TraceMethod firstPart, TraceMethod secondPart) {
        this.firstPart = firstPart;
        this.secondPart = secondPart;
    }

    public Relation(TraceMethod context, TraceMethod firstPart, TraceMethod secondPart) {
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
        return "\n\n" + "Relation{" +
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

    public boolean equals(Relation relation) {
        if (this.getFirstMethod().equals(relation.getFirstMethod()) &&
            this.getSecondMethod().equals(relation.getSecondMethod())) {
            return true;
        }
        return false;
    }

    public boolean equalContext(Relation relation) {
        if (this.getContextMethod() != null && relation.getContextMethod() != null) {
            if (this.getContextMethod().equals(relation.getContextMethod())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
