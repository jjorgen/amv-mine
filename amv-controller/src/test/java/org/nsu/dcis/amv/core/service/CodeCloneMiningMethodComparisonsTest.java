package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsu.dcis.amv.core.domain.CodeCloneMiningResult;
import org.nsu.dcis.amv.core.instrumentation.AmvConfigurationInstrumentation;
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
        List<CodeCloneMiningResult> codeCloneMiningResults =
                codeCloneMiningService.getCodeCloneMiningResults(ROOT_DIRECTORY, new ArrayList<>(), amvConfigurationInstrumentation.getFileExtensions());
        assertTrue(codeCloneMiningResults != null);
        log.info(codeCloneMiningResults.size());
        displayCloneStatistics(codeCloneMiningResults);
    }

    @Test
    public void getCodeCloningResultsForAllClasses() throws Exception {
        List<CodeCloneMiningResult> codeCloneMiningResults =
                codeCloneMiningService.getCodeCloneMiningResults(amvConfigurationInstrumentation.getRootDir(),
                                                                 amvConfigurationInstrumentation.getExcludedDirectoryList(),
                                                                 amvConfigurationInstrumentation.getFileExtensions());
        assertTrue(codeCloneMiningResults != null);
        log.info(codeCloneMiningResults.size());
        displayCloneStatistics(codeCloneMiningResults);
    }
    private void displayCloneStatistics(List<CodeCloneMiningResult> codeCloneMiningResults) {
        int emptyCount = 0;
        int cloneCount = 0;
        int beforeAdvice = 0;
        int aroundAdvice = 0;
        int afterAdvice = 0;
        for (CodeCloneMiningResult codeCloneMiningResult : codeCloneMiningResults) {
            switch (codeCloneMiningResult.getType()) {
                case EMPTY:
                    emptyCount++;
                    break;
                case CLONE:
                    cloneCount++;
                    inspectCloneResult(codeCloneMiningResult);
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
    }

    private void inspectCloneResult(CodeCloneMiningResult codeCloneMiningResult) {
        log.info("---------------------------------------------------------------------------");
        MethodRepresentation compareFrom = codeCloneMiningResult.getMethodPair().getFirst();
        log.info("From Method Name: " + compareFrom.getFullMethodName());
        log.info("Clone: compareFrom: " + compareFrom.getStringifiedWithoutComments());
        MethodRepresentation compareTo = codeCloneMiningResult.getMethodPair().getSecond();
        log.info("To Method Name:   " + compareTo.getFullMethodName());
        log.info("Clone: compareTo: " + compareTo.getStringifiedWithoutComments());
        log.info("***************************************************************************");
    }
}
