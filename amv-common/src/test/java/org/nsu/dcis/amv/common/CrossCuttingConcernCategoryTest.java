package org.nsu.dcis.amv.common;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by jorgej2 on 6/25/2018.
 */
public class CrossCuttingConcernCategoryTest {

    private Logger log = Logger.getLogger(getClass().getName());

    @Test
    public void displayCrossCuttingConcernCategories() throws Exception {
        log.info(CrossCuttingConcernCategory.Category.CROSS_CUTTING_CONCERN_AS_INTERFACE_AROUND_ADVICE.name());
        log.info(CrossCuttingConcernCategory.Category.CROSS_CUTTING_CONCERN_AS_INTERFACE_BEFORE_ADVICE.name());
        log.info(CrossCuttingConcernCategory.Category.CROSS_CUTTING_CONCERN_AS_INTERFACE_AFTER_ADVICE.name());
    }
}
