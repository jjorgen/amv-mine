package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.extend.CompilationUnitWrapper;
import org.apache.log4j.Logger;
import org.nsu.dcis.amv.common.AspectMiningByCategory;
import org.nsu.dcis.amv.common.AspectMiningDetailResult;
import org.nsu.dcis.amv.common.CalledMethod;
import org.nsu.dcis.amv.core.domain.CodeCloneResult;
import org.nsu.dcis.amv.core.exception.AspectCloneException;
import org.nsu.dcis.amv.core.instrumentation.AmvConfigurationInstrumentation;
import org.nsu.dcis.amv.core.util.CodeCloneMiningResult;
import org.nsu.dcis.amv.core.util.CodeCloneStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jorgej2 on 5/21/2018.
 */
@Service
public class CrossCuttingConcernAsInterfaceService
{
    private Logger log = Logger.getLogger(getClass().getName());

    @Autowired
    AmvConfigurationInstrumentation amvConfigurationInstrumentation;

    @Autowired
    private CodeCloneMiningService codeCloneMiningService;
    private AspectMiningDetailResult beforeAdviceDetailResults;
    private AspectMiningDetailResult afterAdviceDetailResults;

    public AspectMiningDetailResult getAroundAdviceDetailResults() {
        CodeCloneMiningResult codeCloneMiningResult = getCodeCloneMiningResult();
        ArrayList<CodeCloneResult> codeCloneResults =
                getCommonInterfaceCodeCloneResults(codeCloneMiningResult.getAroundAdviceCandidates(), getAllInterfaces());

        return extractAdviceDetailResults(codeCloneResults, "Cross Cutting Concern As Interface Around Advice");
    }

    public AspectMiningDetailResult getBeforeAdviceDetailResults() {
        CodeCloneMiningResult codeCloneMiningResult = getCodeCloneMiningResult();
        ArrayList<CodeCloneResult> codeCloneResults =
                getCommonInterfaceCodeCloneResults(codeCloneMiningResult.getBeforeAdviceCandidates(), getAllInterfaces());

        return extractAdviceDetailResults(codeCloneResults, "Cross Cutting Concern As Interface Before Advice");
    }

    public AspectMiningDetailResult getAfterAdviceDetailResults() {
        CodeCloneMiningResult codeCloneMiningResult = getCodeCloneMiningResult();
        ArrayList<CodeCloneResult> codeCloneResults =
                getCommonInterfaceCodeCloneResults(codeCloneMiningResult.getAfterAdviceCandidates(), getAllInterfaces());

        return extractAdviceDetailResults(codeCloneResults, "Cross Cutting Concern As Interface After Advice");
    }

    private AspectMiningDetailResult extractAdviceDetailResults(ArrayList<CodeCloneResult> codeCloneResults, String adviceType) {
        AspectMiningDetailResult aspectMiningDetailResult = new AspectMiningDetailResult();
        aspectMiningDetailResult.setCrossCuttingConcernCategoryDisplayName(adviceType);
        aspectMiningDetailResult.setLeftSideHeading("Interface : First Duplicate Method");
        aspectMiningDetailResult.setRightSideHeading("Duplicated Methods Detail");
//        aspectMiningDetailResult.setCallingMethod("Calling Method");

        CalledMethod[] calledMethods = new CalledMethod[codeCloneResults.size() * 2];
        String[] callingMethods = new String[codeCloneResults.size()];
        for (int i = 0; i < codeCloneResults.size(); i++) {
//            if (i == 0) {
//                aspectMiningDetailResult.setCallingMethod(codeCloneResults.get(0).getCommonInterfaceName() +
//                " : " +  codeCloneResults.get(0).getMethodPair().getFirst().getMethodName());
//            }
//            callingMethods[i] = codeCloneResults.get(i).getMethodPair().getFirst().getMethodName();

            callingMethods[i] = codeCloneResults.get(i).getCommonInterfaceName() +
                                " : " +  codeCloneResults.get(i).getMethodPair().getFirst().getMethodName();

            CalledMethod calledMethod = new CalledMethod();
            calledMethod.setCalledMethodName(codeCloneResults.get(i).getMethodPair().getFirst().getFilePath() + " : " + codeCloneResults.get(i).getMethodPair().getFirst().getMethodName());
            calledMethod.setCalledMethodDetail(codeCloneResults.get(i).getMethodPair().getFirst().getStringifiedWithoutComments());
            calledMethods[2 * i] = calledMethod;

            calledMethod = new CalledMethod();
            calledMethod.setCalledMethodName(codeCloneResults.get(i).getMethodPair().getSecond().getFilePath() + " : " + codeCloneResults.get(i).getMethodPair().getSecond().getMethodName());
            calledMethod.setCalledMethodDetail(codeCloneResults.get(i).getMethodPair().getSecond().getStringifiedWithoutComments());
            calledMethods[2 * i + 1] = calledMethod;
        }
        aspectMiningDetailResult.setCallingMethods(callingMethods);
        aspectMiningDetailResult.setCalledMethod(calledMethods);

        return aspectMiningDetailResult;
    }

//    private AspectMiningDetailResult extractAdviceDetailResults(AspectMiningByCategory aroundAdviceCandidatesAsInterface) {
//        AspectMiningDetailResult aspectMiningDetailResult = new AspectMiningDetailResult();
//        aspectMiningDetailResult.setCrossCuttingConcernCategoryDisplayName("Cross Cutting Concern As Interface Around Advice");
//        aspectMiningDetailResult.setLeftSideHeading("First Duplicate Method");
//        aspectMiningDetailResult.setRightSideHeading("Duplicated Methods Detail");
//        aspectMiningDetailResult.setCallingMethod("Calling Method");
//
//        CalledMethod[] calledMethods = new CalledMethod[2];
//        for (int i = 0; i < calledMethods.length; i++) {
//            CalledMethod calledMethod = new CalledMethod();
//            calledMethod.setCalledMethodName(new String("Called Method Number " + i));
//            calledMethod.setCalledMethodDetail(new String("Called Method Detail " + i));
//            calledMethods[i] = calledMethod;
//        }
//        aspectMiningDetailResult.setCalledMethod(calledMethods);
//
//        return aspectMiningDetailResult;
//    }

    public List<AspectMiningByCategory> getCrossCuttingConcerns() {
        List<AspectMiningByCategory> aspectMiningByCategoryList = new ArrayList();

        log.info("Mining for cross cutting concern as interface");

        CodeCloneMiningResult codeCloneMiningResult = getCodeCloneMiningResult();

        CodeCloneStatistics codeCloneStatistics = codeCloneMiningResult.getCodeCloneStatistics();
        log.info("Code Clone Statistics: " + codeCloneStatistics);
        aspectMiningByCategoryList.add(getBeforeAdviceCrossCuttingConcernAsInterfaceCandidates(codeCloneMiningResult.getBeforeAdviceCandidates(), getAllInterfaces()));
        aspectMiningByCategoryList.add(getAfterAdviceCrossCuttingConcernAsInterfaceCandidates(codeCloneMiningResult.getAfterAdviceCandidates(), getAllInterfaces()));
        aspectMiningByCategoryList.add(getAroundAdviceCandidatesAsInterface(codeCloneMiningResult.getAroundAdviceCandidates(), getAllInterfaces()));
//        aspectMiningByCategoryList.add(getClonesAsInterface(codeCloneMiningResult));
        log.info("Returning aspectMiningByCategoryList: " + aspectMiningByCategoryList);
        return aspectMiningByCategoryList;
    }

    public CodeCloneMiningResult getCodeCloneMiningResult() {
        return codeCloneMiningService.getCodeCloneMiningResults("C:/work/0_NSU/CH/ifa",
                amvConfigurationInstrumentation.getExcludedDirectoryList(),
                amvConfigurationInstrumentation.getFileExtensions());
    }

    public AspectMiningByCategory getBeforeAdviceCrossCuttingConcernAsInterfaceCandidates(
           List<CodeCloneResult> beforeAdviceCandidates, List<CompilationUnitWrapper> allInterfaces) {
        int beforeAdviceCandidatesWithComonInterface = getCommonInterfaceCount(beforeAdviceCandidates, allInterfaces);
        return new AspectMiningByCategory("CrossCuttingConcernAsInterface_BeforeAdvice",
                "Cross Cutting Concern As Interface Before Advice",
                0,beforeAdviceCandidatesWithComonInterface,0);
    }

    public AspectMiningByCategory getAfterAdviceCrossCuttingConcernAsInterfaceCandidates(
            List<CodeCloneResult> beforeAdviceCandidates, List<CompilationUnitWrapper> allInterfaces) {
        int afterAdviceCandidatesWithCommonInterface = getCommonInterfaceCount(beforeAdviceCandidates, allInterfaces);
        return new AspectMiningByCategory("CrossCuttingConcernAsInterface_AfterAdvice",
                "Cross Cutting Concern As Interface After Advice",
                0,afterAdviceCandidatesWithCommonInterface,0);
    }

    public AspectMiningByCategory getAroundAdviceCandidatesAsInterface(
            List<CodeCloneResult> aroundAdviceCandidates, List<CompilationUnitWrapper> allInterfaces) {
        int aroundAdviceCandidatesWithCommonInterface = getCommonInterfaceCount(aroundAdviceCandidates, allInterfaces);
        return new AspectMiningByCategory("CrossCuttingConcernAsInterface_AroundAdvice",
                "Cross Cutting Concern As Interface Around Advice",
                0, aroundAdviceCandidatesWithCommonInterface,0);
    }

    public ArrayList<CodeCloneResult> getCommonInterfaceCodeCloneResults(List<CodeCloneResult> adviceCandidates,
                                                                          List<CompilationUnitWrapper> allInterfaces) {
        ArrayList<CodeCloneResult> codeCloneResults = new  ArrayList<>();

        for (CodeCloneResult codeCloneResult : adviceCandidates) {
            Set<String> commonInterfaceNames = getInterfacesInCommon(
                    enclosingClassImplements(codeCloneResult.getMethodPair().getFirst()),
                    enclosingClassImplements(codeCloneResult.getMethodPair().getSecond()));

            CompilationUnitWrapper implementingInterface =
                    getImplementingInterface(codeCloneResult.getMethodPair().getFirst().getMethodName(),
                            codeCloneResult.getMethodPair().getSecond().getMethodName(),
                            getImplementingInterfaceCandidates(commonInterfaceNames, allInterfaces, codeCloneResult.getMethodPair().getFirst().getMethodName()));

            if (implementingInterface != null) {
                log.info("Common Interface Name: " + implementingInterface.getClassOrInterfaceName());
                log.info("Method implemented by interface: " + codeCloneResult.getMethodPair().getFirst().getMethodName());
                codeCloneResult.setCommonInterfaceName(implementingInterface.getClassOrInterfaceName());
                codeCloneResults.add(codeCloneResult);
            }
        }
        return codeCloneResults;
    }

//    public AspectMiningByCategory getClonesAsInterface(
//        List<CodeCloneResult> cloneCandidates, List<CompilationUnitWrapper> allInterfaces) {
//            int beforeAdviceCandidatesWithCommonInterface = getCommonInterfaceCount(cloneCandidates, allInterfaces);
//            return new AspectMiningByCategory("Code Clone As Interface, After Advice",
//                    beforeAdviceCandidatesWithCommonInterface,0,0);
//    }

//    public AspectMiningByCategory getClonesAsInterface(CodeCloneMiningResult codeCloneMiningResult) {
//        int codeCloneWithComminInterface = 0;
//        List<CodeCloneResult> codeClones = codeCloneMiningResult.getClone();
//        log.info("**********************************************************************************");
//        log.info("The Clone Count was: " + codeCloneMiningResult.getClone().size());
//        log.info("**********************************************************************************");
//        for (CodeCloneResult CodeCloneResult : codeClones) {
//            NodeList<ClassOrInterfaceType> firstMethodImplements = enclosingClassImplements(CodeCloneResult.getMethodPair().getFirst());
//            NodeList<ClassOrInterfaceType> secondMethodImplements = enclosingClassImplements(CodeCloneResult.getMethodPair().getSecond());
//            Set<String> commonInterfaceNames = getInterfacesInCommon(firstMethodImplements, secondMethodImplements);
//            if (commonInterfaceNames.size() > 0) {
//                ++codeCloneWithComminInterface;
//                log.info("First Method Name:  " + CodeCloneResult.getMethodPair().getFirst().getFullMethodName());
//                log.info("Second Method Name: " + CodeCloneResult.getMethodPair().getSecond().getFullMethodName());
//                log.info("With common interface(s) found: " + commonInterfaceNames);
//                log.info("**********************************************************************************");
//            }
//        }
//        log.info("**********************************************************************************");
//        log.info("The number of Code Clones with common interface was: " + codeCloneWithComminInterface);
//        log.info("**********************************************************************************");
//        return new AspectMiningByCategory("Cross Cutting Concern As Interface, Clone",
//                codeCloneWithComminInterface,0,0);
//    }


//        log.info("**********************************************************************************");
//        log.info("The Around Advice Count was: " + codeCloneMiningResult.getAroundAdviceDetailResults().size());
//        log.info("**********************************************************************************");
//        List<CodeCloneResult> aroundAdviceCandidates = codeCloneMiningResult.getAroundAdviceDetailResults();
//        for (CodeCloneResult codeCloneResult : aroundAdviceCandidates) {
//            NodeList<ClassOrInterfaceType> firstMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getFirst());
//            NodeList<ClassOrInterfaceType> secondMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getSecond());
//            Set<String> commonInterfaceNames = getInterfacesInCommon(firstMethodImplements, secondMethodImplements);
//            if (commonInterfaceNames.size() > 0) {
//                log.info("First Method Name:  " + codeCloneResult.getMethodPair().getFirst().getFullMethodName());
//                log.info("Second Method Name: " + codeCloneResult.getMethodPair().getSecond().getFullMethodName());
//                log.info("With common interface(s) found: " + commonInterfaceNames);
//                log.info("**********************************************************************************");
//            }
//        }
//        log.info("**********************************************************************************");
//        log.info("The number of After Advice Candidates with common interface was: " + aroundAdviceCandidatesWithCommonInterface);
//        log.info("**********************************************************************************");
//        return new AspectMiningByCategory("Cross Cutting Concern As Interface, Around Advice",
//                aroundAdviceCandidatesWithCommonInterface,0,0);
//

//
//        int afterAdviceCandidatesWithComminInterface = 0;
//        List<CodeCloneResult> afterAdviceCandidates = codeCloneMiningResult.getAfterAdviceCandidates();
//        log.info("**********************************************************************************");
//        log.info("The After Advice Candidate Count was: " + codeCloneMiningResult.getAfterAdviceCandidates().size());
//        log.info("**********************************************************************************");
//
////        int beforeAdviceCandidatesWithComonInterface = getCommonInterfaceCount(beforeAdviceCandidates, allInterfaces);
////        return new AspectMiningByCategory("Cross Cutting Concern As Interface, Before Advice",
////                beforeAdviceCandidatesWithComonInterface,0,0);
//
//
//        for (CodeCloneResult codeCloneResult : afterAdviceCandidates) {
//            NodeList<ClassOrInterfaceType> firstMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getFirst());
//            NodeList<ClassOrInterfaceType> secondMethodImplements = enclosingClassImplements(codeCloneResult.getMethodPair().getSecond());
//            Set<String> commonInterfaceNames = getInterfacesInCommon(firstMethodImplements, secondMethodImplements);
//            if (commonInterfaceNames.size() > 0) {
//                ++afterAdviceCandidatesWithComminInterface;
//                log.info("First Method Name:  " + codeCloneResult.getMethodPair().getFirst().getFullMethodName());
//                log.info("Second Method Name: " + codeCloneResult.getMethodPair().getSecond().getFullMethodName());
//                log.info("With common interface(s) found: " + commonInterfaceNames);
//                log.info("**********************************************************************************");
//            }
//        }
//        log.info("**********************************************************************************");
//        log.info("The number of After Advice Candidates with common interface was: " + afterAdviceCandidatesWithComminInterface);
//        log.info("**********************************************************************************");
//        return new AspectMiningByCategory("Cross Cutting Concern As Interface, After Advice",
//                afterAdviceCandidatesWithComminInterface,0,0);

    private int getCommonInterfaceCount(List<CodeCloneResult> adviceCandidates,
                                        List<CompilationUnitWrapper> allInterfaces) {
        int adviceCandidatesWithCommonInterface = 0;
        for (CodeCloneResult codeCloneResult : adviceCandidates) {
            Set<String> commonInterfaceNames = getInterfacesInCommon(
                enclosingClassImplements(codeCloneResult.getMethodPair().getFirst()),
                enclosingClassImplements(codeCloneResult.getMethodPair().getSecond()));

            CompilationUnitWrapper implementingInterface =
                getImplementingInterface(codeCloneResult.getMethodPair().getFirst().getMethodName(),
                codeCloneResult.getMethodPair().getSecond().getMethodName(),
                getImplementingInterfaceCandidates(commonInterfaceNames, allInterfaces, codeCloneResult.getMethodPair().getFirst().getMethodName()));

            if (implementingInterface != null) {
                log.info("Common Interface Name: " + implementingInterface.getClassOrInterfaceName());
                log.info("Method implemented by interface: " + codeCloneResult.getMethodPair().getFirst().getMethodName());
                ++adviceCandidatesWithCommonInterface;
            }
        }
        return adviceCandidatesWithCommonInterface;
    }

    private List getImplementingInterfaceCandidates(Set<String> commonInterfaceNames, List<CompilationUnitWrapper> allInterfaces, String methodName) {
        List implementingInterfaceCandidates = new ArrayList<CompilationUnitWrapper>();
        for (String commonInterfaceName : commonInterfaceNames) {
            for (CompilationUnitWrapper  compilationUnitWrapper : allInterfaces) {
                if (commonInterfaceName.equals(compilationUnitWrapper.getClassOrInterfaceName())) {
                    log.info("Class Or Interface Name: " + compilationUnitWrapper.getClassOrInterfaceName());
                    log.info("Common Interface Name:   " + commonInterfaceName);
                    if (compilationUnitWrapper.getMethodNames().contains(methodName)) {
                        log.info("*************************************************************");
                        log.info("Common Interface Name: " + compilationUnitWrapper.getMethodNames());
                        log.info("Compilation Unit Method Names: " + methodName);
                        log.info("Method Name: " + methodName);
                        implementingInterfaceCandidates.add(compilationUnitWrapper);
                    }
                }
            }
        }
        return implementingInterfaceCandidates;
    }

    private CompilationUnitWrapper getImplementingInterface(String firstMethod, String secondMethod, List<CompilationUnitWrapper> implementingInterfaceCandidates) {
        for (CompilationUnitWrapper compilationUnitWrapper : implementingInterfaceCandidates) {
            ArrayList<String> methodNames = compilationUnitWrapper.getMethodNames();
            if (methodNames.contains(firstMethod) && methodNames.contains(secondMethod)) {
                return compilationUnitWrapper;
            }
        }
        return null;
    }

    private String getImplementingInterface(String firstMethodName, String secondMethodName1, Set<String> commonInterfaceNames, List<CompilationUnitWrapper> allInterfaces) {
        return firstMethodName;
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

    public List<CompilationUnitWrapper> getAllInterfaces() {
        List<CompilationUnitWrapper> allInterfaces = codeCloneMiningService.getAllInterfaces(
                "C:/work/0_NSU/CH/ifa",
                amvConfigurationInstrumentation.getExcludedDirectoryList(),
                amvConfigurationInstrumentation.getFileExtensions());
        return allInterfaces;
    }
}
