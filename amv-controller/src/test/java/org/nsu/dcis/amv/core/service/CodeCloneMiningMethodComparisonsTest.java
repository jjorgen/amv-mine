package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsu.dcis.amv.core.domain.CodeCloneResult;
import org.nsu.dcis.amv.core.instrumentation.AmvConfigurationInstrumentation;
import org.nsu.dcis.amv.core.util.CodeCloneMiningResult;
import org.nsu.dcis.amv.core.util.CodeCloneStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by John Jorgensen on 3/26/2017.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CodeCloneMiningMethodComparisonsTest {

    @Autowired
    AmvConfigurationInstrumentation amvConfigurationInstrumentation;

    @Autowired
    private CodeCloneMiningService codeCloneMiningService;

    private static final String ROOT_DIRECTORY = "C:/WS/amvCore/src/test/java/org/nsu/dcis/amv/core/support";
    private Logger log = Logger.getLogger(getClass().getName());

    @Test(expected = IllegalArgumentException.class)
    public void expectingExceptionWhenExcludedDirectoryListIsNull() throws Exception {
        codeCloneMiningService.getCodeCloneMiningResults(ROOT_DIRECTORY, null, amvConfigurationInstrumentation.getFileExtensions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void expectingExceptionWhenRootDirectoryIsNull() throws Exception {
        codeCloneMiningService.getCodeCloneMiningResults(null, new ArrayList<String>(), amvConfigurationInstrumentation.getFileExtensions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void expectingExceptionWhenRootDirectoryIsBlank() throws Exception {
        codeCloneMiningService.getCodeCloneMiningResults(" ", new ArrayList<String>(), amvConfigurationInstrumentation.getFileExtensions());
    }

    @Test
    public void getCodeCloningResultsForClassesUnderRootDirectory() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
                codeCloneMiningService.getCodeCloneMiningResults(ROOT_DIRECTORY, new ArrayList<>(), amvConfigurationInstrumentation.getFileExtensions());
        displayCloneStatistics(codeCloneMiningResult.getCodeCloneResults());
    }

    @Test
    public void getCodeCloningResultsForBeforeAdviceAspect() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
            codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                    amvConfigurationInstrumentation.getExcludedDirectoryList(),
                    amvConfigurationInstrumentation.getFileExtensions());

        displayCloneStatistics(codeCloneMiningResult.getCodeCloneResults());
    }

    private void displayCloneStatisticsForBeforeAdviceAspect(List<CodeCloneResult> codeCloneResults) {
        int aspectIdx = 0;
        for (CodeCloneResult cloneMiningResult : codeCloneResults) {
            if (cloneMiningResult.isBeforeAdviceCandidate()) {
                log.info("*************************************************************");
                log.info("*********** Aspect Number: '" + (++aspectIdx) +"' ***********");
                log.info("*************************************************************");
                inspectCloneResult(cloneMiningResult);
                if (aspectIdx == 1197) {
                    System.out.println("Aspect Number: '1197'");
                    break;
                }
            }
        }
    }

    @Test
    public void getCodeCloningResultsForAllClasses() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
                codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                        amvConfigurationInstrumentation.getExcludedDirectoryList(),
                        amvConfigurationInstrumentation.getFileExtensions());
        displayCodeCloneResults(codeCloneMiningResult);
        displayBeforeAdviceCandidates(codeCloneMiningResult);

    }

    private void displayBeforeAdviceCandidates(CodeCloneMiningResult codeCloneMiningResult) {
        List<CodeCloneResult> beforeAdviceCandidates = codeCloneMiningResult.getBeforeAdviceCandidates();
        for (CodeCloneResult codeCloneResult : beforeAdviceCandidates) {
            log.info("**********************************************************************************");
            codeCloneResult.display();
            log.info("**********************************************************************************");
        }
    }

    private void displayCodeCloneResults(CodeCloneMiningResult codeCloneMiningResult) {
        CodeCloneStatistics codeCloneStatistics = codeCloneMiningResult.getCodeCloneStatistics();
        log.info("Before Advice Count: " + codeCloneStatistics.getBeforeAdviceCount());
        log.info("After Advice Count: " + codeCloneStatistics.getAfterAdviceCount());
        log.info("Around Advice Count: " + codeCloneStatistics.getAroundAdviceCount());
        log.info("Clone Count: " + codeCloneStatistics.getCloneCount());
        log.info("Empty Count: " + codeCloneStatistics.getEmptyCount());
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

//        log.info("Empty:  " + emptyCount);
//        log.info("Clone:  " + cloneCount);
//        log.info("Before: " + beforeAdvice);
//        log.info("Around: " + aroundAdvice);
//        log.info("After:  " + afterAdvice);
        return new CodeCloneStatistics(emptyCount, cloneCount, beforeAdvice, aroundAdvice, afterAdvice);
    }

    private void inspectCloneResult(CodeCloneResult codeCloneResult) {
        log.info("---------------------------------------------------------------------------");
        MethodRepresentation compareFrom = codeCloneResult.getMethodPair().getFirst();
        log.info("\nFrom Method Name: " + compareFrom.getFullMethodName());
        log.info("\nClone: compareFrom: " + compareFrom.getStringifiedWithoutComments());
        MethodRepresentation compareTo = codeCloneResult.getMethodPair().getSecond();
        log.info("\nTo Method Name:   " + compareTo.getFullMethodName());
        log.info("\nClone: compareTo: " + compareTo.getStringifiedWithoutComments());
        log.info("***************************************************************************");
    }
}
