package org.nsu.dcis.amv.core.domain;

import com.github.javaparser.ast.MethodRepresentation;
import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgej2 on 2/14/2018.
 */
public class CallsInClone {
    private Logger log = Logger.getLogger(getClass().getName());

    private CodeCloneResult cloneResult;
    private List<Pair<String, List<MethodRepresentation>>> nameOfMethodCalledFromMethodAndMethodRepresentations = new ArrayList<>();

    public void setCloneResult(CodeCloneResult cloneResult) {
        this.cloneResult = cloneResult;
    }

    public void add(Pair<String, List<MethodRepresentation>> nameOfMethodCalledFromMethodAndMethodRepresentation) {
        this.nameOfMethodCalledFromMethodAndMethodRepresentations.add(nameOfMethodCalledFromMethodAndMethodRepresentation);
    }

    public void display() {
        cloneResult.display();

        for (int i = 0;  nameOfMethodCalledFromMethodAndMethodRepresentations.size() > i; i++) {
            Pair<String, List<MethodRepresentation>> nameOfMethodCalledFromMethodAndMethodRepresentation =
                                                     nameOfMethodCalledFromMethodAndMethodRepresentations.get(i);

            List<MethodRepresentation> methodRepresentation = nameOfMethodCalledFromMethodAndMethodRepresentation.getSecond();
            log.info("Calling Method: [" + nameOfMethodCalledFromMethodAndMethodRepresentation.getFirst() +
                     "], Called Method: " + (methodRepresentation.size() > 0 ? "[" + methodRepresentation.get(0).getFilePath() + " - " +
                    methodRepresentation.get(0).getFullMethodName() + "]" : "[none]"));
//
//            if (methodRepresentation.size() > 0) {
//                log.info("Called Method: " + methodRepresentation.get(0).getFullMethodName());
//            } else {
//                log.info("Called Method: [none]");
//            }
        }
    }
}
