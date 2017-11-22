package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.domain.CodeCloneMiningResult;
import org.nsu.dcis.amv.core.domain.FileScanResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by John Jorgensen on 3/12/2017.
 */
@Service
public class CodeCloneMiningService {

    @Autowired
    FileScanningService fileScanningService;

    @Autowired
    MethodRepresentationService methodRepresentationService;

    private Logger log = Logger.getLogger(getClass().getName());


    public List<CodeCloneMiningResult> getCodeCloneMiningResults(String rootDir, List<String> excludedDirectoryList, Set<String> fileExtensions) {
        FileScanResult fileScanResult = fileScanningService.scan(rootDir, excludedDirectoryList, fileExtensions);
        List<MethodRepresentation> methodRepresentations = methodRepresentationService.getMethodRepresentations(fileScanResult);
        List<CodeCloneMiningResult> codeCloneMiningResults = new ArrayList<CodeCloneMiningResult>();
        for (int i = 0; i < methodRepresentations.size(); i++) {
            for (int j = i + 1; j < methodRepresentations.size(); j++) {
                CodeCloneMiningResult codeCloneMiningResult = getCodeCloneMiningResult(methodRepresentations.get(i), methodRepresentations.get(j));
                codeCloneMiningResults.add(codeCloneMiningResult);
            }
        }
        return codeCloneMiningResults;
    }

    private CodeCloneMiningResult getCodeCloneMiningResult(MethodRepresentation compareFrom, MethodRepresentation compareTo) {
        if (compareFrom.getClassName().equals(compareTo.getClassName()) && compareFrom.getMethodName().equals(compareTo.getMethodName())) {
            return new CodeCloneMiningResult();
        }

        if (signaturesAreEqual(compareFrom, compareTo)) {
            return compareMethodBodies(compareFrom, compareTo);
        }
        return new CodeCloneMiningResult();
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

    public CodeCloneMiningResult compareMethodBodies(MethodRepresentation compareFrom, MethodRepresentation compareTo) {
        if (compareFrom.getFullMethodName().equals(compareTo.getFullMethodName())) {
            log.info("EQUALS EQUALS EQUALS EQUALS EQUALS ");
        }

        int fromTopOfMethodLineCount = 0;
        int fromBottomOfMethodLineCount = 0;
        fromTopOfMethodLineCount = getFromTopOfMethodLineCount(compareFrom, compareTo);
        fromBottomOfMethodLineCount = getFromBottomOfMethodLineCount(compareFrom, compareTo);
        if (fromTopOfMethodLineCount > 0 || fromBottomOfMethodLineCount > 0) {
            return new CodeCloneMiningResult(compareFrom, compareTo, fromTopOfMethodLineCount, fromBottomOfMethodLineCount);
        }
        else {
            return new CodeCloneMiningResult();
        }
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
        for (int i = minBodySize -1; i > -1; i--) {
            if (compareFrom.getMethodTokens().get(fromLineIdx--).equals(compareTo.getMethodTokens().get(toLineIdx--))) {
                fromBottomOfMethodLineCount++;
            } else {
                break;
            }
        }
        return fromBottomOfMethodLineCount;
    }

//    private CodeCloneMiningResult getCodeCloneMiningResult(MethodRepresentation compareFrom, MethodRepresentation compareTo) {
//        return new CodeCloneMiningResult();
//    }
}


//        CheckCodeClone checkCodeClone = (compareFrom, compareTo) -> {
//            if (signaturesAreEqual(compareFrom, compareTo)) {
//                if (compareFrom.getClassName().equals(compareTo.getClassName())) {
//                    log.info("CLASS IS THE SAME: " + compareFrom.getClassName());
//                    log.info("FROM METHOD NAME: " + compareFrom.getMethodName());
//                    log.info("TO METHOD NAME:   " + compareTo.getMethodName());
//                    if (compareFrom.getMethodName().equals(compareTo.getMethodName())) {
//                        log.info("METHOD IS THE SAME: " + compareFrom.getMethodName());
//                        log.info("FULL METHOD IS THE SAME: " + compareFrom.getFullMethodName());
//                    }
//                }
//                return compareMethodBodies(compareFrom, compareTo);
//            }
//            return new CodeCloneMiningResult();
//        };
