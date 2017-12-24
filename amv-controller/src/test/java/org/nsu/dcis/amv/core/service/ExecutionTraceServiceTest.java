package org.nsu.dcis.amv.core.service;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsu.dcis.amv.core.exception.ExecutionTraceException;
import org.nsu.dcis.amv.core.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;

/**
 * Created by jorgej2 on 12/3/2017.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ExecutionTraceServiceTest {

    @Autowired
    FileUtil fileUtil;

    @Autowired
    private ExecutionTraceService executionTraceService;

    private Logger log = Logger.getLogger(getClass().getName());

    private String EXECUTION_TRACE_LOG_FILE_WITH_ONE_FULL_RELATION = "C:/log/method_execution_trace_with_one_full_relation.log";
    private String EXECUTION_TRACE_LOG_FILE_WITH_FOUR_RELATIONS = "C:/log/method_execution_trace_with_three_relations.log";
    private String FULL_EXECUTION_TRACE_LOG_FILE = "C:/log/method_execution_trace.log";
    private String EXECUTION_TRACE_LOG_FILE_WITH_ONE_RELATION = "C:/log/method_execution_trace_WITH_ONE_LINE_FOR_TESTING_PURPOSE.log";
    private String METHOD_EXECUTION_RELATIONS_FILE = "C:/log/method_execution_relations.log";

    @Test
    public void getAllRelationsWithCallingContext() throws Exception {
        BufferedReader executionTraceLogFileReader = fileUtil.openFileForReadingLines(FULL_EXECUTION_TRACE_LOG_FILE);
        List<Pair<InsideRelation, InsideRelation>> insideRelationsPairs = executionTraceService.getAllInsideRelations(executionTraceLogFileReader);
        executionTraceService.writeInsideRelations(insideRelationsPairs, METHOD_EXECUTION_RELATIONS_FILE);
    }

    @Test
    public void getRelationsWithACallingContext() throws Exception {
        BufferedReader executionTraceLogFileReader = fileUtil.openFileForReadingLines(FULL_EXECUTION_TRACE_LOG_FILE);
        List<InsideRelationsTree> insideRelationsTreeList = getInsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        List<InsideRelation> insideRelations = new ArrayList<>();
        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            insideRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
        }
        log.info("Total number of relations: " + insideRelations.size());

        List<InsideRelation> insideRelationsWithCallingContext = new ArrayList<>();
        for (InsideRelation insideRelation : insideRelations) {
            if (insideRelation.hasCallingContext()) {
                insideRelationsWithCallingContext.add(insideRelation);
            }
        }
        log.info("Number of relations with calling context: " + insideRelationsWithCallingContext.size());
    }

    @Test
    public void getAllRelations() throws Exception {
        BufferedReader executionTraceLogFileReader = fileUtil.openFileForReadingLines(FULL_EXECUTION_TRACE_LOG_FILE);
        List<InsideRelationsTree> insideRelationsTreeList = getInsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        List<InsideRelation> insideRelations = new ArrayList<>();
        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            insideRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
        }
        Assert.assertEquals("Number of relations expected was 5714",5714, insideRelations.size());

        for (InsideRelation insideRelation : insideRelations) {
            log.info(insideRelation);
        }

        BufferedWriter bufferedWriter = fileUtil.openFileForWritingLines(METHOD_EXECUTION_RELATIONS_FILE);
        for (InsideRelation insideRelation : insideRelations) {
            fileUtil.writeLineToFile(bufferedWriter, insideRelation.toString());
        }
        fileUtil.closeFileWritingLines(bufferedWriter);
    }

    @Test
    public void getInsideRelationsTrees() throws Exception {
        BufferedReader executionTraceLogFileReader = fileUtil.openFileForReadingLines(EXECUTION_TRACE_LOG_FILE_WITH_FOUR_RELATIONS);
        List<InsideRelationsTree> insideRelationsTreeList = getInsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        List<InsideRelation> insideRelations = new ArrayList<>();
        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            insideRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
        }
        Assert.assertEquals("Four relations were expected",4, insideRelations.size());

        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            insideRelationsTree.print();
            log.info("");
        }
    }

    @Test
    public void getOneInsideRelationsTree() throws Exception {
        BufferedReader executionTraceLogFileReader = fileUtil.openFileForReadingLines(EXECUTION_TRACE_LOG_FILE_WITH_ONE_FULL_RELATION);
        List<InsideRelationsTree> insideRelationsTreeList = getInsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            insideRelationsTree.print();
        }
    }

    @Test
    public void getThreeInsideRelationsTrees() throws Exception {
        BufferedReader executionTraceLogFileReader = fileUtil.openFileForReadingLines(EXECUTION_TRACE_LOG_FILE_WITH_FOUR_RELATIONS);

        List<InsideRelationsTree> nextInsideRelationsTreeList = getInsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);
    }

    @Test
    public void getLastInsideRelationsTree() throws Exception {
        BufferedReader executionTraceLogFileReader = fileUtil.openFileForReadingLines(EXECUTION_TRACE_LOG_FILE_WITH_ONE_RELATION);

        TraceMethod traceMethodFromFile = executionTraceService.getTraceMethod(executionTraceLogFileReader);
        InsideRelationsTree insideRelationsTree = new InsideRelationsTree(traceMethodFromFile);

        InsideRelationsTree nextInsideRelationsTree = executionTraceService.getNextInsideRelationsTree(
                insideRelationsTree, executionTraceLogFileReader);

        boolean last = nextInsideRelationsTree.isLast();
        System.out.println("last " + last);
    }

    @Test
    public void getInsideRelationsTree() throws Exception {
        BufferedReader executionTraceLogFileReader = fileUtil.openFileForReadingLines(FULL_EXECUTION_TRACE_LOG_FILE);

        List<InsideRelationsTree> nextInsideRelationsTreeList = getInsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);
    }

    @Test
    public void getTraceMethodFromFile() throws Exception {
        BufferedReader bufferedReader = fileUtil.openFileForReadingLines(FULL_EXECUTION_TRACE_LOG_FILE);
        TraceMethod traceMethodFromFile = executionTraceService.getTraceMethod(bufferedReader);
        fileUtil.closeFileForReadingLines(bufferedReader);

        Assert.assertEquals(0, traceMethodFromFile.getLevel());
        Assert.assertNotNull(traceMethodFromFile.getLineReadFromFile());
    }

    @Test
    public void mineForAspects() throws Exception {
        executionTraceService.mineForAspects(FULL_EXECUTION_TRACE_LOG_FILE);
    }

    @Test
    public void getRelation()  throws Exception {
        executionTraceService.getNextRelation(getBufferedReader());
    }

    @Test
    public void getLevelZero() throws Exception {
        int level = executionTraceService.getLevel(getLineLevelZero());
        Assert.assertEquals(0, level);
    }

    @Test
    public void getLevelOne() throws Exception {
        int level = executionTraceService.getLevel(getLineLevelOne());
        Assert.assertEquals(1, level);
    }

    @Test
    public void getLevelTwo() throws Exception {
        int level = executionTraceService.getLevel(getLineLevelTwo());
        Assert.assertEquals(2, level);
    }

    @Test
    public void getLevelThree() throws Exception {
        int level = executionTraceService.getLevel(getLineLevelThree());
        Assert.assertEquals(3, level);
    }

    @Test(expected = ExecutionTraceException.class)
    public void getInvalidLevel() throws Exception {
        int level = executionTraceService.getLevel(getLineWithInvalidLevel());
        Assert.assertEquals(4, level);
    }

    private BufferedReader getBufferedReader() {
        return fileUtil.openFileForReadingLines(FULL_EXECUTION_TRACE_LOG_FILE);
    }

    private String getLineLevelZero() {
        return " INFO [2017-12-02 21:24:00,731] [main] [MethodExecutionTraceAspect.java:33] - Entering[void org.nsu.dcis.gj214.AppTest.testnametest()]";
    }

    private String getLineLevelOne() {
        return "INFO [2017-12-02 21:24:00,731] [main] [MethodExecutionTraceAspect.java:33] -   Entering[void CH.ifa.draw.apackage.App.main(String[])]";
    }

    public String getLineLevelTwo() {
        return " INFO [2017-12-02 21:24:00,731] [main] [MethodExecutionTraceAspect.java:33] -      Entering[void CH.ifa.draw.apackage.ClassA.callingOnA(int, String)]";
    }

    public String getLineLevelThree() {
        return "INFO [2017-12-02 21:24:00,731] [main] [MethodExecutionTraceAspect.java:33] -         Entering[void CH.ifa.draw.bpackage.ClassB.callingOnB(int, String)]";
    }

    public String getLineWithInvalidLevel() {
        return "INFO [2017-12-02 21:24:00,731] [main] [MethodExecutionTraceAspect.java:33] -         Invalid[void CH.ifa.draw.bpackage.ClassB.callingOnB(int, String)]";
    }

    private List<InsideRelationsTree> getInsideRelationsTree(BufferedReader executionTraceLogFileReader) {
        List<InsideRelationsTree> insideRelationsTreeList = new ArrayList<>();
        TraceMethod traceMethodFromFile = executionTraceService.getTraceMethod(executionTraceLogFileReader);
        InsideRelationsTree nextInsideRelationsTree = new InsideRelationsTree(traceMethodFromFile);
        do {
            nextInsideRelationsTree = executionTraceService.getNextInsideRelationsTree(
                    nextInsideRelationsTree, executionTraceLogFileReader);
            if (!nextInsideRelationsTree.isEmpty()) {
                insideRelationsTreeList.add(nextInsideRelationsTree);
            }
        } while (!nextInsideRelationsTree.isLast());
        return insideRelationsTreeList;
    }
}