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

    // When displaying the results the following should show.
    //    INFO [2018-01-23 09:12:31,942] [main] [CodeCloneMiningMethodComparisonsTest.java:168] - Empty:  3755091
    //    INFO [2018-01-23 09:12:31,942] [main] [CodeCloneMiningMethodComparisonsTest.java:169] - Clone:  33
    //    INFO [2018-01-23 09:12:31,942] [main] [CodeCloneMiningMethodComparisonsTest.java:170] - Before: 40
    //    INFO [2018-01-23 09:12:31,942] [main] [CodeCloneMiningMethodComparisonsTest.java:171] - Around: 1
    //    INFO [2018-01-23 09:12:31,942] [main] [CodeCloneMiningMethodComparisonsTest.java:172] - After:  5
    @Test
    public void getCodeCloningResultsForAllClasses() throws Exception {
        CodeCloneMiningResult codeCloneMiningResult =
                codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                        amvConfigurationInstrumentation.getExcludedDirectoryList(),
                        amvConfigurationInstrumentation.getFileExtensions());

//        displayCodeCloneResults(codeCloneMiningResult);
        CodeCloneStatistics codeCloneStatistics = codeCloneMiningResult.getCodeCloneStatistics();
        log.info("Code Clone Statistics: " + codeCloneStatistics);
        displayBeforeAdviceCandidates(codeCloneMiningResult);
        displayAfterCandidates(codeCloneMiningResult);
        displayAroundAdviceCandidates(codeCloneMiningResult);
        displayClones(codeCloneMiningResult);
        // Displays the results
        displayCloneStatistics(codeCloneMiningResult.getCodeCloneResults());
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

    private void displayBeforeAdviceCandidates(CodeCloneMiningResult codeCloneMiningResult) {
        List<CodeCloneResult> beforeAdviceCandidates = codeCloneMiningResult.getBeforeAdviceCandidates();
        log.info("**********************************************************************************");
        log.info("The Before Advice Candidate Count was: " + codeCloneMiningResult.getBeforeAdviceCandidates().size());
        log.info("**********************************************************************************");
        for (CodeCloneResult codeCloneResult : beforeAdviceCandidates) {
            log.info("**********************************************************************************");
            codeCloneResult.display();
            log.info("**********************************************************************************");
        }
    }

    private void displayAfterCandidates(CodeCloneMiningResult codeCloneMiningResult) {
        List<CodeCloneResult> afterAdviceCandidates = codeCloneMiningResult.getAfterAdviceCandidates();
        log.info("**********************************************************************************");
        log.info("The After Advice Candidate was: " + codeCloneMiningResult.getAfterAdviceCandidates().size());
        log.info("**********************************************************************************");
        for (CodeCloneResult codeCloneResult : afterAdviceCandidates) {
            log.info("**********************************************************************************");
            codeCloneResult.display();
            log.info("**********************************************************************************");
        }
    }

    private void displayAroundAdviceCandidates(CodeCloneMiningResult codeCloneMiningResult) {
        log.info("**********************************************************************************");
        log.info("The Around Advice Count was: " + codeCloneMiningResult.getAroundAdviceCandidates().size());
        log.info("**********************************************************************************");
        List<CodeCloneResult> aroundAdviceCandidates = codeCloneMiningResult.getAroundAdviceCandidates();
        for (CodeCloneResult codeCloneResult : aroundAdviceCandidates) {
            log.info("**********************************************************************************");
            codeCloneResult.display();
            log.info("**********************************************************************************");
        }
    }

    private void displayCodeCloneResults(CodeCloneMiningResult codeCloneMiningResult) {
        List<CodeCloneResult> codeCloneResults = codeCloneMiningResult.getCodeCloneResults();
        for (CodeCloneResult codeCloneResult : codeCloneResults) {
            log.info("**********************************************************************************");
            codeCloneResult.display();
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
}
