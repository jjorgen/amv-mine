package org.nsu.dcis.amv.core.controller;

import org.nsu.dcis.amv.common.AspectMiningByCategory;
import org.nsu.dcis.amv.common.AspectMiningRequest;
import org.nsu.dcis.amv.common.AspectMiningResult;
import org.nsu.dcis.amv.common.AspectMiningSummary;
import org.nsu.dcis.amv.core.service.CallsAtTheBeginningOfMethodService;
import org.nsu.dcis.amv.core.service.CallsAtTheEndOfMethodService;
import org.nsu.dcis.amv.core.service.CrossCuttingConcernAsInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class AmvClient {

    @Autowired
    CallsAtTheBeginningOfMethodService callsAtTheBeginningOfMethodService;

    @Autowired
    CrossCuttingConcernAsInterfaceService crossCuttingConcernAsInterfaceService;

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
        List<AspectMiningByCategory> aspectMiningByCategoryList = new ArrayList();

        AspectMiningSummary aspectMiningSummary = new AspectMiningSummary();
        List<AspectMiningRequest.AspectMiningCategory> aspectMiningCategories = aspectMiningRequest.getAspectMiningCategories();
        log.info("Aspect Mining Categories Count: " + aspectMiningCategories.size());
        AspectMiningByCategory[] aspectMiningByCategories = new AspectMiningByCategory[aspectMiningCategories.size()];
        for (AspectMiningRequest.AspectMiningCategory aspectMiningCategory : aspectMiningCategories) {

            if (aspectMiningCategory.name().equalsIgnoreCase(AspectMiningRequest.AspectMiningCategory.CROSS_CUTTING_CONCERN_AS_INTERFACE.name())) {
                aspectMiningByCategoryList.addAll(crossCuttingConcernAsInterfaceService.getCrossCuttingConcerns());
            }
            if (aspectMiningCategory.name().equalsIgnoreCase(AspectMiningRequest.AspectMiningCategory.CALLS_AT_BEGINNING_AND_END_OF_METHODS.name())) {
                int numberOfCommonCallsAtTheBeginningOfAMethod = callsAtTheBeginningOfMethodService.commonCallsAtTheBeginningOfAMethod();
                log.info("Number Of Common Calls At The Beginning Of A Method: " + numberOfCommonCallsAtTheBeginningOfAMethod);
                aspectMiningSummary = new AspectMiningSummary(100, 150, 180);

                AspectMiningByCategory aspectMiningByCategory =
                        new AspectMiningByCategory("Calls At The Beginning Of A Method",
                                numberOfCommonCallsAtTheBeginningOfAMethod,0,0);
                aspectMiningByCategories[0] = aspectMiningByCategory;

                int numberOfCommonCallsAtTheEndOfAMethod = callsAtTheEndOfMethodService.commonCallsAtTheEndOfAMethod();
                log.info("Number Of Calls At The End Of A Method: " + numberOfCommonCallsAtTheBeginningOfAMethod);

                aspectMiningByCategory =
                        new AspectMiningByCategory("Calls At The End Of A Method",
                                numberOfCommonCallsAtTheEndOfAMethod,0,0);
                aspectMiningByCategories[1] = aspectMiningByCategory;

                aspectMiningSummary.setAspectMiningByCategory(aspectMiningByCategories);
            }
        }
        AspectMiningByCategory[] aspectMiningByCategoryArray = new AspectMiningByCategory[aspectMiningByCategoryList.size()];
        Iterator<AspectMiningByCategory> aspectMiningByCategoryIterator = aspectMiningByCategoryList.iterator();
        int idx = 0;
        while(aspectMiningByCategoryIterator.hasNext()) {
            aspectMiningByCategoryArray[idx++] = aspectMiningByCategoryIterator.next();
        }
        aspectMiningSummary.setAspectMiningByCategory(aspectMiningByCategoryArray);
        return aspectMiningSummary;

//  This code is for testing only
//
//        AspectMiningByCategory aspectMiningByCategory =
//                new AspectMiningByCategory("Ordered Method Call, Inside Relation",
//                        40,0,0);
//        aspectMiningByCategories[2] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Ordered Method Call, Outside Relation",
//                        11,0,0);
//        aspectMiningByCategories[3] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Clone, Before Advice",
//                        40,0,0);
//        aspectMiningByCategories[4] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Clone, After Advice",
//                        5,0,0);
//        aspectMiningByCategories[5] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Clone, Around Advice",
//                        1,0,0);
//        aspectMiningByCategories[6] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Clone, Complete Clone",
//                        33,0,0);
//        aspectMiningByCategories[7] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Cross Cutting Concern As Interface",
//                        33,0,0);
//        aspectMiningByCategories[8] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Calls In Clones",
//                        33,0,0);
//        aspectMiningByCategories[9] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Cross Cutting Concern As Interface, Before Advice",
//                        3,0,0);
//        aspectMiningByCategories[10] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Cross Cutting Concern As Interface, After Advice",
//                        2,0,0);
//        aspectMiningByCategories[11] = aspectMiningByCategory;
//
//        aspectMiningByCategory =
//                new AspectMiningByCategory("Cross Cutting Concern As Interface, Around Advice",
//                        1,0,0);
//        aspectMiningByCategories[12] = aspectMiningByCategory;
//
//
//        aspectMiningSummary.setAspectMiningByCategory(aspectMiningByCategories);
//
//        int total = 0;
//        AspectMiningByCategory[] aspectMineByCategory = aspectMiningSummary.getAspectMineByCategory();
//        for (int i = 0; i < aspectMineByCategory.length; i++) {
//            total += aspectMineByCategory[i].getClusteringCount();
//        }
//        aspectMiningSummary.setClusteringTotalCount(total);
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
                "Calls At The Beginning Of A Method",
                100,
                200,
                300
        );
        aspectMiningByCategories[0] = aspectMiningByCategory;
        return aspectMiningByCategories;
    }
}
