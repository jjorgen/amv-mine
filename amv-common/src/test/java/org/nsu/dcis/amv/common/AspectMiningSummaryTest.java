package org.nsu.dcis.amv.common;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created by jorgej2 on 6/3/2018.
 */
public class AspectMiningSummaryTest {

    @Test
    public void getTotalNumberOfCrossCuttingConcerns() throws Exception {
        AspectMiningSummary aspectMiningSummary = new AspectMiningSummary();

        AspectMiningByCategory before =
                new AspectMiningByCategory("Cross Cutting Concern As Interface, Before Advice",
                0,10,0);
        AspectMiningByCategory after =
                new AspectMiningByCategory("Cross Cutting Concern As Interface, Before Advice",
                        0,20,0);
        AspectMiningByCategory around =
                new AspectMiningByCategory("Cross Cutting Concern As Interface, Before Advice",
                        0,30,0);


        AspectMiningByCategory[] aspectMiningByCategory = new AspectMiningByCategory[3];
        aspectMiningByCategory[0] = before;
        aspectMiningByCategory[1] = after;
        aspectMiningByCategory[2] = around;

        aspectMiningSummary.setAspectMiningByCategory(aspectMiningByCategory);

        assertTrue("Total number of cross cutting concerns should be " +
                aspectMiningSummary.getOverallTotalCount(), aspectMiningSummary.getOverallTotalCount() == 60);
    }
}
