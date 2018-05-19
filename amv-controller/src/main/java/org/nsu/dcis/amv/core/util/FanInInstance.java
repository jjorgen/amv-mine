package org.nsu.dcis.amv.core.util;

import java.util.*;

/**
 * Created by jorgej2 on 3/31/2018.
 */
public class FanInInstance {

    private static final String METHOD_AND_FAN_IN_SEPARATOR_CHARACTER = ":";
    private List<String> callerMethodLines = new ArrayList<>();
    private String calledMethodLine;
    private String calledMethod;
    private int fanInValue = 0;
    List<String> fanInItems = new ArrayList<>();
    private int uniqueCallingClassesCount;

    public FanInInstance(String calledMethodLine) {
        this.calledMethodLine = calledMethodLine;
        setCalledMethod();
        setFanInValue();
    }

    private void setCalledMethod() {
        int methodAndFanInSeparatorIndex = getMethodAndFanInSeparatorIndex();
        String methodName = calledMethodLine.substring(0, methodAndFanInSeparatorIndex).trim();
        this.calledMethod = methodName;
    }

    private void setFanInValue() {
        int methodAndFanInSeparatorIndex = getMethodAndFanInSeparatorIndex();
        String fanInValueAsString = calledMethodLine.substring(methodAndFanInSeparatorIndex + 2, calledMethodLine.length());
        this.fanInValue = Integer.valueOf(fanInValueAsString);
    }

    public static boolean isStartOfFanInInstance(String lineFromFile) {
        if ("CH.".equals(lineFromFile.substring(0, 3))) {
            return true;
        }
        return false;
    }

    public void addItem(String lineFromFile) {
        this.callerMethodLines.add(lineFromFile);
    }

    private int getMethodAndFanInSeparatorIndex() {
        return calledMethodLine.indexOf(METHOD_AND_FAN_IN_SEPARATOR_CHARACTER);
    }

    private String getCalledMethod() {
        return calledMethod;
    }

    public int getFanInValue() {
        return fanInValue;
    }

    public int getNumberOfUniqueCallingClasses() {
        if (callerMethodLines.size() == 0) {
            return 0;
        } else {
            return getUniqueCallingClassesCount();
        }
    }

    public int getUniqueCallingClassesCount() {
        Set<String> uniqueCallingClasses = new HashSet();
        for (String callerMethodLine : callerMethodLines) {
            uniqueCallingClasses.add(getClassName(callerMethodLine));
        }
        return uniqueCallingClasses.size();
    }

    public String getClassName(String callerMethodLine) {
        String className = null;
        Character firstCharacter = null;
        StringTokenizer methodPart = new StringTokenizer(callerMethodLine, ".");
        methodPart.nextElement();
        while(methodPart.hasMoreTokens()) {
            String element = (String)methodPart.nextElement();
            firstCharacter = element.charAt(0);
            if (Character.isUpperCase(firstCharacter)) {
                className = (String)element;
                break;
            }
        }
        return className;
    }

    public String getVectorInstance(int lineNumber) {
//        return fanInValue + " " + getNumberOfUniqueCallingClasses();
        return calledMethod + " " + fanInValue + " " + getNumberOfUniqueCallingClasses();
    }

    public String getVectorInstanceForRFile(int lineNumber) {
        return fanInValue + "," + getNumberOfUniqueCallingClasses();
//        return lineNumber + " - " + fanInValue + "," + getNumberOfUniqueCallingClasses();
    }
}
