package org.nsu.dcis.amv.core.util;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;

import static org.junit.Assert.*;

/**
 * Created by jorgej2 on 12/3/2017.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class FileUtilTest {

    @Autowired
    FileUtil fileUtil;

    private Logger log = Logger.getLogger(getClass().getName());
    private static final String FULL_PATH_TO_FILE = "C:/log/method_execution_trace.log";

    @Test
    public void openFileForReadingLines() {
        BufferedReader bufferedReader = fileUtil.openFileForReadingLines(FULL_PATH_TO_FILE);
        String line = fileUtil.readLineFromFile(bufferedReader);
        log.info("Line read from file: " + line);
        fileUtil.closeFileForReadingLines(bufferedReader);
        Assert.assertNotNull("Unable to read line gtom file: " + FULL_PATH_TO_FILE, line);
    }
}