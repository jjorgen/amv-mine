package org.nsu.dcis.amv.core.controller;

import org.nsu.dcis.amv.common.AspectMiningByCategory;
import org.nsu.dcis.amv.common.AspectMiningRequest;
import org.nsu.dcis.amv.common.AspectMiningResult;
import org.nsu.dcis.amv.common.AspectMiningSummary;
import org.nsu.dcis.amv.core.domain.AspectMiningResults;
import org.nsu.dcis.amv.core.service.CallsAtTheBeginningOfMethodService;
import org.nsu.dcis.amv.core.service.CallsAtTheEndOfMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

@Controller
public class AmvClient {

    @Autowired
    CallsAtTheBeginningOfMethodService callsAtTheBeginningOfMethodService;

    @Autowired
    CallsAtTheEndOfMethodService callsAtTheEndOfMethodService;

    @Autowired
    ExecutionTraceController executionTraceController;

    @Autowired
    CodeCloneController codeCloneController;

    public static final String ASPECT_MINING_USE_CASE = "AspectMining";
    private static AmvClient amvClient = new AmvClient();
    private Logger log = Logger.getLogger(getClass().getName());
    private AspectMiningByCategory[] aspectMiningByCategory;

    public AspectMiningSummary getAspectMiningResults(AspectMiningRequest aspectMiningRequest) {
        AspectMiningSummary aspectMiningSummary = new AspectMiningSummary();
        AspectMiningResult aspectMiningResult = new AspectMiningResult();
        List<AspectMiningRequest.AspectMiningCategory> aspectMiningCategories = aspectMiningRequest.getAspectMiningCategories();
        log.info("Aspect Mining Categories Count: " + aspectMiningCategories.size());
        for (AspectMiningRequest.AspectMiningCategory aspectMiningCategory : aspectMiningCategories) {
            log.info("AspectMiningCategory: " + aspectMiningCategory);

            log.info("aspectMiningCategory.name() " + aspectMiningCategory.name());
            log.info("AspectMiningRequest.AspectMiningCategory.CALLS_AT_BEGINNING_AND_END_OF_METHODS.name(): " + AspectMiningRequest.AspectMiningCategory.CALLS_AT_BEGINNING_AND_END_OF_METHODS.name());
            if (aspectMiningCategory.name().equalsIgnoreCase(AspectMiningRequest.AspectMiningCategory.CALLS_AT_BEGINNING_AND_END_OF_METHODS.name())) {
                log.info("Calling: callsAtTheBeginningOfMethodService.commonCallsAtTheBeginningOfAMethod()");
                int numberOfCommonCallsAtTheBeginningOfAMethod = callsAtTheBeginningOfMethodService.commonCallsAtTheBeginningOfAMethod();
                log.info("Number Of Common Calls At The Beginning Of A Method: " + numberOfCommonCallsAtTheBeginningOfAMethod);
                aspectMiningSummary = new AspectMiningSummary(100, 150, 180);

                AspectMiningByCategory[] aspectMiningByCategories = new AspectMiningByCategory[2];
                AspectMiningByCategory aspectMiningByCategory =
                        new AspectMiningByCategory("Common Calls At The Beginning Of A Method",
                                numberOfCommonCallsAtTheBeginningOfAMethod,0,0);
                aspectMiningByCategories[0] = aspectMiningByCategory;

                int numberOfCommonCallsAtTheEndOfAMethod = callsAtTheEndOfMethodService.commonCallsAtTheEndOfAMethod();
                log.info("Number Of Common Calls At The End Of A Method: " + numberOfCommonCallsAtTheBeginningOfAMethod);

                aspectMiningByCategory =
                        new AspectMiningByCategory("Common Calls At The End Of A Method",
                                numberOfCommonCallsAtTheEndOfAMethod,0,0);
                aspectMiningByCategories[1] = aspectMiningByCategory;

                aspectMiningSummary.setAspectMiningByCategory(aspectMiningByCategories);
            }
        }

        return aspectMiningSummary;
    }

    public void getAspectMiningResults(Map<String, String> aspectMineMap) {
        log.info("Dispatching: " + aspectMineMap);

        codeCloneController.mineForAspects();
        executionTraceController.mineForAspects();
    }

    private AmvClientController getAmvClientController(ApplicationContext context) {
        return (AmvClientController) context.getBean("amvClientController");
    }

    private void terminateApplicationAbnormally(String[] args) {
        throw new IllegalArgumentException("Illegal arguments passed to AmvClient application, arguments: " + args);
    }

    private boolean isValidArgumentsPassedForUseCase(String[] args) {
        return ("AspectMining".equalsIgnoreCase(ASPECT_MINING_USE_CASE)) ? true : false;
    }

    private void listParametersPassedWhenStartingApplication(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(" ")
                    .append(arg);
        }
        log.info("Arguments passed when starting the application: " + sb.toString());
    }

    public AspectMiningSummary getAspectMiningSummary() {
        AspectMiningSummary aspectMineSummary = new AspectMiningSummary(1200, 1500, 1800);
        aspectMineSummary.setAspectMiningByCategory(getAspectMiningByCategory());
        return aspectMineSummary;
    }

    public AspectMiningByCategory[] getAspectMiningByCategory() {
        AspectMiningByCategory[] aspectMiningByCategories = new AspectMiningByCategory[1];
        AspectMiningByCategory aspectMiningByCategory;
        aspectMiningByCategory = new AspectMiningByCategory(
                "Common Calls At The Beginning Of A Method",
                100,
                200,
                300
        );
        aspectMiningByCategories[0] = aspectMiningByCategory;
        return aspectMiningByCategories;
    }
}
