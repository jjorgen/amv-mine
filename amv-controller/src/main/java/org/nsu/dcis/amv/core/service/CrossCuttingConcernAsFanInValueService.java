package org.nsu.dcis.amv.core.service;

import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.domain.FanInConcernSeed;
import org.nsu.dcis.amv.core.util.FileUtil;
import org.nsu.dcis.amv.core.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgej2 on 2/4/2018.
 */
@Service
public class CrossCuttingConcernAsFanInValueService {

    @Autowired
    FileUtil fileUtil;

    private static String FAN_IN_ANALYSIS_RESULTS_FILE = "C:/log/AnalysisResults.txt";

    public List<FanInConcernSeed> getCrossCuttingConcerns() {
        List<FanInConcernSeed> fanInConcernSeedList = new ArrayList();
        List<FanInConcernSeed> sortedFanInConcernSeedList = new ArrayList();
        int lineCount = 0;

        BufferedReader bufferedReader = fileUtil.openFileForReadingLines(FAN_IN_ANALYSIS_RESULTS_FILE);
        String line = null;
        do {
            ++lineCount;
            line = fileUtil.readLineFromFile(bufferedReader);
            if (line != null && line.substring(0, 3).equalsIgnoreCase("CH.") ) {
                FanInConcernSeed fanInConcernSeed = new FanInConcernSeed(line);
                fanInConcernSeedList.add(fanInConcernSeed);
            }
        } while (line != null);

        do {
            Pair<Integer, Integer> seedWithMaxFanInValue = seedWithMaxFanInValue(fanInConcernSeedList);
            sortedFanInConcernSeedList.add(fanInConcernSeedList.get(seedWithMaxFanInValue.getFirst()));
            fanInConcernSeedList.remove(seedWithMaxFanInValue.getFirst().intValue());
        } while (fanInConcernSeedList.size() > 0);

        return sortedFanInConcernSeedList;
    }

    private Pair<Integer, Integer> seedWithMaxFanInValue(List<FanInConcernSeed> fanInConcernSeedList) {
        int maxValueIndex = 0;
        int maxFanInValue = 0;

        for (int idx = 0; idx < fanInConcernSeedList.size(); idx++ ) {
            if (fanInConcernSeedList.get(idx).getFanInValue() >= maxFanInValue) {
                maxFanInValue = fanInConcernSeedList.get(idx).getFanInValue();
                maxValueIndex = idx;
            }
        }
        return new Pair(maxValueIndex, maxFanInValue);
    }
}
