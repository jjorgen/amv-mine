package org.nsu.dcis.amv.core.util;

import com.github.javaparser.ast.MethodRepresentation;

/**
 * Created by jorgej2 on 2/24/2018.
 */
public class MethodWithCall {

    // This is the method that calls the common method
    private MethodRepresentation callingMethodRepresentation;

    // This is the common method that is being called
    private MethodRepresentation calledMethodRepresentation;

    public MethodWithCall(MethodRepresentation callingMethodRepresentation, MethodRepresentation calledMethodRepresentation) {
        this.callingMethodRepresentation = callingMethodRepresentation;
        this.calledMethodRepresentation = calledMethodRepresentation;
    }

    public String getCallingMethodRepresentationId() {
//        return callingMethodRepresentation.getFilePath() + " : " + callingMethodRepresentation.getFullMethodName();
        return callingMethodRepresentation.getFullMethodName();
    }

    public String getCalledMethodRepresentationId() {
//        return calledMethodRepresentation.getFilePath() + " : " + calledMethodRepresentation.getFullMethodName();
        return calledMethodRepresentation.getFullMethodName();
    }

    @Override
    public String toString() {
        return "\nMethod calls common method path:           '" + callingMethodRepresentation.getFilePath() + "'\n" +
                 "Method Name of Method calls common method: '" + callingMethodRepresentation.getFullMethodName()  + "'\n" +
                 "Common called Method Representation path:  '" + calledMethodRepresentation.getFilePath() + "'\n" +
                 "*** Common called Full Method Name:        '" + calledMethodRepresentation.getFullMethodName() + "'";

//        return "\nMethod Name calling common method: '" + callingMethodRepresentation.getFullMethodName()  + "'\n" +
//                 "Common called method name:         '" + calledMethodRepresentation.getFullMethodName() + "'";
    }
}
