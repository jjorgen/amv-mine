package org.nsu.dcis.amv.common;

import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jorgej2 on 6/20/2018.
 */
@XmlRootElement(name="aspectMiningDetailResult")
public class AspectMiningDetailResult {

    private String crossCuttingConcernCategory;
    private String crossCuttingConcernCategoryDisplayName;
    private String calledMethod[];

    private Logger log = Logger.getLogger(getClass().getName());

    public String getCrossCuttingConcernCategory() {
        return crossCuttingConcernCategory;
    }

    public void setCrossCuttingConcernCategory(String crossCuttingConcernCategory) {
        this.crossCuttingConcernCategory = crossCuttingConcernCategory;
    }

    public String getCrossCuttingConcernCategoryDisplayName() {
        return crossCuttingConcernCategoryDisplayName;
    }

    public void setCrossCuttingConcernCategoryDisplayName(String crossCuttingConcernCategoryDisplayName) {
        this.crossCuttingConcernCategoryDisplayName = crossCuttingConcernCategoryDisplayName;
    }

    public String[] getCalledMethod() {
        return calledMethod;
    }

    public void setCalledMethod(String[] calledMethod) {
        this.calledMethod = calledMethod;
    }

    @Override
    public String toString() {
        return "AspectMiningDetailResult{" +
                "crossCuttingConcernCategory='" + crossCuttingConcernCategory + '\'' +
                ", crossCuttingConcernCategoryDisplayName='" + crossCuttingConcernCategoryDisplayName + '\'' +
                '}';
    }
}
