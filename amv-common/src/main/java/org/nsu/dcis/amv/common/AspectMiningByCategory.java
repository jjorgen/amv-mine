package org.nsu.dcis.amv.common;

/**
 * Created by jorgej2 on 1/21/2018.
 */
public class AspectMiningByCategory {
    String crossCuttingConcernCategory;
    String displayName;
    int clusteringCount;
    int cloningCount;
    int eventTracesCount;

    AspectMiningByCategory() {
    }

    public AspectMiningByCategory(String crossCuttingConcernCategory, String displayName,
                                  int clusteringCount, int cloningCount, int eventTracesCount) {
        this.displayName = displayName;
        this.crossCuttingConcernCategory = crossCuttingConcernCategory;
        this.clusteringCount = clusteringCount;
        this.cloningCount = cloningCount;
        this.eventTracesCount = eventTracesCount;
    }

    public String getCrossCuttingConcernCategory() {
        return crossCuttingConcernCategory;
    }

    public void setCrossCuttingConcernCategory(String crossCuttingConcernCategory) {
        this.crossCuttingConcernCategory = crossCuttingConcernCategory;
    }

    public int getClusteringCount() {
        return clusteringCount;
    }

    public void setClusteringCount(int clusteringCount) {
        this.clusteringCount = clusteringCount;
    }

    public int getCloningCount() {
        return cloningCount;
    }

    public void setCloningCount(int cloningCount) {
        this.cloningCount = cloningCount;
    }

    public int getEventTracesCount() {
        return eventTracesCount;
    }

    public void setEventTracesCount(int eventTracesCount) {
        this.eventTracesCount = eventTracesCount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "AspectMiningByCategory{" +
                "crossCuttingConcernCategory='" + crossCuttingConcernCategory + '\'' +
                ", clusteringCount=" + clusteringCount +
                ", cloningCount=" + cloningCount +
                ", eventTracesCount=" + eventTracesCount +
                '}';
    }
}
