package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.extend.CompilationUnitWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.util.MethodWithCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jorgej2 on 3/14/2018.
 */
@Service
public class CallsAtTheEndOfMethodService {

    @Autowired
    private CommonMiningService commonMiningService;

    private Logger log = Logger.getLogger(getClass().getName());

    public int commonCallsAtTheEndOfAMethod() {
        Set<String> testSet = new HashSet<>();
        MethodWithCall methodWithCall = null;
        List<MethodRepresentation> allMethodRepresentations = getAllMethodRepresentations();
        List<MethodWithCall> methodWithCallList = new ArrayList<>();

        for (MethodRepresentation callingMethodRepresentation : allMethodRepresentations) {
            String methodAtTheEnd = getNameOfMethodCalledFrom(callingMethodRepresentation);
            if (calls(methodAtTheEnd)) {
                MethodRepresentation calledMethodRepresentation = getMethodRepresentationFor(methodAtTheEnd, allMethodRepresentations);
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
        return getCrossCuttingConcernsForSetsOfCalledMethods(methodWithCallList);

//        getCrossCuttingConcernCandidatesFor(methodWithCallList);
    }

    private int getCrossCuttingConcernsForSetsOfCalledMethods(List<MethodWithCall> methodWithCallList) {
        Set<MethodWithCall> methodSet = null;
        Map<String, Set> methodSetMap = new HashMap<>();
        int idx = 0;
        for (MethodWithCall method : methodWithCallList) {
//            ++idx;
//            log.info(method);
//            log.info("KEY: " + method.getCalledMethodRepresentationId());
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
        log.info("Number of sets of methods called at end was: " + keys.size());
        return keys.size();
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

    private boolean calls(String methodAtTheEnd) {
        return !StringUtils.isBlank(methodAtTheEnd);
    }

    private boolean exists(MethodRepresentation methodRepresentationForCalledMethod) {
        return methodRepresentationForCalledMethod != null;
    }

    private boolean isDuplicate(Set<String> testSet, MethodWithCall methodWithCall) {
        return testSet.contains(methodWithCall.getCallingMethodRepresentationId());
    }

    private String getNameOfMethodCalledFrom(MethodRepresentation methodRepresentation) {
        CompilationUnitWrapper compilationUnitWrapper = new CompilationUnitWrapper(methodRepresentation.getFilePath());

        if (methodRepresentation.getFilePath().equals("C:\\work\\0_NSU\\CH\\ifa\\draw\\util\\UndoableTool.java") &&
                methodRepresentation.getFullMethodName().equals("UndoableTool.toolDeactivated")) {
        }

        String nameOfLastMethodCalledFromMethod =
                compilationUnitWrapper.getNameOfLastMethodCalledFromMethod(methodRepresentation.getMethodName());

        return nameOfLastMethodCalledFromMethod;
    }
    public List<MethodRepresentation> getAllMethodRepresentations() {
        return commonMiningService.getAllMethodRepresentations();

//        return commonMiningService.getAllMethodRepresentations(amvConfigurationInstrumentation.getRootDir(),
//                amvConfigurationInstrumentation.getExcludedDirectoryList(),
//                amvConfigurationInstrumentation.getFileExtensions());
    }


}
