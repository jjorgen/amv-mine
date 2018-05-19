package org.nsu.dcis.amv.core.service;

import org.nsu.dcis.amv.core.util.FanInInstance;
import org.nsu.dcis.amv.core.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by jorgej2 on 3/31/2018.
 */
@Service
public class VectorSpaceModel_One_fanIn_numCallers_Service {

    @Autowired
    private FileUtil fileUtil;

    private BufferedWriter bufferedWriter = null;
    private BufferedWriter bufferedWriterForRFile = null;
    private int lineNumber;

    public void createVectorSpaceModelNumberOne(String fanInResultFilePath, String vector_M_One_FilePath, String vector_M_One_FilePath_For_System_R) {
        BufferedReader bufferedReader = fileUtil.openFileForReadingLines(fanInResultFilePath);
        bufferedWriter = fileUtil.openFileForWritingLines(vector_M_One_FilePath);
        bufferedWriterForRFile = fileUtil.openFileForWritingLines(vector_M_One_FilePath_For_System_R);
        FanInInstance fanInInstance = null;

        String lineFromFile = fileUtil.readLineFromFile(bufferedReader);
        while(moreLinesToReadFromFile(lineFromFile)) {
            if (FanInInstance.isStartOfFanInInstance(lineFromFile)) {
                writeToVector(fanInInstance, bufferedWriter, bufferedWriterForRFile);
                fanInInstance = new FanInInstance(lineFromFile);
            } else {
                addItem(lineFromFile, fanInInstance);
            }
            lineFromFile = fileUtil.readLineFromFile(bufferedReader);
        }
        writeToVector(fanInInstance, bufferedWriter, bufferedWriterForRFile);

        fileUtil.closeFileForReadingLines(bufferedReader);
        fileUtil.closeFileWritingLines(bufferedWriter);
        fileUtil.closeFileWritingLines(bufferedWriterForRFile);
   }

    private void addItem(String lineFromFile, FanInInstance fanInInstance) {
        if (!"Callers:".equals(lineFromFile.trim())) {
            fanInInstance.addItem(lineFromFile);
        }
    }

    private void writeToVector(FanInInstance fanInInstance, BufferedWriter bufferedWriter, BufferedWriter bufferedWriterForRFile) {
        if (fanInInstance != null) {
            System.out.println("Writing Line Number: " + ++lineNumber);
            fileUtil.writeLineToFile(bufferedWriter, fanInInstance.getVectorInstance(lineNumber));
            fileUtil.writeLineToFile(bufferedWriterForRFile, fanInInstance.getVectorInstanceForRFile(lineNumber));
        }
    }

    private boolean moreLinesToReadFromFile(String lineFromFile) {
        return lineFromFile != null;
    }

    public void groupMethodsIntoClusters(String clusteringResultsFilePath_v1, String vectorFilePath_v1, String clusterDistribution) {

        BufferedWriter bufferedWriterVector = fileUtil.openFileForWritingLines(clusterDistribution);
        List<Integer> clusterNumbers = getClusterNumbers(clusteringResultsFilePath_v1);
        List<String> vector = getVector(vectorFilePath_v1);

        for (int index = 1; index < 22; index++) {
            System.out.println("Start identifying methods in cluster number: "  + index);
            fileUtil.writeLineToFile(bufferedWriterVector, "\n\n*********************************************************");
            fileUtil.writeLineToFile(bufferedWriterVector, "Start identifying methods in cluster number: "  + index);
            fileUtil.writeLineToFile(bufferedWriterVector, "*********************************************************");
            int vectorItem = 0;
            for (Integer clusterNumber : clusterNumbers) {
                if (index == clusterNumber) {
                    fileUtil.writeLineToFile(bufferedWriterVector, vector.get(vectorItem));
                }
                ++vectorItem;
            }
        }
        fileUtil.closeFileWritingLines(bufferedWriterVector);
    }

    private List<String> getVector(String vectorFilePath_v1) {
        BufferedReader bufferedReaderVector = fileUtil.openFileForReadingLines(vectorFilePath_v1);
        String lineFromFile = fileUtil.readLineFromFile(bufferedReaderVector);
        List<String> vector = new ArrayList<>();
        while(moreLinesToReadFromFile(lineFromFile)) {
            vector.add(lineFromFile);
            lineFromFile = fileUtil.readLineFromFile(bufferedReaderVector);
        }
        fileUtil.closeFileForReadingLines(bufferedReaderVector);
        return vector;
    }

    private List<Integer> getClusterNumbers(String clusteringResultsFilePath_v1) {
        BufferedReader bufferedReaderClusterResult = fileUtil.openFileForReadingLines(clusteringResultsFilePath_v1);

        List<Integer> clusterNumbers = new ArrayList<>();
        String lineFromFile = fileUtil.readLineFromFile(bufferedReaderClusterResult);
        StringBuffer sb = new StringBuffer();
        while(moreLinesToReadFromFile(lineFromFile)) {
            sb.append(lineFromFile).append(",");
            lineFromFile = fileUtil.readLineFromFile(bufferedReaderClusterResult);
        }
        fileUtil.closeFileForReadingLines(bufferedReaderClusterResult);

        String clusteringResult = sb.toString();
        clusteringResult.replaceAll("\n", ",");
        System.out.println(clusteringResult);
        clusteringResult = clusteringResult.substring(0, clusteringResult.length() - 1);

        StringTokenizer stringTokenizer = new StringTokenizer(clusteringResult, ",");
        while(stringTokenizer.hasMoreTokens()) {
            String clusterNumberAsString = stringTokenizer.nextToken();
            clusterNumbers.add(Integer.valueOf(clusterNumberAsString));
        }
        fileUtil.closeFileForReadingLines(bufferedReaderClusterResult);
        return clusterNumbers;
    }
}
