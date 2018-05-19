package org.nsu.dcis.amv.core.util;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jorgej2 on 4/1/2018.
 */
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class FanInInstanceTest {

    public static final String VECTOR_INSTANCE_WHEN_CALLING_METHODS = "CH.ifa.draw.standard.RelativeLocator.east() 5 4";
    private Logger log = Logger.getLogger(getClass().getName());
    private int numberOfUniqueCallingClasses;
    private static final String VECTOR_INSTANCE_WHEN_NO_CALLING_METHODS =
            "CH.ifa.draw.test.contrib.PolygonFigureTest.testOutermostPoint() 0 0";

    @Test
    public void getCalledMethod() throws Exception {
        FanInInstance fanInInstance = getFanInInstanceWithCallingMethods();
        String calledMethod = fanInInstance.getCalledMethod();
        Assert.assertEquals("CH.ifa.draw.standard.RelativeLocator.east()", calledMethod);
    }

    @Test
    public void getFanInValue() throws Exception {
        FanInInstance fanInInstance = getFanInInstanceWithCallingMethods();
        int fanInValue = fanInInstance.getFanInValue();
        Assert.assertEquals(5, fanInValue);
    }

//    @Test
//    public void getVectorInstanceWhenNoCallingMethods() throws Exception {
//        FanInInstance fanInInstanceWithNoCallingMethods = getFanInInstanceWithNoCallingMethods();
//        String vectorInstance = fanInInstanceWithNoCallingMethods.getVectorInstance(lineNumber);
//        Assert.assertEquals(VECTOR_INSTANCE_WHEN_NO_CALLING_METHODS, vectorInstance);
//    }
//
//    @Test
//    public void getVectorInstanceWhenCallingMethods() throws Exception {
//        FanInInstance fanInInstanceWithCallingMethods = getFanInInstanceWithCallingMethods();
//        String vectorInstance = fanInInstanceWithCallingMethods.getVectorInstance(lineNumber);
//        System.out.println(vectorInstance);
//        Assert.assertEquals(VECTOR_INSTANCE_WHEN_CALLING_METHODS, vectorInstance);
//    }

    @Test
    public void getClassName() throws Exception {
        FanInInstance fanInInstance = getFanInInstanceWithCallingMethods();
        String className = fanInInstance.getClassName("  > CH.ifa.draw.contrib.dnd.DragNDropTool.setCursor(IIQDrawingView;)");
        Assert.assertEquals("DragNDropTool", className);
    }

    private FanInInstance getFanInInstanceWithCallingMethods() {
        FanInInstance fanInInstance = new FanInInstance("CH.ifa.draw.standard.RelativeLocator.east() : 5");
        fanInInstance.addItem("CH.ifa.draw.contrib.dnd.DragNDropTool.setCursor(IIQDrawingView;)");
        fanInInstance.addItem("CH.ifa.draw.samples.pert.PertFigure.handles()");
        fanInInstance.addItem("CH.ifa.draw.samples.net.NodeFigure.createConnectors()");
        fanInInstance.addItem("CH.ifa.draw.standard.EastHandle.EastHandle(QFigure;)");
        fanInInstance.addItem("CH.ifa.draw.samples.net.NodeFigure.handles()");
        return fanInInstance;
    }

    private FanInInstance getFanInInstanceWithNoCallingMethods() {
        return new FanInInstance("CH.ifa.draw.test.contrib.PolygonFigureTest.testOutermostPoint() : 0");
    }

}