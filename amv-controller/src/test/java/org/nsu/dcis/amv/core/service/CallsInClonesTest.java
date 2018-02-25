package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.ast.Node;
import com.github.javaparser.extend.CompilationUnitWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgej2 on 2/6/2018.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CallsInClonesTest {

//        methodRepresentations.addAll(compilationUnitWrapper.getMethodRepresentations());
    /*
     * This test will use the output from the clone detection unit test as input.
     * For each clone it will extract method calls and then looks for these method
     * calls across clones.
     */

    private CompilationUnitWrapper compilationUnitWrapper;
    private List<MethodRepresentation> methodRepresentations = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        compilationUnitWrapper = new CompilationUnitWrapper("C:/work/0_NSU/CH/ifa/draw/application/DrawApplication.java");
    }

    @Test
    public void getMethodCallsInMethods() throws Exception {
        MethodRepresentation fireViewCreatedEventMethod = compilationUnitWrapper.getMethodRepresentation("fireViewCreatedEvent");
        ArrayList<Node> bodyList = fireViewCreatedEventMethod.getBodyList();
        for (Node node : bodyList) {
            System.out.println(node);
        }
    }
}
