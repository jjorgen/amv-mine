package org.nsu.dcis.amv.core.service;

import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.domain.EventTracesResults;
import org.nsu.dcis.amv.core.exception.ExecutionTraceException;
import org.nsu.dcis.amv.core.util.ExecutionTraceRelation;
import org.nsu.dcis.amv.core.util.FileUtil;
import org.nsu.dcis.amv.core.util.InsideRelationsTree;
import org.nsu.dcis.amv.core.util.TraceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;

@Service
public class ExecutionTraceService {

    @Autowired
    FileUtil fileUtil;

    private Logger log = Logger.getLogger(getClass().getName());

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

    public TraceMethod getTraceMethod(BufferedReader executionTraceLogFileReader) {
        String lineReadFromFile = fileUtil.readLineFromFile(executionTraceLogFileReader);
        if (lineReadFromFile == null) {
            return new TraceMethod();
        }
        int level = getLevel(lineReadFromFile);
        return new TraceMethod(level, lineReadFromFile);
    }
}
