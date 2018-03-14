package org.nsu.dcis.amv.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgej2 on 3/11/2018.
 */
public class AspectMiningRequest {

    public enum  AspectMiningCategory {
        ORDERED_METHOD_CALL,
        CODE_CLONE,
        UNIQUE_CLASS_FAN_IN,
        CALLS_IN_CLONES,
        CALLS_AT_BEGINNING_AND_END_OF_METHODS,
        EVENT_AS_PARAMETER,
        SINGLETON,
        OBSERVER,
        CHAIN_OF_RESPONSIBILITY,
        CROSS_CUTTING_CONCERN_AS_INTERFACE
    }

//    public static int ORDERED_METHOD_CALL = 1;
//    public static int CODE_CLONE = 2;
//    public static int UNIQUE_CLASS_FAN_IN = 3;
//    public static int CALLS_IN_CLONES = 4;
//    public static int CALLS_AT_BEGINNING_AND_END_OF_METHODS = 5;
//    public static int EVENT_AS_PARAMETER = 6;
//    public static int SINGLETON = 7;
//    public static int OBSERVER = 8;
//    public static int CHAIN_OF_RESPONSIBILITY = 9;
//    public static int CROSS_CUTTING_CONCERN_AS_INTERFACE = 10;

    private List<AspectMiningCategory> aspectMiningCategories = new ArrayList<>();

    public void addAspectMiningCategory(AspectMiningCategory aspectMiningCategory) {
        aspectMiningCategories.add(aspectMiningCategory);
    }

    public List<AspectMiningCategory> getAspectMiningCategories() {
        return aspectMiningCategories;
    }

    @Override
    public String toString() {
        return "AspectMiningRequest{" +
                "aspectMiningCategories=" + aspectMiningCategories +
                '}';
    }
}
