package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.extend.CompilationUnitWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsu.dcis.amv.core.domain.CallsInClone;
import org.nsu.dcis.amv.core.domain.CodeCloneResult;
import org.nsu.dcis.amv.core.instrumentation.AmvConfigurationInstrumentation;
import org.nsu.dcis.amv.core.util.CodeCloneMiningResult;
import org.nsu.dcis.amv.core.util.CodeCloneStatistics;
import org.nsu.dcis.amv.core.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgej2 on 2/13/2018.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CallsInClonesTest2 {

    @Autowired
    AmvConfigurationInstrumentation amvConfigurationInstrumentation;

    @Autowired
    private CodeCloneMiningService codeCloneMiningService;

    private Logger log = Logger.getLogger(getClass().getName());

    @Test
    public void getFirstClone() throws Exception {
        List<MethodRepresentation> allMethodRepresentations = getAllMethodRepresentations();
//        for (MethodRepresentation methodRepresentation : allMethodRepresentations) {
//            System.out.println(methodRepresentation.getMethodName());
//        }
//        firstClone.display();
//        System.out.println(namesOfMethodCalledFromMethod);

        CodeCloneMiningResult codeCloneMiningResult = getCodeCloneMiningResult();
        List<CodeCloneResult> clones = codeCloneMiningResult.getClone();
//        CodeCloneResult firstClone = getFirstClone(clones);

        for (CodeCloneResult clone : clones) {
            CallsInClone callsInClone = new CallsInClone();
            callsInClone.setCloneResult(clone);

            CompilationUnitWrapper compilationUnitWrapper = new CompilationUnitWrapper(clone.getMethodPair().getFirst().getFilePath());
            List<String> namesOfMethodCalledFromMethod = getNamesOfMethodCalledFromMethod(compilationUnitWrapper, clone);

            for (String nameOfMethodCalledFromMethod : namesOfMethodCalledFromMethod) {
                List<MethodRepresentation> methodRepresentationForCalledMethod =
                        getMethodRepresentationFor(nameOfMethodCalledFromMethod, allMethodRepresentations);

                Pair<String, List<MethodRepresentation>> nameOfMethodCalledFromMethodAndMethodRepresentation =
                        new Pair<>(nameOfMethodCalledFromMethod, methodRepresentationForCalledMethod);
                callsInClone.add(nameOfMethodCalledFromMethodAndMethodRepresentation);
            }
            callsInClone.display();
        }
    }

    /**
     * This method returns the method representation for the given method if this exists.
     * If no method representation is found then an empty list is returned.
     *
     * @param method
     * @param allMethodRepresentations
     * @return List of method representations
     */
    private List<MethodRepresentation> getMethodRepresentationFor(String method,
            List<MethodRepresentation> allMethodRepresentations) {
        List<MethodRepresentation> methodRepresentationsForCalledMethod = new ArrayList<>();
        for (MethodRepresentation methodRepresentation : allMethodRepresentations) {
            if (methodRepresentation.getMethodName().equals(method)) {
                if (methodRepresentationsForCalledMethod.size() == 0) {
                    methodRepresentationsForCalledMethod.add(methodRepresentation);
                } else {
                    log.info("*******************************************");
                    log.info("*******************************************");
                    log.info(methodRepresentation.getFullMethodName());
                    log.info("*******************************************");
                    log.info("*******************************************");
                    return new ArrayList<MethodRepresentation>();
                }
            }
        }
        return methodRepresentationsForCalledMethod;
    }

    private CodeCloneResult getFirstClone(List<CodeCloneResult> clones) {
        return clones.get(0);
    }

    @Test
    public void getCodeCloningResultsForAllClasses() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult = getCodeCloneMiningResult();

//        displayCodeCloneResults(codeCloneMiningResult);
        CodeCloneStatistics codeCloneStatistics = codeCloneMiningResult.getCodeCloneStatistics();
        log.info("Code Clone Statistics: " + codeCloneStatistics);
        displayClones(codeCloneMiningResult);
        // Displays the results
        displayCloneStatistics(codeCloneMiningResult.getCodeCloneResults());
    }

    private CodeCloneMiningResult getCodeCloneMiningResult() {
        return codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                amvConfigurationInstrumentation.getExcludedDirectoryList(),
                amvConfigurationInstrumentation.getFileExtensions());
    }

    private void displayClones(CodeCloneMiningResult codeCloneMiningResult) {
        List<CodeCloneResult> clones = codeCloneMiningResult.getClone();
        log.info("**********************************************************************************");
        log.info("The Clone Count was: " + codeCloneMiningResult.getClone().size());
        log.info("**********************************************************************************");
        for (CodeCloneResult clone : clones) {
            log.info("**********************************************************************************");
            clone.display();
            log.info("**********************************************************************************");
        }
    }

    private CodeCloneStatistics displayCloneStatistics(List<CodeCloneResult> codeCloneResults) {
        int emptyCount = 0;
        int cloneCount = 0;
        int beforeAdvice = 0;
        int aroundAdvice = 0;
        int afterAdvice = 0;
        for (CodeCloneResult codeCloneResult : codeCloneResults) {
            switch (codeCloneResult.getType()) {
                case EMPTY:
                    emptyCount++;
                    break;
                case CLONE:
                    cloneCount++;
//                    inspectCloneResult(codeCloneResult);
                    break;
                case BEFORE_ADVICE_CANDIDATE:
                    beforeAdvice++;
                    break;
                case AROUND_ADVICE_CANDIDATE:
                    aroundAdvice++;
                    break;
                case AFTER_ADVICE_CANDIDATE:
                    afterAdvice++;
            }
        }

        log.info("Empty:  " + emptyCount);
        log.info("Clone:  " + cloneCount);
        log.info("Before: " + beforeAdvice);
        log.info("Around: " + aroundAdvice);
        log.info("After:  " + afterAdvice);
        return new CodeCloneStatistics(emptyCount, cloneCount, beforeAdvice, aroundAdvice, afterAdvice);
    }

    public List<String> getNamesOfMethodCalledFromMethod(CompilationUnitWrapper compilationUnitWrapper, CodeCloneResult firstClone) {
        List<String> namesOfMethodsCalledFromMethodAsString = new ArrayList<>();
        List<Statement> namesOfMethodsCalledFromMethod = compilationUnitWrapper.getNamesOfMethodsCalledFromMethod(
                                                         firstClone.getMethodPair().getFirst().getMethodName(), CompilationUnitWrapper.ALL_METHODS);
        for (Statement statement : namesOfMethodsCalledFromMethod) {
            String methodNameAsString = getMethodNameAsStringFor(statement);
            if (!StringUtils.isBlank(methodNameAsString)) {
                namesOfMethodsCalledFromMethodAsString.add(methodNameAsString);
            }
        }
        return namesOfMethodsCalledFromMethodAsString;
    }

    private String getMethodNameAsStringFor(Statement statement) {
        String statementAsString = statement.toString().replaceAll(" ","");
        if (statementAsString.startsWith("//")) {
            return "";
        } else {
            int periodPos = statementAsString.indexOf(".");
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
}
