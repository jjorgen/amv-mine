package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.domain.CodeCloneResult;
import org.nsu.dcis.amv.core.domain.FileScanResult;
import org.nsu.dcis.amv.core.exception.AspectCloneException;
import org.nsu.dcis.amv.core.util.AmvConfiguration;
import org.nsu.dcis.amv.core.util.CodeCloneMiningResult;
import org.nsu.dcis.amv.core.util.CodeCloneStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.nsu.dcis.amv.core.domain.CodeCloneResult.BOTTOM_OF_METHOD_TOKEN_THRESHOLD;
import static org.nsu.dcis.amv.core.domain.CodeCloneResult.TOP_OF_METHOD_TOKEN_THRESHOLD;

/**
 * Created by John Jorgensen on 3/12/2017.
 */
@Service
public class CodeCloneMiningService {

    @Autowired
    private AmvConfiguration amvConfiguration;

    @Autowired
    FileScanningService fileScanningService;

    @Autowired
    MethodRepresentationService methodRepresentationService;

    private Logger log = Logger.getLogger(getClass().getName());

    public void mineForAspects() {
        getCodeCloneMiningResults(amvConfiguration.getJhotdrawSourceRoot(),
                amvConfiguration.getExcludedDirectoryList(),
                amvConfiguration.getFileExtensions());
    }

    public CodeCloneMiningResult getCodeCloneMiningResults(String rootDir, List<String> excludedDirectoryList, Set<String> fileExtensions) {
        FileScanResult fileScanResult = fileScanningService.scan(rootDir, excludedDirectoryList, fileExtensions);
        List<MethodRepresentation> methodRepresentations = methodRepresentationService.getMethodRepresentations(fileScanResult);
        List<CodeCloneResult> codeCloneResults = new ArrayList();
        for (int i = 0; i < methodRepresentations.size(); i++) {
            for (int j = i + 1; j < methodRepresentations.size(); j++) {
                CodeCloneResult codeCloneResult = getCodeCloneMiningResult(methodRepresentations.get(i), methodRepresentations.get(j));
//                if (!codeCloneResult.isSameMethods()) {
                    codeCloneResults.add(codeCloneResult);
//                }
            }
        }
        CodeCloneMiningResult codeCloneMiningResult = new CodeCloneMiningResult(getCodeCloneStatistics(codeCloneResults),
                                                                                codeCloneResults);
        return codeCloneMiningResult;
    }

    public List<MethodRepresentation> getAllMethodRepresentations(String rootDir, List<String> excludedDirectoryList, Set<String> fileExtensions) {
        log.info("fileScanResult");
        FileScanResult fileScanResult = fileScanningService.scan(rootDir, excludedDirectoryList, fileExtensions);
        log.info("after fileScanResult: " + fileScanResult.getFileList().size());
        return methodRepresentationService.getMethodRepresentations(fileScanResult);
    }


    private CodeCloneStatistics getCodeCloneStatistics(List<CodeCloneResult> codeCloneResults) {
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

        return new CodeCloneStatistics(emptyCount, cloneCount, beforeAdvice, aroundAdvice, afterAdvice);
    }

    private CodeCloneResult getCodeCloneMiningResult(MethodRepresentation compareFrom, MethodRepresentation compareTo) {
        if (compareFrom.getClassName().equals(compareTo.getClassName()) && compareFrom.getMethodName().equals(compareTo.getMethodName())) {
            return new CodeCloneResult();
        }

        if (signaturesAreEqual(compareFrom, compareTo)) {
            return compareMethodBodies(compareFrom, compareTo);
        }
        return new CodeCloneResult();
    }

    public boolean signaturesAreEqual(MethodRepresentation compareFrom, MethodRepresentation compareTo) {
        if (compareFrom.getParameters().size() != compareTo.getParameters().size()) return false;
        for (int i = 0; i < compareFrom.getParameters().size(); i++) {
            if (!compareFrom.getParameters().get(i).getType().equals(compareTo.getParameters().get(i).getType())) {
                return false;
            }
        }
        return true;
    }

    public CodeCloneResult compareMethodBodies(MethodRepresentation compareFrom, MethodRepresentation compareTo) {
        CodeCloneResult cloneMiningResult = null;
        if (compareFrom.getFullMethodName().equals(compareTo.getFullMethodName())) {
            throw new AspectCloneException("The first method is the same as the second method. This means that " +
                                           "the method is compared to itself. This should never happen.");
        }

        int fromTopOfMethodLineCount = getFromTopOfMethodLineCount(compareFrom, compareTo);
        int fromBottomOfMethodLineCount = getFromBottomOfMethodLineCount(compareFrom, compareTo);
        if (fromTopOfMethodLineCount > TOP_OF_METHOD_TOKEN_THRESHOLD || fromBottomOfMethodLineCount > BOTTOM_OF_METHOD_TOKEN_THRESHOLD) {
            cloneMiningResult = new CodeCloneResult(compareFrom, compareTo, fromTopOfMethodLineCount, fromBottomOfMethodLineCount);
        } else {
            cloneMiningResult = new CodeCloneResult();
        }
        return cloneMiningResult;
    }

    private int getFromTopOfMethodLineCount(MethodRepresentation compareFrom, MethodRepresentation compareTo) {
        int minBodySize = Math.min(compareFrom.getMethodTokens().size(), compareTo.getMethodTokens().size());
        int fromTopOfMethodLineCount = 0;
        for (int i = 0; i < minBodySize; i++) {
            if (compareFrom.getMethodTokens().get(i).equals(compareTo.getMethodTokens().get(i))) {
                fromTopOfMethodLineCount++;
            } else {
                break;
            }
        }
        return fromTopOfMethodLineCount;
    }

    private int getFromBottomOfMethodLineCount(MethodRepresentation compareFrom, MethodRepresentation compareTo) {
        int minBodySize = Math.min(compareFrom.getMethodTokens().size(), compareTo.getMethodTokens().size());
        int fromLineIdx = compareFrom.getMethodTokens().size() - 1;
        int toLineIdx = compareTo.getMethodTokens().size() - 1;

        int fromBottomOfMethodLineCount = 0;
        for (int i = minBodySize - 1; i > -1; i--) {
            if (compareFrom.getMethodTokens().get(fromLineIdx--).equals(compareTo.getMethodTokens().get(toLineIdx--))) {
                fromBottomOfMethodLineCount++;
            } else {
                break;
            }
        }
        return fromBottomOfMethodLineCount;
    }
}
