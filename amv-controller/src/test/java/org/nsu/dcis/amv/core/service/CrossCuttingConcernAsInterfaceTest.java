package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.extend.CompilationUnitWrapper;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsu.dcis.amv.core.domain.CodeCloneResult;
import org.nsu.dcis.amv.core.exception.AspectCloneException;
import org.nsu.dcis.amv.core.instrumentation.AmvConfigurationInstrumentation;
import org.nsu.dcis.amv.core.util.CodeCloneMiningResult;
import org.nsu.dcis.amv.core.util.CodeCloneStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jorgej2 on 1/23/2018.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CrossCuttingConcernAsInterfaceTest {

    @Autowired
    AmvConfigurationInstrumentation amvConfigurationInstrumentation;

    @Autowired
    private CodeCloneMiningService codeCloneMiningService;

    private static final String ROOT_DIRECTORY = "C:/WS/amvCore/src/test/java/org/nsu/dcis/amv/core/support";
    private Logger log = Logger.getLogger(getClass().getName());

    @Test
    public void getCrossCuttingConcernsAsInterfaceTest() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
                codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                        amvConfigurationInstrumentation.getExcludedDirectoryList(),
                        amvConfigurationInstrumentation.getFileExtensions());

        CodeCloneStatistics codeCloneStatistics = codeCloneMiningResult.getCodeCloneStatistics();
        log.info("Code Clone Statistics: " + codeCloneStatistics);
        displayBeforeAdviceCrossCuttingConcernAsInterfaceCandidates(codeCloneMiningResult);
        displayAfterCandidatesAsInterfaceCandidates(codeCloneMiningResult);
        displayAroundAdviceCandidatesAsInterfaceCandidates(codeCloneMiningResult);
        displayClonesAsInterface(codeCloneMiningResult);
    }

    @Test
    public void getBeforeCrossCuttingConcernsAsInterfaceTest() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
                codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                        amvConfigurationInstrumentation.getExcludedDirectoryList(),
                        amvConfigurationInstrumentation.getFileExtensions());

        CodeCloneStatistics codeCloneStatistics = codeCloneMiningResult.getCodeCloneStatistics();
        log.info("Code Clone Statistics: " + codeCloneStatistics);
        displayBeforeAdviceCrossCuttingConcernAsInterfaceCandidates(codeCloneMiningResult);
    }

    @Test
    public void getAfterCrossCuttingConcernsAsInterfaceTest() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
                codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                        amvConfigurationInstrumentation.getExcludedDirectoryList(),
                        amvConfigurationInstrumentation.getFileExtensions());

        CodeCloneStatistics codeCloneStatistics = codeCloneMiningResult.getCodeCloneStatistics();
        log.info("Code Clone Statistics: " + codeCloneStatistics);
        displayAfterCandidatesAsInterfaceCandidates(codeCloneMiningResult);
    }

    @Test
    public void getAroundCrossCuttingConcernsAsInterfaceTest() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
                codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                        amvConfigurationInstrumentation.getExcludedDirectoryList(),
                        amvConfigurationInstrumentation.getFileExtensions());

        CodeCloneStatistics codeCloneStatistics = codeCloneMiningResult.getCodeCloneStatistics();
        log.info("Code Clone Statistics: " + codeCloneStatistics);
        displayAroundAdviceCandidatesAsInterfaceCandidates(codeCloneMiningResult);

    }

    @Test
    public void getCodeClonesAsInterfaceTest() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
                codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                        amvConfigurationInstrumentation.getExcludedDirectoryList(),
                        amvConfigurationInstrumentation.getFileExtensions());

        displayClonesAsInterface(codeCloneMiningResult);
    }

    private void displayClonesAsInterface(CodeCloneMiningResult codeCloneMiningResult) {
        int codeCloneWithComminInterface = 0;
        List<CodeCloneResult> codeClones = codeCloneMiningResult.getClone();
        log.info("**********************************************************************************");
        log.info("The Clone Count was: " + codeCloneMiningResult.getClone().size());
        log.info("**********************************************************************************");
        for (CodeCloneResult CodeCloneResult : codeClones) {
            NodeList<ClassOrInterfaceType> firstMethodImplements = methodImplements(CodeCloneResult.getMethodPair().getFirst());
            NodeList<ClassOrInterfaceType> secondMethodImplements = methodImplements(CodeCloneResult.getMethodPair().getSecond());
            Set<String> commonInterfaceNames = getInterfacesInCommon(firstMethodImplements, secondMethodImplements);
            if (commonInterfaceNames.size() > 0) {
                ++codeCloneWithComminInterface;
                log.info("First Method Name:  " + CodeCloneResult.getMethodPair().getFirst().getFullMethodName());
                log.info("Second Method Name: " + CodeCloneResult.getMethodPair().getSecond().getFullMethodName());
                log.info("With common interface(s) found: " + commonInterfaceNames);
                log.info("**********************************************************************************");
            }
        }
        log.info("**********************************************************************************");
        log.info("The number of Code Clones with common interface was: " + codeCloneWithComminInterface);
        log.info("**********************************************************************************");
    }

    private void displayBeforeAdviceCrossCuttingConcernAsInterfaceCandidates(CodeCloneMiningResult codeCloneMiningResult) {
        int beforeAdviceCandidatesWithComminInterface = 0;
        List<CodeCloneResult> beforeAdviceCandidates = codeCloneMiningResult.getBeforeAdviceCandidates();
        log.info("**********************************************************************************");
        log.info("The Before Advice Candidate Count was: " + codeCloneMiningResult.getBeforeAdviceCandidates().size());
        log.info("**********************************************************************************");
        for (CodeCloneResult codeCloneResult : beforeAdviceCandidates) {
            NodeList<ClassOrInterfaceType> firstMethodImplements = methodImplements(codeCloneResult.getMethodPair().getFirst());
            NodeList<ClassOrInterfaceType> secondMethodImplements = methodImplements(codeCloneResult.getMethodPair().getSecond());
            Set<String> commonInterfaceNames = getInterfacesInCommon(firstMethodImplements, secondMethodImplements);
            if (commonInterfaceNames.size() > 0) {
                ++beforeAdviceCandidatesWithComminInterface;
                log.info("First Method Name:  " + codeCloneResult.getMethodPair().getFirst().getFullMethodName());
                log.info("Second Method Name: " + codeCloneResult.getMethodPair().getSecond().getFullMethodName());
                log.info("With common interface(s) found: " + commonInterfaceNames);
                log.info("**********************************************************************************");
            }
        }
        log.info("**********************************************************************************");
        log.info("The number of Before Advice Candidates with common interface was: " + beforeAdviceCandidatesWithComminInterface);
        log.info("**********************************************************************************");
    }

    private void displayAfterCandidatesAsInterfaceCandidates(CodeCloneMiningResult codeCloneMiningResult) {
        int afterAdviceCandidatesWithComminInterface = 0;
        List<CodeCloneResult> afterAdviceCandidates = codeCloneMiningResult.getAfterAdviceCandidates();
        log.info("**********************************************************************************");
        log.info("The After Advice Candidate Count was: " + codeCloneMiningResult.getAfterAdviceCandidates().size());
        log.info("**********************************************************************************");
        for (CodeCloneResult codeCloneResult : afterAdviceCandidates) {
            NodeList<ClassOrInterfaceType> firstMethodImplements = methodImplements(codeCloneResult.getMethodPair().getFirst());
            NodeList<ClassOrInterfaceType> secondMethodImplements = methodImplements(codeCloneResult.getMethodPair().getSecond());
            Set<String> commonInterfaceNames = getInterfacesInCommon(firstMethodImplements, secondMethodImplements);
            if (commonInterfaceNames.size() > 0) {
                ++afterAdviceCandidatesWithComminInterface;
                log.info("First Method Name:  " + codeCloneResult.getMethodPair().getFirst().getFullMethodName());
                log.info("Second Method Name: " + codeCloneResult.getMethodPair().getSecond().getFullMethodName());
                log.info("With common interface(s) found: " + commonInterfaceNames);
                log.info("**********************************************************************************");
            }
        }
        log.info("**********************************************************************************");
        log.info("The number of After Advice Candidates with common interface was: " + afterAdviceCandidatesWithComminInterface);
        log.info("**********************************************************************************");
    }

    private void displayAroundAdviceCandidatesAsInterfaceCandidates(CodeCloneMiningResult codeCloneMiningResult) {
        int aroundAdviceCandidatesWithCommonInterface = 0;

        log.info("**********************************************************************************");
        log.info("The Around Advice Count was: " + codeCloneMiningResult.getAroundAdviceCandidates().size());
        log.info("**********************************************************************************");
        List<CodeCloneResult> aroundAdviceCandidates = codeCloneMiningResult.getAroundAdviceCandidates();
        for (CodeCloneResult codeCloneResult : aroundAdviceCandidates) {
            NodeList<ClassOrInterfaceType> firstMethodImplements = methodImplements(codeCloneResult.getMethodPair().getFirst());
            NodeList<ClassOrInterfaceType> secondMethodImplements = methodImplements(codeCloneResult.getMethodPair().getSecond());
            Set<String> commonInterfaceNames = getInterfacesInCommon(firstMethodImplements, secondMethodImplements);
            if (commonInterfaceNames.size() > 0) {
                ++aroundAdviceCandidatesWithCommonInterface;
                log.info("First Method Name:  " + codeCloneResult.getMethodPair().getFirst().getFullMethodName());
                log.info("Second Method Name: " + codeCloneResult.getMethodPair().getSecond().getFullMethodName());
                log.info("With common interface(s) found: " + commonInterfaceNames);
                log.info("**********************************************************************************");
            }
        }
        log.info("**********************************************************************************");
        log.info("The number of After Advice Candidates with common interface was: " + aroundAdviceCandidatesWithCommonInterface);
        log.info("**********************************************************************************");
    }

    private Set<String> getInterfacesInCommon(NodeList<ClassOrInterfaceType> firstMethodImplements,
                                              NodeList<ClassOrInterfaceType> secondMethodImplements) {
        Set<String> commonInterfaceNames = new HashSet<String>();
        for (ClassOrInterfaceType firstInterfaceType : firstMethodImplements) {
            String firstInterfaceName = firstInterfaceType.getName();
            for (ClassOrInterfaceType secondInterfaceType : secondMethodImplements) {
                String secondInterfaceName = firstInterfaceType.getName();
                if (secondInterfaceName.equals(firstInterfaceName)) {
                    commonInterfaceNames.add(firstInterfaceType.getName());
                }
            }
        }
        return commonInterfaceNames;
    }

    private NodeList<ClassOrInterfaceType>  methodImplements(MethodRepresentation firstMethodRepresentation) {
        NodeList<ClassOrInterfaceType> methodImplements = null;
        CompilationUnitWrapper compilationUnitWrapper = new CompilationUnitWrapper(firstMethodRepresentation.getFilePath());
        try {
            CompilationUnit compilationUnit = compilationUnitWrapper.getCompilationUnit(firstMethodRepresentation.getFilePath());
            NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
            for (TypeDeclaration type : types) {
                if (type instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration) type).isInterface()) {
                    methodImplements = ((ClassOrInterfaceDeclaration) type).getImplements();
                }
            }
        } catch (IOException e) {
            throw new AspectCloneException("An error when attempting to extract interfaces for method", e);
        }
        return methodImplements;
    }
}
