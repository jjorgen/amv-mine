package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.extend.CompilationUnitWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsu.dcis.amv.core.domain.CodeCloneResult;
import org.nsu.dcis.amv.core.instrumentation.AmvConfigurationInstrumentation;
import org.nsu.dcis.amv.core.util.MethodWithCallAtTheBeginning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jorgej2 on 2/19/2018.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CallsAtTheBeginningOfAMethodTest {

    @Autowired
    AmvConfigurationInstrumentation amvConfigurationInstrumentation;

    @Autowired
    private CodeCloneMiningService codeCloneMiningService;

    private Logger log = Logger.getLogger(getClass().getName());

    @Test
    public void commonCallsAtTheBeginningOfAMethod() throws Exception {
        Set<String> testSet = new HashSet<>();
        MethodWithCallAtTheBeginning methodWithCallAtTheBeginning = null;

        List<MethodWithCallAtTheBeginning> methodWithCallAtTheBeginningList = new ArrayList<MethodWithCallAtTheBeginning>();
        List<MethodRepresentation> allMethodRepresentations = getAllMethodRepresentations();
        log.info("Total number of method representations: " + allMethodRepresentations.size());

        for (MethodRepresentation callingMethodRepresentation : allMethodRepresentations) {
            String methodNameAtBeginning = getNameOfMethodCalledFrom(callingMethodRepresentation);
            if (calls(methodNameAtBeginning)) {
                MethodRepresentation calledMethodRepresentation = getMethodRepresentationFor(methodNameAtBeginning, allMethodRepresentations);
                if (exists(calledMethodRepresentation)) {
                    methodWithCallAtTheBeginning = new MethodWithCallAtTheBeginning(callingMethodRepresentation, calledMethodRepresentation);
                    if (!isDuplicate(testSet, methodWithCallAtTheBeginning)) {
                        testSet.add(methodWithCallAtTheBeginning.getSignature());
                        methodWithCallAtTheBeginningList.add(methodWithCallAtTheBeginning);
                    }
                }
            }
        }
        displayAllMethodsWithCallAtTheBeginning(methodWithCallAtTheBeginningList);
    }

    private boolean isDuplicate(Set<String> testSet, MethodWithCallAtTheBeginning methodWithCallAtTheBeginning) {
        return testSet.contains(methodWithCallAtTheBeginning.getSignature());
    }

    private boolean exists(MethodRepresentation methodRepresentationForCalledMethod) {
        return methodRepresentationForCalledMethod != null;
    }

    private void displayAllMethodsWithCallAtTheBeginning(List<MethodWithCallAtTheBeginning> methodWithCallAtTheBeginningList) {
        for (MethodWithCallAtTheBeginning methodWithCallAtTheBeginning : methodWithCallAtTheBeginningList) {
            log.info("******************************************************************************************************");
            log.info(methodWithCallAtTheBeginning);
            log.info("******************************************************************************************************");
        }
        log.info("Total number of methods with call at beginning was: " + methodWithCallAtTheBeginningList.size());
    }

    private boolean calls(String nameOfMethodCalledFromMethod) {
        return !StringUtils.isBlank(nameOfMethodCalledFromMethod);
    }

    /**
     * This method returns the method representation for the given method if this exists.
     * If no method representation is found then an empty list is returned.
     *
     * @param method
     * @param allMethodRepresentations
     * @return List of method representations
     */
    private MethodRepresentation getMethodRepresentationFor(String method, List<MethodRepresentation> allMethodRepresentations) {
        MethodRepresentation methodRepresentationForCalledMethod =  null;
        for (MethodRepresentation methodRepresentation : allMethodRepresentations) {
            if (methodRepresentation.getMethodName().equals(method)) {
                if (methodRepresentationForCalledMethod == null) {
                    methodRepresentationForCalledMethod = methodRepresentation;
                } else if (methodRepresentationForCalledMethod.getFullMethodName().equals(methodRepresentation.getFullMethodName())) {
                    // Duplicate method found, ignore this.
                    log.info("Duplicate method found");
                    log.info("First found method:  " + methodRepresentationForCalledMethod.getFullMethodName());
                    log.info("Second found method: " + methodRepresentation.getFullMethodName());

                } else {
                    log.info("Ambiguity");
                    log.info("First found method:  " + methodRepresentationForCalledMethod.getFullMethodName());
                    log.info("Second found method: " + methodRepresentation.getFullMethodName());

//                    log.info("*******************************************");
//                    log.info("*******************************************");
//                    log.info(methodRepresentation.getFullMethodName());
//                    log.info("*******************************************");
//                    log.info("*******************************************");
                    return null;
                }
            }
        }
        return methodRepresentationForCalledMethod;
    }

    private String getNameOfMethodCalledFrom(MethodRepresentation methodRepresentation) {
        String nameOfMethodCalledFromMethodAsString = null;
        CompilationUnitWrapper compilationUnitWrapper = new CompilationUnitWrapper(methodRepresentation.getFilePath());

        if (methodRepresentation.getFilePath().equals("C:\\work\\0_NSU\\CH\\ifa\\draw\\util\\UndoableTool.java") &&
            methodRepresentation.getFullMethodName().equals("UndoableTool.toolDeactivated")) {
            System.out.println("found");
        }

        List<Statement> namesOfMethodsCalledFromMethod =
            compilationUnitWrapper.getNamesOfMethodsCalledFromMethod(methodRepresentation.getMethodName(),CompilationUnitWrapper.METHOD_AT_BEGINNING);

        for (Statement statement : namesOfMethodsCalledFromMethod) {
            String methodNameAsString = getMethodNameAsStringFor(statement);
            if (calls(methodNameAsString)) {
                nameOfMethodCalledFromMethodAsString = methodNameAsString;
            }
        }
        return nameOfMethodCalledFromMethodAsString;
    }

    private String getMethodNameAsStringFor(Statement statement) {
        String statementAsString = statement.toString().replaceAll(" ","");
        if (statementAsString.startsWith("//")) {
            return "";
        } else {
            int periodPos = statementAsString.lastIndexOf(".");
//            int periodPos = statementAsString.indexOf(".");
            if (!(periodPos > 0)) {
                return "";
            } else {
                if (periodPos == statementAsString.length()) {
                    return "";
                } else {
                    String methodString = statementAsString.substring(periodPos + 1, statementAsString.length() - 1);
                    if (!methodString.contains("(")) {
                        return "";
                    } else {
                        int parenthesesPos = methodString.indexOf("(");
                        return methodString.substring(0, parenthesesPos);
                    }
                }
            }
        }
    }

    public List<MethodRepresentation> getAllMethodRepresentations() {
        return codeCloneMiningService.getAllMethodRepresentations(amvConfigurationInstrumentation.getRootDir(),
                amvConfigurationInstrumentation.getExcludedDirectoryList(),
                amvConfigurationInstrumentation.getFileExtensions());
    }

    public List<String> getNamesOfMethodCalledFromMethod(CompilationUnitWrapper compilationUnitWrapper, CodeCloneResult firstClone) {
        List<String> namesOfMethodsCalledFromMethodAsString = new ArrayList<>();
        List<Statement> namesOfMethodsCalledFromMethod = compilationUnitWrapper.getNamesOfMethodsCalledFromMethod(
                firstClone.getMethodPair().getFirst().getMethodName(), CompilationUnitWrapper.ALL_METHODS);
        for (Statement statement : namesOfMethodsCalledFromMethod) {
            String methodNameAsString = getMethodNameAsStringFor(statement);
            if (calls(methodNameAsString)) {
                namesOfMethodsCalledFromMethodAsString.add(methodNameAsString);
            }
        }
        return namesOfMethodsCalledFromMethodAsString;
    }
}
