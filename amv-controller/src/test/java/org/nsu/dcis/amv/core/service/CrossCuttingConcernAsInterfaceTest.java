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

    /**
     * Developer Note:
     *   Conclusions and further work. The code in this test suite demonstrates:
     *   (1) How it is possible to get interfaces for a class which that contains a method.
     *   (2) That it is possible to get interface that the current interface is extending.
     *   (3) That it is possible to get the methods that an interface implements.
     *   (4) That insufficient information is enclosed in the AST so tht it is not possible
     *       to get the interface that an interface extends and then get the method names from
     *       the extended interface directly. This because the full path of the extended interface
     *       is not available.
     *   (5) In order to work around this the following can be done when building the AST:
     *       (5.1) Create a separate collection of all the CompilationUnitWrapper classes which
     *             are instantiated from interfaces.
     *       (5.2) Search this collection by interface name when trying to find the methods that
     *             is present in an ancestor interface. The CompilationUnitWrapper will have this
     *             information since it was created from the full path of the interface.
     *       (5.3) Also, (5.2) is best implemented as a recursive method since many interfaces may
     *             be present, each with their own inheritance hierarchy.
     *
     * @throws Exception
     */



    // Developer Note:
    // The following algorithm will be used when looking for implementing interface for a method.
    //
    //    Algorithm that will be used to locate interface that implement method(s)
    //            --------------------------------------------------------------------------------------------------------
    //            for each interface look for match of implementing method in the interface
    //
    //	1. after a particular interface has been searched the look for ancester interface if this exists
    //	2. search the ancester interface for the method
    //	3. repeat (2) as long as there are ancester interfaces to search
    //
    //    stop when an interface that implements the method has been found
    //	if the interface can not be instantiated with CompilationUnit as wiil be the case with EventListener then stop
    //    Concusion: The result is inconclusive.
    //
    //    Also consider
    //	-------------
    //            if the methods are not the same then for the second method perform the same analysis
    //	if an implementing interface is not found then stop -> not to be included in the set
    //
    //	if an implementing interface is found then
    //            check if the two interfaces for the two methods are the same.


    @Test
    public void getImplementingInterfaceForMethodIncludingAncestorInterface() throws Exception {
        String matchingMethodName = "getFont";
        boolean methodWasFound = false;

        CompilationUnitWrapper compilationUnitWrapper = new CompilationUnitWrapper("C:/work/0_NSU/CH/ifa/draw/contrib/html/AttributeContentProducerContext.java");
        CompilationUnit compilationUnit = compilationUnitWrapper.getCompilationUnit("C:/work/0_NSU/CH/ifa/draw/contrib/html/AttributeContentProducerContext.java");

        List<MethodRepresentation> methodRepresentations = compilationUnitWrapper.getMethodRepresentations();
        for (MethodRepresentation methodRepresentation : methodRepresentations) {
            log.info("Interface method name: " + methodRepresentation.getMethodName());
            if (matchingMethodName.equals(methodRepresentation.getMethodName())) {
                methodWasFound = true;
                log.info("Method '" + methodRepresentation.getMethodName() + "' was found");
                break;
            }
            if (!methodWasFound) {
                log.info("Method '" + methodRepresentation.getMethodName() + "' was not found");
            }
        }

        if (!methodWasFound) {
            NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
            for (TypeDeclaration type : types) {
                if (type instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) type).isInterface()) {
                    NodeList<ClassOrInterfaceType> extendingInterfaces = ((ClassOrInterfaceDeclaration) type).getExtends();
                    for (ClassOrInterfaceType extendingInterface : extendingInterfaces) {
                        System.out.println("The extending interface is: " + extendingInterface);

//                        NodeList<TypeDeclaration<?>> types = extendingInterface.getTypes();
//                        for (TypeDeclaration type : types) {
//                            if (type instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration) type).isInterface()) {
//                                methodImplements = ((ClassOrInterfaceDeclaration) type).getImplements();
//                            }
//                        }
//
//
//
//                        methodImplements = extendingInterface.getImplements();
//
                    }
                }
            }
        }
    }

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
            NodeList<ClassOrInterfaceType> firstMethodImplements = enclosingClassImplements(CodeCloneResult.getMethodPair().getFirst());
            NodeList<ClassOrInterfaceType> secondMethodImplements = enclosingClassImplements(CodeCloneResult.getMethodPair().getSecond());
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
            NodeList<ClassOrInterfaceType> firstMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getFirst());
            NodeList<ClassOrInterfaceType> secondMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getSecond());
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
            NodeList<ClassOrInterfaceType> firstMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getFirst());
            NodeList<ClassOrInterfaceType> secondMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getSecond());
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
            NodeList<ClassOrInterfaceType> firstMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getFirst());
            NodeList<ClassOrInterfaceType> secondMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getSecond());
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
                String secondInterfaceName = secondInterfaceType.getName();
                if (secondInterfaceName.equals(firstInterfaceName)) {
                    commonInterfaceNames.add(firstInterfaceType.getName());
                }
            }
        }
        return commonInterfaceNames;
    }

    private NodeList<ClassOrInterfaceType> enclosingClassImplements(MethodRepresentation firstMethodRepresentation) {
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
