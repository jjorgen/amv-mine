package org.nsu.dcis.amv.core.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsu.dcis.amv.core.domain.FanInConcernSeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by jorgej2 on 2/4/2018.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CrossCuttingConcernAsFanInValueTest {

    @Autowired
    CrossCuttingConcernAsFanInValueService crossCuttingConcernAsFanInValueService;

    private Logger log = Logger.getLogger(getClass().getName());

    @Test
    public void getCrossCuttingConcernAsFanInValueTest() throws Exception {
        List<FanInConcernSeed> sortedFanInConcernSeedList = crossCuttingConcernAsFanInValueService.getCrossCuttingConcerns();
        for (FanInConcernSeed fanInConcernSeed : sortedFanInConcernSeedList) {
            log.info(fanInConcernSeed);
        }
    }
}
