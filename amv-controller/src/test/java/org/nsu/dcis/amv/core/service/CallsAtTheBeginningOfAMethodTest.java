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
import org.nsu.dcis.amv.core.util.MethodWithCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        MethodWithCall methodWithCall = null;

        List<MethodWithCall> methodWithCallList = new ArrayList<MethodWithCall>();
        List<MethodRepresentation> allMethodRepresentations = getAllMethodRepresentations();
//        log.info("Total number of method representations: " + allMethodRepresentations.size());

        for (MethodRepresentation callingMethodRepresentation : allMethodRepresentations) {
            String methodNameAtBeginning = getNameOfMethodCalledFrom(callingMethodRepresentation);
            if (calls(methodNameAtBeginning)) {
                MethodRepresentation calledMethodRepresentation = getMethodRepresentationFor(methodNameAtBeginning, allMethodRepresentations);
                if (exists(calledMethodRepresentation)) {
                    methodWithCall = new MethodWithCall(callingMethodRepresentation, calledMethodRepresentation);
                    if (!isDuplicate(testSet, methodWithCall)) {
                        testSet.add(methodWithCall.getCallingMethodRepresentationId());
                        methodWithCallList.add(methodWithCall);
                    }
                }
            }
        }
//        displayAllMethodsWithCallAtTheBeginning(methodWithCallList);
        getCrossCuttingConcernsForSetsOfCalledMethods(methodWithCallList);

//        getCrossCuttingConcernCandidatesFor(methodWithCallList);
    }

    private void getCrossCuttingConcernsForSetsOfCalledMethods(List<MethodWithCall> methodWithCallList) {
        Set<MethodWithCall> methodSet = null;
        Map<String, Set> methodSetMap = new HashMap<>();
        int idx = 0;
        for (MethodWithCall method : methodWithCallList) {
//            ++idx;
//            log.info(method);
            log.info("KEY: " + method.getCalledMethodRepresentationId());
            Set methodsSet = methodSetMap.get(method.getCalledMethodRepresentationId());
            if (methodsSet == null) {
                methodSet = new HashSet<>();
                methodSet.add(method);
                methodSetMap.put(method.getCalledMethodRepresentationId(), methodSet);
            } else {
                Set<MethodWithCall> methodNewSet = new HashSet(methodsSet);
                methodNewSet.add(method);
                methodSetMap.put(method.getCalledMethodRepresentationId(), methodNewSet);
            }
            if (idx > 9) break;
        }
        Set<String> keys = methodSetMap.keySet();
        for (String key : keys) {
            log.info("***************************");
            log.info(methodSetMap.get(key));
        }
    }

    private void getCrossCuttingConcernCandidatesFor(List<MethodWithCall> methodWithCallList) {
        Set<MethodWithCall> processedSet = new HashSet<>();
        Set<MethodWithCall> methodWithCallSet = new HashSet<>();
        Set<MethodWithCall> methodWithCallCopySet = new HashSet<>();
        int setIndex = 0;
        Map <Integer, Set<MethodWithCall>> integerSetMap = new ConcurrentHashMap();

        List<MethodWithCall> methodWithCallCopyList = new ArrayList<>(methodWithCallList);
        for (MethodWithCall methodWithCall : methodWithCallList) {
            if (!hasBeenProcessed(methodWithCall.getCalledMethodRepresentationId(), processedSet)) {
                boolean firstAdd = true;
                for (MethodWithCall methodWithCallCopy : methodWithCallCopyList) {
                    if (!hasBeenProcessed(methodWithCallCopy.getCalledMethodRepresentationId(), processedSet)) {
                        if (methodWithCall.getCalledMethodRepresentationId().equals(methodWithCallCopy.getCalledMethodRepresentationId())) {
                            if (!methodWithCall.getCallingMethodRepresentationId().equals(methodWithCallCopy.getCallingMethodRepresentationId())) {
                                if (firstAdd) {
                                    firstAdd = false;
                                    methodWithCallSet.add(methodWithCall);
                                    methodWithCallSet.add(methodWithCallCopy);
                                } else {
                                    methodWithCallSet.add(methodWithCallCopy);

    //                                boolean alreadyAdded = false;
    //                                methodWithCallCopySet = new HashSet<>(methodWithCallSet);
    //                                for (MethodWithCall callAtTheBeginning : methodWithCallCopySet) {
    //                                    if (callAtTheBeginning.getCallingMethodRepresentationId().equals(methodWithCallCopy.getCallingMethodRepresentationId())) {
    //                                        alreadyAdded = true;
    //                                    }
    //                                }
    //                                if (!alreadyAdded) {
    //                                    methodWithCallSet.add(methodWithCallCopy);
    //                                }
                                }
                            }
                        }
                    }
                }
                if (!methodWithCallSet.isEmpty()) {
                    integerSetMap.put(setIndex++, methodWithCallSet);
                }
            }
            processedSet.add(methodWithCall);
        }

        for (Integer key : integerSetMap.keySet()) {
            log.info("********************************************************************************");
            log.info("********************************************************************************");
            log.info("********************************************************************************");
            log.info("********************************************************************************");
            Set<MethodWithCall> methodWithCalls = integerSetMap.get(key);
            log.info(methodWithCalls);
        }
    }

    private boolean hasBeenProcessed(String calledMethodRepresentationId, Set<MethodWithCall> processedSet) {
        boolean hasAlreadyBeenProcessed = false;
        for (MethodWithCall methodWithCall : processedSet) {
            if (methodWithCall.getCalledMethodRepresentationId().equals(calledMethodRepresentationId)) {
                hasAlreadyBeenProcessed = true;
            }
        }
        return hasAlreadyBeenProcessed;
    }

    private boolean isDuplicate(Set<String> testSet, MethodWithCall methodWithCall) {
        return testSet.contains(methodWithCall.getCallingMethodRepresentationId());
    }

    private boolean exists(MethodRepresentation methodRepresentationForCalledMethod) {
        return methodRepresentationForCalledMethod != null;
    }

    private void displayAllMethodsWithCallAtTheBeginning(List<MethodWithCall> methodWithCallList) {
        for (MethodWithCall methodWithCall : methodWithCallList) {
            log.info("******************************************************************************************************");
            log.info(methodWithCall);
            log.info("******************************************************************************************************");
        }
        log.info("Total number of methods with call at beginning was: " + methodWithCallList.size());
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

            // A method representation was found for the given method
            if (methodRepresentation.getMethodName().equals(method)) {

                // This is the first method representation that has been found for the method. The method representation has not
                // already been set for this method so the returning method representation will therefore be set to the found
                // method representation.
                if (methodRepresentationForCalledMethod == null) {
                    methodRepresentationForCalledMethod = methodRepresentation;

                // The same method representation that has already been found was found again. Ignore this
                } else if (methodRepresentationForCalledMethod.getFullMethodName().equals(methodRepresentation.getFullMethodName())) {
//                    log.info("Duplicate method found");
//                    log.info("First found method:  " + methodRepresentationForCalledMethod.getFullMethodName());
//                    log.info("Second found method: " + methodRepresentation.getFullMethodName());

                // More than one method representation was found for the method. This represents ambiguity. Ambiguity is
                // not accepted and the returning MethodRepresentation is therefore set to null.
                } else {
//                    log.info("Ambiguity");
//                    log.info("First found method:  " + methodRepresentationForCalledMethod.getFullMethodName());
//                    log.info("Second found method: " + methodRepresentation.getFullMethodName());

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

    private static String getMethodNameAsStringFor(Statement statement) {
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
