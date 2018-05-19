package org.nsu.dcis.amv.core.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jorgej2 on 4/22/2018.
 */
public class VectorSpaceModel_Two_fanIn_hasMethod_ServiceTest {

    @Autowired
    VectorSpaceModel_Two_fanIn_hasMethod_Service vectorSpaceModel_two_fanIn_hasMethod_service;

    @Test
    public void createVectorSpaceModelNumberTwoTest() throws Exception {
        vectorSpaceModel_two_fanIn_hasMethod_service.createVectorSpaceModelNumberTwo();
    }
}
