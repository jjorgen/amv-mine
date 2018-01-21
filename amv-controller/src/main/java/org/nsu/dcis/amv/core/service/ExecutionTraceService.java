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
    private Object outsideBeforeRelations;
    private int traceFileLineNumber = 0;

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
    public OutsideRelationsTree getNextOutsideRelationsTree(OutsideRelationsTree nextOutsideRelationsTree,
                                                          BufferedReader executionTraceLogFileReader) {

        ArrayList<TraceMethod> traceMethodsInRelationsTree = new ArrayList<>();
        traceMethodsInRelationsTree.add(nextOutsideRelationsTree.getRootForNextRelationTree());

        TraceMethod traceMethod = getTraceMethod(executionTraceLogFileReader);
        if (traceMethod.getLevel() > nextOutsideRelationsTree.getRootForNextRelationTree().getLevel()) {
            int expectedLevel = traceMethod.getLevel();
            while (expectedLevel++ == traceMethod.getLevel()) {
                traceMethodsInRelationsTree.add(traceMethod);
                traceMethod = getTraceMethod(executionTraceLogFileReader);
            }
            return new OutsideRelationsTree(traceMethodsInRelationsTree, traceMethod);
        }
        return new OutsideRelationsTree(traceMethodsInRelationsTree, traceMethod);
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
        TraceMethod traceMethod = new TraceMethod(++traceFileLineNumber, level, lineReadFromFile);
        if (traceMethod.getTraceFileLineNumber() == 25) {
            log.info(traceMethod);
        }

//        log.info(traceMethod);
        return traceMethod;
    }

    private boolean isLastLineReadFromFile(String lineReadFromFile) {
        return lineReadFromFile == null;
    }

    public List<Pair<Relation, Relation>>  getAllInsideRelations(BufferedReader executionTraceLogFileReader) {

        List<InsideRelationsTree> insideRelationsTreeList = getInsideRelationsTrees(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        List<Relation> allRelations = new ArrayList<>();
        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            allRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
        }

        Relation[] relationsArray = getArrayRepresentationOfAllInsideRelations(allRelations);
        Relation[] relationsArrayCmp = getArrayRepresentationOfAllInsideRelations(allRelations);

        Map<Integer, List<Relation>> setsOfInsideRelations = getSetsOfInsideRelations(relationsArray, relationsArrayCmp);

        Set<Integer> keys = setsOfInsideRelations.keySet();
//        log.info("Number of set of equal relations: " + keys.size());

//        logNumberOfRelationsInRelationsSets(setsOfInsideRelations, keys);

        Map<Integer, List<Relation>> mapOfRelationsInDifferentContexts = getSetOfRelationsInDifferentContexts(setsOfInsideRelations);
        log.info(mapOfRelationsInDifferentContexts);

        List<Pair<Relation, Relation>> insideRelationsPairs = getSetsOfInstanceRelationsWithDifferentContexts(setsOfInsideRelations, keys);

        return insideRelationsPairs;
    }

    private Map<Integer, List<Relation>> getSetOfRelationsInDifferentContexts(Map<Integer, List<Relation>> setsOfInsideRelations) {
        Map<Integer, List<Relation>> mapOfRelationsInDifferentContexts = new TreeMap<>();
        Set<Integer> keySet = setsOfInsideRelations.keySet();

        outer: for (Integer key : keySet) {
            List<Relation> equalRelations = setsOfInsideRelations.get(key);
            Relation firstRelation = equalRelations.get(0);
            List<Relation> relationsInDifferentContexts = new ArrayList<> ();
            relationsInDifferentContexts.add(firstRelation);
            boolean firstComparison = true;
            for (Relation relation : equalRelations) {
                if (firstComparison) {
                    firstComparison = false;
                } else {
                    for (Relation relationCmp : relationsInDifferentContexts) {
                        if (!relation.equals(relationCmp)) {
                            relationsInDifferentContexts.add(relation);
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

    private List<Pair<Relation, Relation>> getSetsOfInstanceRelationsWithDifferentContexts(Map<Integer, List<Relation>> setsOfInsideRelations, Set<Integer> keys) {
        List<Pair<Relation, Relation>> insideRelationsPairs = new ArrayList<>();
        outer: for (Integer key : keys) {
            List<Relation> equalRelations = setsOfInsideRelations.get(key);
            Relation firstRelation = equalRelations.get(0);
            for (Relation relation : equalRelations) {
                if (!firstRelation.equalContext(relation)) {
                    insideRelationsPairs.add(new Pair(firstRelation, relation));
                    continue outer;
                }
            }
//            log.info("Equal Relations size: " + equalRelations.size());
        }
//        log.info("Number of cross cutting concerns from inside relations: " + insideRelationsPairs.size());
        return insideRelationsPairs;
    }

    private void logNumberOfRelationsInRelationsSets(Map<Integer, List<Relation>> setsOfInsideRelations, Set<Integer> keys) {
        for (Integer key : keys) {
            List<Relation> equalRelation = setsOfInsideRelations.get(key);
            log.info("Equal Relations size: " + equalRelation.size());
        }
    }

    private Map<Integer, List<Relation>> getSetsOfInsideRelations(Relation[] relationsArray, Relation[] relationsArrayCmp) {
        Map<Integer, List<Relation>> insideRelationMap = new TreeMap<>();
        int idx = 0;
        for (int i = 0; i < relationsArray.length; i++) {
            Relation relationOne = relationsArray[i];
            List<Relation> equalRelations = new ArrayList<>();
            equalRelations.add(relationOne);
            for (int j = 0; j < relationsArrayCmp.length; j++) {
                Relation relationTwo = relationsArrayCmp[j];
                if (i == j) {
                    continue;
                } else {
                    if (relationOne.equals(relationTwo)) {
                        equalRelations.add(relationTwo);
                    }
                }
            }
            if (equalRelations.size() > 1) {
                insideRelationMap.put(idx++, equalRelations);
            }
        }
        return insideRelationMap;
    }

    private Relation[] getArrayRepresentationOfAllInsideRelations(List<Relation> allRelations) {
        return allRelations.toArray(new Relation[0]);
    }

    /**
     * This method gets the list of inside relations trees. The relations tree is created from methods
     * read from the raw executions trace file.
     *
     * @param executionTraceLogFileReader the raw execution trace file
     * @return
     */
    public List<OutsideRelationsTree> getOutsideRelationsTree(BufferedReader executionTraceLogFileReader) {
        List<OutsideRelationsTree> outsideRelationsTreeList = new ArrayList<>();
        TraceMethod traceMethod = getTraceMethod(executionTraceLogFileReader);
        OutsideRelationsTree nextOutsideRelationsTree = new OutsideRelationsTree(traceMethod);
        do {
            nextOutsideRelationsTree = getNextOutsideRelationsTree(
                    nextOutsideRelationsTree, executionTraceLogFileReader);
            if (moreOutsideRelationsToRead(nextOutsideRelationsTree)) {
                outsideRelationsTreeList.add(nextOutsideRelationsTree);
            }
        } while (moreOutsideRelationsToRead(nextOutsideRelationsTree));
        return outsideRelationsTreeList;
    }

    /**
     * This method gets the list of inside relations trees. The relations tree is created from methods
     * read from the raw executions trace file.
     *
     * @param executionTraceLogFileReader the raw execution trace file
     * @return
     */
    private List<InsideRelationsTree> getInsideRelationsTrees(BufferedReader executionTraceLogFileReader) {
        List<InsideRelationsTree> insideRelationsTreeList = new ArrayList<>();
        TraceMethod traceMethod = getTraceMethod(executionTraceLogFileReader);

        // Create seed for the first Inside Relations Tree
        InsideRelationsTree nextInsideRelationsTree = new InsideRelationsTree(traceMethod);
        do {
            // Extract the next Inside Relations Tree from the raw input file.
            nextInsideRelationsTree = getNextInsideRelationsTree(
                nextInsideRelationsTree, executionTraceLogFileReader);

            // If a valid inside relations tree was created then add this to the list.
            if (moreInsideRelationsToRead(nextInsideRelationsTree)) {
                insideRelationsTreeList.add(nextInsideRelationsTree);
            }

          // Continue if there are more inside relations trees that can be extracted from the raw input file.
        } while (moreInsideRelationsToRead(nextInsideRelationsTree));
        return insideRelationsTreeList;
    }

    /**
     * This method gets one inside relations tree. The relations tree is created from methods
     * read from the raw executions trace file.
     */
    public InsideRelationsTree getNextInsideRelationsTree(InsideRelationsTree nextInsideRelationsTree,
                                                          BufferedReader executionTraceLogFileReader) {

        // Initialize an array to hold trace methods in relations tree.
        ArrayList<TraceMethod> traceMethodsInRelationsTree = new ArrayList<>();
        traceMethodsInRelationsTree.add(nextInsideRelationsTree.getRootForNextRelationTree());

        // Get next trace method from the raw input file that may be part of the relations tree.
        TraceMethod traceMethod = getTraceMethod(executionTraceLogFileReader);

        // This method is at a lower level than the prior method and is therefore part of a tree
        if (traceMethod.getLevel() > nextInsideRelationsTree.getRootForNextRelationTree().getLevel()) {
            int expectedLevel = traceMethod.getLevel();
            while (expectedLevel++ == traceMethod.getLevel()) {
                traceMethodsInRelationsTree.add(traceMethod);
                traceMethod = getTraceMethod(executionTraceLogFileReader);
            }
            return new InsideRelationsTree(traceMethodsInRelationsTree, traceMethod);
        }

        // This method is not at a lower level. Create a new Inside Relations Tree Seed.
        return new InsideRelationsTree(traceMethodsInRelationsTree, traceMethod);
    }

    private boolean moreOutsideRelationsToRead(OutsideRelationsTree nextOutsideRelationsTree) {
        return !nextOutsideRelationsTree.isLast();
    }

    private boolean moreInsideRelationsToRead(InsideRelationsTree nextInsideRelationsTree) {
        return !nextInsideRelationsTree.isLast();
    }

    // Outside before  relations are those where: (1) (a) -> (b), (2) (a) and (b) are at the same level
    public List<Relation> getOutsideBeforeRelations(BufferedReader executionTraceLogFileReader) {
        List<OutsideBeforeRelationsTree> outsideBeforeRelationsTreeList = getOutsideBeforeRelationsTrees(executionTraceLogFileReader);

        List<Relation> allRelations = new ArrayList<>();
        for (OutsideBeforeRelationsTree outsideBeforeRelationsTree : outsideBeforeRelationsTreeList) {
            allRelations.addAll(outsideBeforeRelationsTree.getAllOutsideBeforeRelationsInATree());
        }

//        log.info("******************************************************");
//        int idx = 0;
//        for (OutsideBeforeRelationsTree outsideBeforeRelationsTree : outsideBeforeRelationsTreeList) {
//            outsideBeforeRelationsTree.print();
//            log.info("******************************************************");
//            idx++;
//            if (idx > 20) break;
//        }

        return allRelations;
    }

    // Outside before  relations are those where: (1) (a) -> (b), (2) (a) and (b) are at the same level
    private List<OutsideBeforeRelationsTree> getOutsideBeforeRelationsTrees(BufferedReader executionTraceLogFileReader) {
        List<OutsideBeforeRelationsTree> outsideBeforeRelationsTreeList = new ArrayList<>();
        OutsideBeforeRelationsTree tree = new OutsideBeforeRelationsTree();
        TraceMethod traceMethod = getTraceMethod(executionTraceLogFileReader);
        while (!traceMethod.isEmpty()) {
            tree.rebuildWith(traceMethod);
            if (tree.isValid()) {
                traceMethod = getTraceMethod(executionTraceLogFileReader);
                if (!traceMethod.isEmpty()) {
                    while (!traceMethod.isEmpty() && tree.willStillBeValidWhenAdding(traceMethod)) {
                        tree.add(traceMethod);
                        traceMethod = getTraceMethod(executionTraceLogFileReader);
                    }
                    addTreeToOutsideRelationsList(tree, outsideBeforeRelationsTreeList);
                    tree = new OutsideBeforeRelationsTree();
                } else {
                    // The end of the file has been reached. Add the last tree to the
                    // collection of relations tree list.
                    addTreeToOutsideRelationsList(tree, outsideBeforeRelationsTreeList);
                }
            } else {
                traceMethod = getTraceMethod(executionTraceLogFileReader);
            }
        }
        return outsideBeforeRelationsTreeList;
    }

    private void addTreeToOutsideRelationsList(OutsideBeforeRelationsTree tree,
                                               List<OutsideBeforeRelationsTree> outsideBeforeRelationsTreeList) {
        if (!tree.containsTestRelation()) {
            outsideBeforeRelationsTreeList.add(tree);
        }
    }

    /**
     * This method gets one inside relations tree. The relations tree is created from methods
     * read from the raw executions trace file.
     * Outside before  relations are those where: (1) (a) -> (b), (2) (a) and (b) are at the same level
     */
    public OutsideBeforeRelationsTree getNextOutsideBeforeRelationsTree(OutsideBeforeRelationsTree nextOutsideBeforeRelationsTree,
                                                                        BufferedReader executionTraceLogFileReader) {
        // Initialize an array to hold trace methods in relations tree.
        ArrayList<TraceMethod> traceMethodsInRelationsTree = new ArrayList<>();
        traceMethodsInRelationsTree.add(nextOutsideBeforeRelationsTree.getRootForNextRelationTree());

        // Get next trace method from the raw input file that may be part of the relations tree.
        TraceMethod traceMethod = getTraceMethod(executionTraceLogFileReader);
        log.info(traceMethod);

        if (traceMethod.getLevel() > nextOutsideBeforeRelationsTree.getRootForNextRelationTree().getLevel()) {
            int expectedLevel = traceMethod.getLevel();
            while (expectedLevel == traceMethod.getLevel()) {
                traceMethodsInRelationsTree.add(traceMethod);
                traceMethod = getTraceMethod(executionTraceLogFileReader);
                log.info(traceMethod);
            }
            return new OutsideBeforeRelationsTree(traceMethodsInRelationsTree, traceMethod);
        }
        return new OutsideBeforeRelationsTree(traceMethodsInRelationsTree, traceMethod);
    }

    private boolean moreOutsideBeforeRelationsToRead(OutsideBeforeRelationsTree nextOutsideBeforeRelationsTree) {
        return !nextOutsideBeforeRelationsTree.isLast();
    }

    public Set<Relation> getSetOfInstanceRelations(List<Pair<Relation, Relation>> insideRelationsPairs) {
        Set<Relation> relationsSet = new HashSet<Relation>();

        for (Pair<Relation, Relation> insideRelationPair :  insideRelationsPairs) {
            Relation firstInstanceRelation = insideRelationPair.getFirst();
            Relation secondInstanceRelation = insideRelationPair.getSecond();

            relationsSet.add(firstInstanceRelation);
            relationsSet.add(secondInstanceRelation);
        }

        return relationsSet;
    }

    public Map<Integer, Set<Relation>> getInstanceRelationsWithDifferentContext(Set<Relation> relationSet) {
        Set<Relation> exploredSet = new HashSet<>();
        Map<Integer, Set<Relation>> relationsWithDifferentContextMap = new HashMap<>();
        int key = 0;

//        for (Relation insideRelation : relationSet) {
//            log.info(insideRelation);
//        }

        outer: for (Relation relation : relationSet) {
            for (Relation exploredRelation : exploredSet) {
                if (exploredRelation.equals(relation)) {
                    break outer;
                }
            }
            exploredSet.add(relation);

            Set<Relation> relationsWithDifferentContext = new HashSet<>();
            relationsWithDifferentContext.add(relation);

            Set<TraceMethod> uniqueContextSet = new HashSet<>();
            uniqueContextSet.add(relation.getContext());

            int compareCount = 0;
            for (Relation relationCompare : relationSet) {
                if (relationCompare.equals(relation) && !relationCompare.equalContext(relation)) {
                    ++compareCount;
                    //log.info("Compare count: " + compareCount);
                    boolean newContext = true;
                    for (TraceMethod traceMethod : uniqueContextSet) {
                        if (traceMethod.equals(relation.getContext())) {
                            newContext = false;
                            break;
                        }
                    }
                    if (newContext) {
                        relationsWithDifferentContext.add(relationCompare);
                    }
                }
            }
            if (relationsWithDifferentContext.size() > 1) {
                relationsWithDifferentContextMap.put(key++, relationsWithDifferentContext);
            }
        }

        return relationsWithDifferentContextMap;
    }

    public void writeInsideRelations(List<Pair<Relation, Relation>> insideRelationsPairs, String methodExecutionRelationsFile) {
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

    public Set<OutsideRelation> getOutsideRelations(BufferedReader executionTraceLogFileReader) {
        List<OutsideRelationsTree> outsideRelationsTree = getOutsideRelationsTree(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        Set<OutsideRelation> allInsideRelations = new HashSet<>();
//        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
//            allInsideRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
//        }

        return allInsideRelations;
    }

    public Set<Relation> getInsideRelations(BufferedReader executionTraceLogFileReader) {

        List<InsideRelationsTree> insideRelationsTreeList = getInsideRelationsTrees(executionTraceLogFileReader);
        fileUtil.closeFileForReadingLines(executionTraceLogFileReader);

        Set<Relation> allRelations = new HashSet<>();
        for (InsideRelationsTree insideRelationsTree : insideRelationsTreeList) {
            allRelations.addAll(insideRelationsTree.getAllInsideRelationsInATree());
        }

        return allRelations;
    }

    public Map<Integer, Set<Relation>> getInsideRelationsWithMultipleContexts(Set<Relation> relationSet) {
        Set<Relation> exploredRelationsSet = new HashSet<>();
        Map<Integer, Set<Relation>> relationsWithDifferentContextMap = new HashMap<>();
        int key = 0;

        outer: for (Relation relation : relationSet) {
            for (Relation exploredRelation : exploredRelationsSet) {
                if (exploredRelation.equals(relation)) {
                    continue outer;
                }
            }
            exploredRelationsSet.add(relation);

            Set<TraceMethod> uniqueContextSet= new HashSet<>();
            uniqueContextSet.add(relation.getContext());

            Set<Relation> relationsWithDifferentContext = new HashSet<>();
            relationsWithDifferentContext.add(relation);

            inner: for (Relation relationCompare : relationSet) {
                if (relationCompare.equals(relation)) {
                    if (!relationCompare.equalContext(relation)) {
                        Set<TraceMethod> uniqueContextNewSet = new HashSet<>(uniqueContextSet);
                        for (TraceMethod traceMethod : uniqueContextNewSet) {
                            if (traceMethod.equals(relationCompare.getContext())) {
                                continue inner;
                            } else {
                                uniqueContextSet = new HashSet<>();
                                uniqueContextSet.addAll(uniqueContextNewSet);
                                uniqueContextSet.add(relationCompare.getContext());
                            }
                        }
                        relationsWithDifferentContext.add(relationCompare);
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

