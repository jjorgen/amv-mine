package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.extend.CompilationUnitWrapper;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nsu.dcis.amv.core.instrumentation.AmvConfigurationInstrumentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by jorgej2 on 5/27/2018.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CodeCloneMiningServiceTest {

    @Autowired
    CodeCloneMiningService codeCloneMiningService;

    @Autowired
    AmvConfigurationInstrumentation amvConfigurationInstrumentation;

    private List<CompilationUnitWrapper> allInterfaces;
    private Logger log = Logger.getLogger(getClass().getName());

    @Test
    public void getAllInterfacesTest() throws Exception {
        allInterfaces = codeCloneMiningService.getAllInterfaces(
                amvConfigurationInstrumentation.getRootDir(),
                amvConfigurationInstrumentation.getExcludedDirectoryList(),
                amvConfigurationInstrumentation.getFileExtensions());

        log.info("Number of interfaces found was: " + allInterfaces.size());
        assertTrue("At least one interface should be found in the code base", !allInterfaces.isEmpty());


        log.info("Listing interfaces found in the code base");
        for (CompilationUnitWrapper compilationUnitWrapper : allInterfaces) {
            log.info("Full file name: " + compilationUnitWrapper.getFullFileName());
            log.info("Interface Name: " + compilationUnitWrapper.getClassOrInterfaceName());
            List<MethodRepresentation> methodRepresentations = compilationUnitWrapper.getMethodRepresentations();
            for (MethodRepresentation methodRepresentation : methodRepresentations) {
                log.info("Method representation: " + methodRepresentation.getMethodName());
            }
        }
    }
}