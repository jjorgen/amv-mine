package org.nsu.dcis.amv.core.service;

import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.domain.EventTracesResults;
import org.nsu.dcis.amv.core.exception.ExecutionTraceException;
import org.nsu.dcis.amv.core.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;

@Service
public class ExecutionTraceService {

    @Autowired
    FileUtil fileUtil;

    private Logger log = Logger.getLogger(getClass().getName());
    private Object allInsideRelations;

    public EventTracesResults mineForAspects(String pathToMethodExecutionTraceLog) {
        log.info("Mine using event traces.");

        BufferedReader bufferedReader = fileUtil.openFileForReadingLines(pathToMethodExecutionTraceLog);
        ExecutionTraceRelation executionTraceRelation = getNextRelation(bufferedReader);

        String line = fileUtil.readLineFromFile(bufferedReader);
        log.info("Line read from file: " + line);
        fileUtil.closeFileForReadingLines(bufferedReader);

        return new EventTracesResults();
    }

    public ExecutionTraceRelation getNextRelation(BufferedReader bufferedReader) {
        ExecutionTraceRelation executionTraceRelation = new ExecutionTraceRelation();
        String line = fileUtil.readLineFromFile(bufferedReader);
        int level = getLevel(line);
        return executionTraceRelation;
    }

    public int getLevel(String line) {
        int idx = 1;
        int dashPosition = line.indexOf("- ");
        int enteringPosition = line.indexOf("Entering");

        if ((enteringPosition - dashPosition) == 2) {
            return 0;
        } else if ((enteringPosition - dashPosition) == 4) {
            return 1;
        } else {
            while ((enteringPosition - dashPosition) != 4 + idx * 3) {
                if ((4 + idx * 3) > line.length()) {
                    throw new ExecutionTraceException("Unable to determine level");
                }
                ++idx;
            }
        }
        return idx + 1;
    }

    public void filterOutTestMethodsFromMethodExecutionTrace(String execution_trace_log_file, String filtered_execution_trace_log_file) {
        String lineReadFromFile;
        BufferedReader bufferedReader = fileUtil.openFileForReadingLines(execution_trace_log_file);
        BufferedWriter bufferedWriter = fileUtil.openFileForWritingLines(filtered_execution_trace_log_file);
        int lineReadCounter = 0;
        do {
            lineReadFromFile = fileUtil.readLineFromFile(bufferedReader);

            log.info("Line number read: " + ++lineReadCounter);
            if (lineReadCounter > 100) {
                break;
            }

            if (lineReadFromFile != null) {
                if (!isTestMethod(lineReadFromFile)) {
                    fileUtil.writeLineToFile(bufferedWriter, lineReadFromFile);
                }
            }

        } while(lineReadFromFile != null);

        fileUtil.closeFileForReadingLines(bufferedReader);
        fileUtil.closeFileWritingLines(bufferedWriter);
    }

    private boolean isTestMethod(String lineReadFromFile) {
        if (lineReadFromFile.contains(".test.")) {
            return true;
        }
        return false;
    }

    /**
     * This method gets one inside relations tree. The relations tree is created from methods
     * read from the raw executions trace file.
     */
    public InsideRelationsTree getNextOutsideRelationsTree(InsideRelationsTree nextInsideRelationsTree,
                                                          BufferedReader executionTraceLogFileReader) {

        ArrayList<TraceMethod> traceMethodsInRelationsTree = new ArrayList<>();
        traceMethodsInRelationsTree.add(nextInsideRelationsTree.getRootForNextRelationTree());

        TraceMethod traceMethod = getTraceMethod(executionTraceLogFileReader);
        if (traceMethod.getLevel() > nextInsideRelationsTree.getRootForNextRelationTree().getLevel()) {
            int expectedLevel = traceMethod.getLevel();
            while (expectedLevel++ == traceMethod.getLevel()) {
                traceMethodsInRelationsTree.add(traceMethod);
                traceMethod = getTraceMethod(executionTraceLogFileReader);
            }
            return new InsideRelationsTree(traceMethodsInRelationsTree, traceMethod);
        }
        return new InsideRelationsTree(traceMethodsInRelationsTree, traceMethod);
    }

    /**
     * This method gets one inside relations tree. The relations tree is created from methods
     * read from the raw executions trace file.
     */
    public InsideRelationsTree getNextInsideRelationsTree(InsideRelationsTree nextInsideRelationsTree,
                                                          BufferedReader executionTraceLogFileReader) {

        ArrayList<TraceMethod> traceMethodsInRelationsTree = new ArrayList<>();
        traceMethodsInRelationsTree.add(nextInsideRelationsTree.getRootForNextRelationTree());

        TraceMethod traceMethod = getTraceMethod(executionTraceLogFileReader);
        if (traceMethod.getLevel() > nextInsideRelationsTree.getRootForNextRelationTree().getLevel()) {
            int expectedLevel = traceMethod.getLevel();
            while (expectedLevel++ == traceMethod.getLevel()) {
                traceMethodsInRelationsTree.add(traceMethod);
                traceMethod = getTraceMethod(executionTraceLogFileReader);
            }
            return new InsideRelationsTree(traceMethodsInRelationsTree, traceMethod);
        }
        return new InsideRelationsTree(traceMethodsInRelationsTree, traceMethod);
    }

    /**
     * This method reads one trace method from the raw execution trace log file.
     * A trace method consists of one line with the traced method and the level
     * at which this method was executed. The level corresponds to the number of
     * indentations at which the method was written.
     *
     * @param executionTraceLogFileReader
     * @return
     */
    public TraceMethod getTraceMethod(BufferedReader executionTraceLogFileReader) {
        String lineReadFromFile = fileUtil.readLineFromFile(executionTraceLogFileReader);
        if (isLastLineReadFromFile(lineReadFromFile)) {
            return new TraceMethod();
        }
        int level = getLevel(lineReadFromFile);
        return new TraceMethod(level, lineReadFromFile);
    }

    private boolean isLastLineReadFromFile(String lineReadFromFile) {
        return lineReadFromFile == null;
    }

    public List<Pair<InsideRelation, InsideRelation>>  getAllInsideRelations(BufferedReader executionTraceLogFileReader) {

        List<InsideRelationsTree> insideRelationsTreeList = getInsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        List<InsideRelation> allInsideRelations = new ArrayList<>();
        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            allInsideRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
        }

        InsideRelation[] insideRelationsArray = getArrayRepresentationOfAllInsideRelations(allInsideRelations);
        InsideRelation[] insideRelationsArrayCmp = getArrayRepresentationOfAllInsideRelations(allInsideRelations);

        Map<Integer, List<InsideRelation>> setsOfInsideRelations = getSetsOfInsideRelations(insideRelationsArray, insideRelationsArrayCmp);

        Set<Integer> keys = setsOfInsideRelations.keySet();
//        log.info("Number of set of equal relations: " + keys.size());

//        logNumberOfRelationsInRelationsSets(setsOfInsideRelations, keys);

        Map<Integer, List<InsideRelation>> mapOfRelationsInDifferentContexts = getSetOfRelationsInDifferentContexts(setsOfInsideRelations);
        log.info(mapOfRelationsInDifferentContexts);

        List<Pair<InsideRelation, InsideRelation>> insideRelationsPairs = getSetsOfInstanceRelationsWithDifferentContexts(setsOfInsideRelations, keys);

        return insideRelationsPairs;
    }

    private Map<Integer, List<InsideRelation>> getSetOfRelationsInDifferentContexts(Map<Integer, List<InsideRelation>> setsOfInsideRelations) {
        Map<Integer, List<InsideRelation>> mapOfRelationsInDifferentContexts = new TreeMap<>();
        Set<Integer> keySet = setsOfInsideRelations.keySet();

        outer: for (Integer key : keySet) {
            List<InsideRelation> equalRelations = setsOfInsideRelations.get(key);
            InsideRelation firstInsideRelation = equalRelations.get(0);
            List<InsideRelation> relationsInDifferentContexts = new ArrayList<> ();
            relationsInDifferentContexts.add(firstInsideRelation);
            boolean firstComparison = true;
            for (InsideRelation insideRelation : equalRelations) {
                if (firstComparison) {
                    firstComparison = false;
                } else {
                    for (InsideRelation insideRelationCmp : relationsInDifferentContexts) {
                        if (!insideRelation.equals(insideRelationCmp)) {
                            relationsInDifferentContexts.add(insideRelation);
                        }
                    }
                }
            }
            if (relationsInDifferentContexts.size() > 1) {
                mapOfRelationsInDifferentContexts.put(key, relationsInDifferentContexts);
            }
        }
        return mapOfRelationsInDifferentContexts;
    }

    private List<Pair<InsideRelation, InsideRelation>> getSetsOfInstanceRelationsWithDifferentContexts(Map<Integer, List<InsideRelation>> setsOfInsideRelations, Set<Integer> keys) {
        List<Pair<InsideRelation, InsideRelation>> insideRelationsPairs = new ArrayList<>();
        outer: for (Integer key : keys) {
            List<InsideRelation> equalRelations = setsOfInsideRelations.get(key);
            InsideRelation firstInsideRelation = equalRelations.get(0);
            for (InsideRelation insideRelation : equalRelations) {
                if (!firstInsideRelation.equalContext(insideRelation)) {
                    insideRelationsPairs.add(new Pair(firstInsideRelation, insideRelation));
                    continue outer;
                }
            }
//            log.info("Equal Relations size: " + equalRelations.size());
        }
//        log.info("Number of cross cutting concerns from inside relations: " + insideRelationsPairs.size());
        return insideRelationsPairs;
    }

    private void logNumberOfRelationsInRelationsSets(Map<Integer, List<InsideRelation>> setsOfInsideRelations, Set<Integer> keys) {
        for (Integer key : keys) {
            List<InsideRelation> equalRelation = setsOfInsideRelations.get(key);
            log.info("Equal Relations size: " + equalRelation.size());
        }
    }

    private Map<Integer, List<InsideRelation>> getSetsOfInsideRelations(InsideRelation[] insideRelationsArray, InsideRelation[] insideRelationsArrayCmp) {
        Map<Integer, List<InsideRelation>> insideRelationMap = new TreeMap<>();
        int idx = 0;
        for (int i = 0; i < insideRelationsArray.length; i++) {
            InsideRelation insideRelationOne = insideRelationsArray[i];
            List<InsideRelation> equalRelations = new ArrayList<>();
            equalRelations.add(insideRelationOne);
            for (int j = 0; j < insideRelationsArrayCmp.length; j++) {
                InsideRelation insideRelationTwo = insideRelationsArrayCmp[j];
                if (i == j) {
                    continue;
                } else {
                    if (insideRelationOne.equals(insideRelationTwo)) {
                        equalRelations.add(insideRelationTwo);
                    }
                }
            }
            if (equalRelations.size() > 1) {
                insideRelationMap.put(idx++, equalRelations);
            }
        }
        return insideRelationMap;
    }

    private InsideRelation[] getArrayRepresentationOfAllInsideRelations(List<InsideRelation> allInsideRelations) {
        return allInsideRelations.toArray(new InsideRelation[0]);
    }

    /**
     * This method gets the list of inside relations trees. The relations tree is created from methods
     * read from the raw executions trace file.
     *
     * @param executionTraceLogFileReader the raw execution trace file
     * @return
     */
    public List<InsideRelationsTree> getOutsideRelationsTree(BufferedReader executionTraceLogFileReader) {
        List<InsideRelationsTree> outsideRelationsTreeList = new ArrayList<>();
        TraceMethod traceMethodFromFile = getTraceMethod(executionTraceLogFileReader);
        InsideRelationsTree nextInsideRelationsTree = new InsideRelationsTree(traceMethodFromFile);
        do {
            nextInsideRelationsTree = getNextInsideRelationsTree(
                    nextInsideRelationsTree, executionTraceLogFileReader);
            if (moreInsideRelationsToRead(nextInsideRelationsTree)) {
                outsideRelationsTreeList.add(nextInsideRelationsTree);
            }
        } while (moreInsideRelationsToRead(nextInsideRelationsTree));
        return outsideRelationsTreeList;
    }

    /**
     * This method gets the list of inside relations trees. The relations tree is created from methods
     * read from the raw executions trace file.
     *
     * @param executionTraceLogFileReader the raw execution trace file
     * @return
     */
    private List<InsideRelationsTree> getInsideRelationsTree(BufferedReader executionTraceLogFileReader) {
        List<InsideRelationsTree> insideRelationsTreeList = new ArrayList<>();
        TraceMethod traceMethodFromFile = getTraceMethod(executionTraceLogFileReader);
        InsideRelationsTree nextInsideRelationsTree = new InsideRelationsTree(traceMethodFromFile);
        do {
            nextInsideRelationsTree = getNextInsideRelationsTree(
                    nextInsideRelationsTree, executionTraceLogFileReader);
            if (moreInsideRelationsToRead(nextInsideRelationsTree)) {
                insideRelationsTreeList.add(nextInsideRelationsTree);
            }
        } while (moreInsideRelationsToRead(nextInsideRelationsTree));
        return insideRelationsTreeList;
    }

    private boolean moreInsideRelationsToRead(InsideRelationsTree nextInsideRelationsTree) {
        return !nextInsideRelationsTree.isLast();
    }

    public Set<InsideRelation> getSetOfInstanceRelations(List<Pair<InsideRelation, InsideRelation>> insideRelationsPairs) {
        Set<InsideRelation> insideRelationsSet = new HashSet<InsideRelation>();

        for (Pair<InsideRelation, InsideRelation> insideRelationPair :  insideRelationsPairs) {
            InsideRelation firstInstanceRelation = insideRelationPair.getFirst();
            InsideRelation secondInstanceRelation = insideRelationPair.getSecond();

            insideRelationsSet.add(firstInstanceRelation);
            insideRelationsSet.add(secondInstanceRelation);
        }

        return insideRelationsSet;
    }

    public Map<Integer, Set<InsideRelation>> getInstanceRelationsWithDifferentContext(Set<InsideRelation> insideRelationSet) {
        Set<InsideRelation> exploredSet = new HashSet<>();
        Map<Integer, Set<InsideRelation>> relationsWithDifferentContextMap = new HashMap<>();
        int key = 0;

//        for (InsideRelation insideRelation : insideRelationSet) {
//            log.info(insideRelation);
//        }

        outer: for (InsideRelation insideRelation : insideRelationSet) {
            for (InsideRelation exploredRelation : exploredSet) {
                if (exploredRelation.equals(insideRelation)) {
                    break outer;
                }
            }
            exploredSet.add(insideRelation);

            Set<InsideRelation> relationsWithDifferentContext = new HashSet<>();
            relationsWithDifferentContext.add(insideRelation);

            Set<TraceMethod> uniqueContextSet = new HashSet<>();
            uniqueContextSet.add(insideRelation.getContext());

            int compareCount = 0;
            for (InsideRelation insideRelationCompare : insideRelationSet) {
                if (insideRelationCompare.equals(insideRelation) && !insideRelationCompare.equalContext(insideRelation)) {
                    ++compareCount;
                    //log.info("Compare count: " + compareCount);
                    boolean newContext = true;
                    for (TraceMethod traceMethod : uniqueContextSet) {
                        if (traceMethod.equals(insideRelation.getContext())) {
                            newContext = false;
                            break;
                        }
                    }
                    if (newContext) {
                        relationsWithDifferentContext.add(insideRelationCompare);
                    }
                }
            }
            if (relationsWithDifferentContext.size() > 1) {
                relationsWithDifferentContextMap.put(key++, relationsWithDifferentContext);
            }
        }

        return relationsWithDifferentContextMap;
    }

    public void writeInsideRelations(List<Pair<InsideRelation, InsideRelation>> insideRelationsPairs, String methodExecutionRelationsFile) {
        BufferedWriter bufferedWriter = fileUtil.openFileForWritingLines(methodExecutionRelationsFile);
        int relationCount = 0;
        for (Pair insideRelationsPair : insideRelationsPairs) {

            fileUtil.writeLineToFile(bufferedWriter, "\n\n***********  Start of relation ***********  " +
                    (++relationCount) + "\n\n\n");
            fileUtil.writeLineToFile(bufferedWriter, insideRelationsPair.getFirst().toString());
            fileUtil.writeLineToFile(bufferedWriter, insideRelationsPair.getSecond().toString());
            fileUtil.writeLineToFile(bufferedWriter, "\n\n***********  End of relation ***********  \n\n\n");
        }
        fileUtil.closeFileWritingLines(bufferedWriter);

    }

    public Set<InsideRelation> getOutsideRelations(BufferedReader executionTraceLogFileReader) {
        List<InsideRelationsTree> outsideRelationsTree = getOutsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        Set<InsideRelation> allInsideRelations = new HashSet<>();
//        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
//            allInsideRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
//        }

        return allInsideRelations;
    }

    public Set<InsideRelation> getInsideRelations(BufferedReader executionTraceLogFileReader) {

        List<InsideRelationsTree> insideRelationsTreeList = getOutsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        Set<InsideRelation> allInsideRelations = new HashSet<>();
        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            allInsideRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
        }

        return allInsideRelations;
    }

    public Map<Integer, Set<InsideRelation>> getInsideRelationsWithMultipleContexts(Set<InsideRelation> insideRelationSet) {
        Set<InsideRelation> exploredSet = new HashSet<>();
        Map<Integer, Set<InsideRelation>> relationsWithDifferentContextMap = new HashMap<>();
        int key = 0;

        outer: for (InsideRelation insideRelation : insideRelationSet) {
            for (InsideRelation exploredRelation : exploredSet) {
                if (exploredRelation.equals(insideRelation)) {
                    continue outer;
                }
            }
            exploredSet.add(insideRelation);

            Set<TraceMethod> uniqueContextSet= new HashSet<>();
            uniqueContextSet.add(insideRelation.getContext());

            Set<InsideRelation> relationsWithDifferentContext = new HashSet<>();
            relationsWithDifferentContext.add(insideRelation);

            inner: for (InsideRelation insideRelationCompare : insideRelationSet) {
                if (insideRelationCompare.equals(insideRelation)) {
                    if (!insideRelationCompare.equalContext(insideRelation)) {
                        Set<TraceMethod> uniqueContextNewSet = new HashSet<>(uniqueContextSet);
                        for (TraceMethod traceMethod : uniqueContextNewSet) {
                            if (traceMethod.equals(insideRelationCompare.getContext())) {
                                continue inner;
                            } else {
                                uniqueContextSet = new HashSet<>();
                                uniqueContextSet.addAll(uniqueContextNewSet);
                                uniqueContextSet.add(insideRelationCompare.getContext());
                            }
                        }
                        relationsWithDifferentContext.add(insideRelationCompare);
                    }
                }
            }
            if (relationsWithDifferentContext.size() > 1) {
                relationsWithDifferentContextMap.put(key++, relationsWithDifferentContext);
            }
        }
        return relationsWithDifferentContextMap;
    }
}

