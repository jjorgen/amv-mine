package org.nsu.dcis.amv.core.util;

import com.github.javaparser.ast.MethodRepresentation;

/**
 * Created by jorgej2 on 2/24/2018.
 */
public class MethodWithCallAtTheBeginning {
    private MethodRepresentation callingMethodRepresentation;
    private MethodRepresentation calledMethodRepresentation;

    public MethodWithCallAtTheBeginning(MethodRepresentation callingMethodRepresentation, MethodRepresentation calledMethodRepresentation) {
        this.callingMethodRepresentation = callingMethodRepresentation;
        this.calledMethodRepresentation = calledMethodRepresentation;
    }

    public String getSignature() {
        return callingMethodRepresentation.getFilePath() + " : " + callingMethodRepresentation.getFullMethodName();
    }

    @Override
    public String toString() {
        return "\nClass File Path of Calling Method:      '" + callingMethodRepresentation.getFilePath() + "'\n" +
                 "Calling Method Name:                    '" + callingMethodRepresentation.getFullMethodName()  + "'\n" +
                 "Called Method Representation file path: '" + calledMethodRepresentation.getFilePath() + "'\n" +
                 "Called Full Method Name:                '" + calledMethodRepresentation.getFullMethodName() + "'";
    }
}
