package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.extend.CompilationUnitWrapper;
import com.sun.org.apache.xpath.internal.SourceTree;
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

        List<MethodWithCallAtTheBeginning> methodsWithCallAtTheBeginning = new ArrayList<MethodWithCallAtTheBeginning>();
        List<MethodRepresentation> allMethodRepresentations = getAllMethodRepresentations();
        log.info("Total number of method representations: " + allMethodRepresentations.size());
        for (MethodRepresentation methodRepresentation : allMethodRepresentations) {
            String nameOfMethodCalledFromMethod = getNamesOfMethodCalledFrom(methodRepresentation);
            if (!StringUtils.isBlank(nameOfMethodCalledFromMethod)) {
                MethodWithCallAtTheBeginning methodWithCallAtTheBeginning =
                        new MethodWithCallAtTheBeginning(methodRepresentation, nameOfMethodCalledFromMethod);
                if (!testSet.contains(methodWithCallAtTheBeginning.getSignature())) {
                    testSet.add(methodWithCallAtTheBeginning.getSignature());

                    methodsWithCallAtTheBeginning.add(methodWithCallAtTheBeginning);
                    List<MethodRepresentation> methodRepresentationsForCalledMethod =
                            getMethodRepresentationFor(nameOfMethodCalledFromMethod, allMethodRepresentations);

                    if (!methodRepresentationsForCalledMethod.isEmpty()) {
                        methodWithCallAtTheBeginning.addCalledMethodRepresentation(methodRepresentationsForCalledMethod.iterator().next());
                    }
                }
            }
        }
        for (MethodWithCallAtTheBeginning methodWithCallAtTheBeginning : methodsWithCallAtTheBeginning) {
            if (methodWithCallAtTheBeginning.hasCalledMethodRepresentation()) {
                log.info(methodWithCallAtTheBeginning);
            }
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

    private String getNamesOfMethodCalledFrom(MethodRepresentation methodRepresentation) {
        String nameOfMethodCalledFromMethodAsString= null;
        CompilationUnitWrapper compilationUnitWrapper = new CompilationUnitWrapper(methodRepresentation.getFilePath());
        List<Statement> namesOfMethodsCalledFromMethod =
                compilationUnitWrapper.getNamesOfMethodsCalledFromMethod(methodRepresentation.getMethodName(),CompilationUnitWrapper.METHOD_AT_BEGINNING);

        for (Statement statement : namesOfMethodsCalledFromMethod) {
            String methodNameAsString = getMethodNameAsStringFor(statement);
            if (!StringUtils.isBlank(methodNameAsString)) {
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
}
