package org.nsu.dcis.amv.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jorgej2 on 3/31/2018.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class VectorSpaceModelOne_fanIn_numCallers_ServiceTest {

    private final String fanInResultfilePath = "C:\\work\\0_NSU\\000_Preparing_The_Dissertation_Report\\Fan_In_Analysis\\Fan_In_Analysis_Results_As_Described_In_Appendix_A.txt";
    private final String vectorFilePath = "C:\\work\\0_NSU\\000_Preparing_The_Dissertation_Report\\Fan_In_Analysis\\Vector_M_One_v1.dat";
    private final String vectorFilePathForR = "C:\\work\\0_NSU\\000_Preparing_The_Dissertation_Report\\Fan_In_Analysis\\Vector_M_One_For_R_v1.dat";

    private final String clusteringResultsFilePath_v1 = "C:\\work\\0_NSU\\000_Preparing_The_Dissertation_Report\\Fan_In_Analysis\\save_1\\m1_kmeans21_v1.dat";
    private final String vectorFilePath_v1 = "C:\\work\\0_NSU\\000_Preparing_The_Dissertation_Report\\Fan_In_Analysis\\save_1\\Vector_M_One_v1.dat";
    private final String clusterDistribution = "C:\\work\\0_NSU\\000_Preparing_The_Dissertation_Report\\Fan_In_Analysis\\save_1\\Vector_M_One_clusterDistribution.dat";

    @Autowired
    VectorSpaceModel_One_fanIn_numCallers_Service vectorSpaceModel_One_fanIn_numCallers_Service;

    @Test
    public void createVectorSpaceModelNumberOneTest() throws Exception {
        vectorSpaceModel_One_fanIn_numCallers_Service.createVectorSpaceModelNumberOne(fanInResultfilePath, vectorFilePath, vectorFilePathForR);
    }

    @Test
    public void groupMethodsIntoClusters() throws Exception {
        vectorSpaceModel_One_fanIn_numCallers_Service.groupMethodsIntoClusters(clusteringResultsFilePath_v1, vectorFilePath_v1, clusterDistribution);
    }
}
