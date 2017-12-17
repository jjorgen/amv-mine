package org.nsu.dcis.amv.core.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by jorgej2 on 12/3/2017.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ExecutionTraceControllerTest {

    @Autowired
    ExecutionTraceController executionTraceController;

    @Test
    public void mineForAspects() throws Exception {
        executionTraceController.mineForAspects();
    }

}