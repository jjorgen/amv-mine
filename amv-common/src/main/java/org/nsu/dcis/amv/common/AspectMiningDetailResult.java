package org.nsu.dcis.amv.common;

import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

/**
 * Created by jorgej2 on 6/20/2018.
 */
@XmlRootElement(name="aspectMiningDetailResult")
public class AspectMiningDetailResult {

    private String crossCuttingConcernCategoryDisplayName;
    private String leftSideHeading;
    private String rightSideHeading;
//    private String callingMethod;
    private CalledMethod[] calledMethod;
    private String[] callingMethod;
    private String crossCuttingConcernCategory;

    private Logger log = Logger.getLogger(getClass().getName());

    public String getCrossCuttingConcernCategoryDisplayName() {
        return crossCuttingConcernCategoryDisplayName;
    }

    public void setCrossCuttingConcernCategoryDisplayName(String crossCuttingConcernCategoryDisplayName) {
        this.crossCuttingConcernCategoryDisplayName = crossCuttingConcernCategoryDisplayName;
    }

    public String getLeftSideHeading() {
        return leftSideHeading;
    }

    public void setLeftSideHeading(String leftSideHeading) {
        this.leftSideHeading = leftSideHeading;
    }

    public String getRightSideHeading() {
        return rightSideHeading;
    }

    public void setRightSideHeading(String rightSideHeading) {
        this.rightSideHeading = rightSideHeading;
    }

//    public String getCallingMethod() {
//        return callingMethod;
//    }
//
//    public void setCallingMethod(String callingMethod) {
//        this.callingMethod = callingMethod;
//    }

    public String getCrossCuttingConcernCategory() {
        return crossCuttingConcernCategory;
    }

    public void setCrossCuttingConcernCategory(String crossCuttingConcernCategory) {
        this.crossCuttingConcernCategory = crossCuttingConcernCategory;
    }

    public CalledMethod[] getCalledMethod() {
        return calledMethod;
    }

    public void setCalledMethod(CalledMethod[] calledMethod) {
        this.calledMethod = calledMethod;
    }

    public String[] getCallingMethod() {
        return callingMethod;
    }

    public void setCallingMethods(String[] callingMethod) {
        this.callingMethod = callingMethod;
    }

    @Override
    public String toString() {
        return "AspectMiningDetailResult{" +
                "crossCuttingConcernCategoryDisplayName='" + crossCuttingConcernCategoryDisplayName + '\'' +
                ", leftSideHeading='" + leftSideHeading + '\'' +
                ", rightSideHeading='" + rightSideHeading + '\'' +
                ", callingMethod='" + callingMethod + '\'' +
                ", calledMethod=" + Arrays.toString(calledMethod) +
                ", callingMethods=" + Arrays.toString(callingMethod) +
                ", crossCuttingConcernCategory='" + crossCuttingConcernCategory + '\'' +
                '}';
    }
}
