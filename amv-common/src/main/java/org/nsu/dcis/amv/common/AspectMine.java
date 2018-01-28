package org.nsu.dcis.amv.common;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Created by jorgej2 on 9/10/2017.
 */
@XmlRootElement(name="aspectMine")
public class AspectMine {

    String CLUSTERING_ALGORITHM = "clusteringAlgorithm";
    String VECTOR_SPACE_MODEL = "vectorSpaceModel";
    String ORDERED_METHOD_CALL	= "orderedMethodCall";
    String CODE_CLONE = "codeClone";
    String UNIQUE_CLASS_FAN_IN = "uniqueClassFanIn";
    String CALLS_IN_CLONES = "callsInClones";
    String CALLS_AT_BEGINNING_AND_END_OF_METHOD = "callsAtBeginningAndEndOfMethod";
    String EVENT_AS_PARAMETER = "eventAsParameter";
    String SINGLETON =	"singleton";
    String OBSERVER = "observer";
    String CHAIN_OF_RESPONSIBILITY = "chainOfResponsibility";
    String CROSS_CUTTING_CONCERN_AS_INTERFACE = "crossCuttingConcernAsInterface";

    private String clusteringAlgorithm;
    private String vectorSpaceModel;

    // Cross Cutting Concern Category
    private String orderedMethodCall;
    private String codeClone;
    private String uniqueClassFanIn;
    private String callsInClones;
    private String callsAtBeginningAndEndOfMethod;
    private String eventAsParameter;
    private String singleton;
    private String observer;
    private String chainOfResponsibility;
    private String crossCuttingConcernAsInterface;

    public String getClusteringAlgorithm() {
        return clusteringAlgorithm;
    }

    public void setClusteringAlgorithm(String clusteringAlgorithm) {
        this.clusteringAlgorithm = clusteringAlgorithm;
    }

    public String getVectorSpaceModel() {
        return vectorSpaceModel;
    }

    public void setVectorSpaceModel(String vectorSpaceModel) {
        this.vectorSpaceModel = vectorSpaceModel;
    }

    public String getOrderedMethodCall() {
        return orderedMethodCall;
    }

    public void setOrderedMethodCall(String orderedMethodCall) {
        this.orderedMethodCall = orderedMethodCall;
    }

    public String getCodeClone() {
        return codeClone;
    }

    public void setCodeClone(String codeClone) {
        this.codeClone = codeClone;
    }

    public String getUniqueClassFanIn() {
        return uniqueClassFanIn;
    }

    public void setUniqueClassFanIn(String uniqueClassFanIn) {
        this.uniqueClassFanIn = uniqueClassFanIn;
    }

    public String getCallsInClones() {
        return callsInClones;
    }

    public void setCallsInClones(String callsInClones) {
        this.callsInClones = callsInClones;
    }

    public String getCallsAtBeginningAndEndOfMethod() {
        return callsAtBeginningAndEndOfMethod;
    }

    public void setCallsAtBeginningAndEndOfMethod(String callsAtBeginningAndEndOfMethod) {
        this.callsAtBeginningAndEndOfMethod = callsAtBeginningAndEndOfMethod;
    }

    public String getEventAsParameter() {
        return eventAsParameter;
    }

    public void setEventAsParameter(String eventAsParameter) {
        this.eventAsParameter = eventAsParameter;
    }

    public String getSingleton() {
        return singleton;
    }

    public void setSingleton(String singleton) {
        this.singleton = singleton;
    }

    public String getObserver() {
        return observer;
    }

    public void setObserver(String observer) {
        this.observer = observer;
    }

    public String getChainOfResponsibility() {
        return chainOfResponsibility;
    }

    public void setChainOfResponsibility(String chainOfResponsibility) {
        this.chainOfResponsibility = chainOfResponsibility;
    }

    public String getCrossCuttingConcernAsInterface() {
        return crossCuttingConcernAsInterface;
    }

    public void setCrossCuttingConcernAsInterface(String crossCuttingConcernAsInterface) {
        this.crossCuttingConcernAsInterface = crossCuttingConcernAsInterface;
    }

    public Map<String, String> stringified() {
        Map<String, String> stringifiedAspectMap = new HashMap<String, String>();
        stringifiedAspectMap.put(CLUSTERING_ALGORITHM, getClusteringAlgorithm());
        stringifiedAspectMap.put(VECTOR_SPACE_MODEL, getVectorSpaceModel());
        stringifiedAspectMap.put(ORDERED_METHOD_CALL, getOrderedMethodCall());
        stringifiedAspectMap.put(CODE_CLONE, getCodeClone());
        stringifiedAspectMap.put(UNIQUE_CLASS_FAN_IN, getUniqueClassFanIn());
        stringifiedAspectMap.put(CALLS_IN_CLONES, getCallsInClones());
        stringifiedAspectMap.put(CALLS_AT_BEGINNING_AND_END_OF_METHOD, getCallsAtBeginningAndEndOfMethod());
        stringifiedAspectMap.put(EVENT_AS_PARAMETER, getEventAsParameter());
        stringifiedAspectMap.put(SINGLETON, getSingleton());
        stringifiedAspectMap.put(OBSERVER, getObserver());
        stringifiedAspectMap.put(CHAIN_OF_RESPONSIBILITY, getChainOfResponsibility());
        stringifiedAspectMap.put(CROSS_CUTTING_CONCERN_AS_INTERFACE, getCrossCuttingConcernAsInterface());
        return stringifiedAspectMap;
    }

    @Override
    public String toString() {
        return "org.nsu.dcis.amv.common.AspectMine{" +
                "clusteringAlgorithm='" + clusteringAlgorithm + '\'' +
                ", vectorSpaceModel='" + vectorSpaceModel + '\'' +
                ", orderedMethodCall='" + orderedMethodCall + '\'' +
                ", codeClone='" + codeClone + '\'' +
                ", uniqueClassFanIn='" + uniqueClassFanIn + '\'' +
                ", callsInClones='" + callsInClones + '\'' +
                ", callsAtBeginningAndEndOfMethod='" + callsAtBeginningAndEndOfMethod + '\'' +
                ", eventAsParameter='" + eventAsParameter + '\'' +
                ", singleton='" + singleton + '\'' +
                ", observer='" + observer + '\'' +
                ", chainOfResponsibility='" + chainOfResponsibility + '\'' +
                ", crossCuttingConcernAsInterface='" + crossCuttingConcernAsInterface + '\'' +
                '}';
    }
}
