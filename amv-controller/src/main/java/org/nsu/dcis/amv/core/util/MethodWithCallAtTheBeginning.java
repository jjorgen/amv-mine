package org.nsu.dcis.amv.core.util;

import com.github.javaparser.ast.MethodRepresentation;

/**
 * Created by jorgej2 on 2/24/2018.
 */
public class MethodWithCallAtTheBeginning {
    private MethodRepresentation methodRepresentation;
    private String nameOfMethodCalled;
    private MethodRepresentation methodRepresentationCalled;

    public MethodWithCallAtTheBeginning(MethodRepresentation methodRepresentation, String nameOfMethodCalled) {
        this.methodRepresentation = methodRepresentation;
        this.nameOfMethodCalled = nameOfMethodCalled;
    }

    public void addCalledMethodRepresentation(MethodRepresentation methodRepresentationCalled) {
        this.methodRepresentationCalled = methodRepresentationCalled;
    }

    public boolean hasCalledMethodRepresentation() {
        return (methodRepresentationCalled != null) ? true : false;
    }

    public MethodRepresentation  getCalledMethodRepresentation() {
        return methodRepresentationCalled;
    }

    public String getSignature() {
        return methodRepresentation.getFilePath() + " : " + methodRepresentation.getFullMethodName();
    }

    @Override
    public String toString() {
        return "\nClass File Path of Calling Method:      '" + methodRepresentation.getFilePath() + "'\n" +
                 "Calling Method Name:                    '" + methodRepresentation.getFullMethodName()  + "'\n" +
                 "Called Method:                          '" + nameOfMethodCalled  + "'\n" +
                 "Called Method Representation file path: '" + methodRepresentationCalled.getFilePath() + "'\n" +
                 "Called Full Method Name:                '" + methodRepresentationCalled.getFullMethodName() + "'";
    }
}
