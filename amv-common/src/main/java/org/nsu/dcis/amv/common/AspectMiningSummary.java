package org.nsu.dcis.amv.common;

import java.util.Arrays;

/**
 * Created by jorgej2 on 1/21/2018.
 */
public class AspectMiningSummary {
    int clusteringTotalCount;
    int cloningTotalCount;
    int eventTracesTotalCount;
    int overallTotalCount;

    public AspectMiningSummary() {
    }

    AspectMiningByCategory[] aspectMineByCategory;

    public AspectMiningSummary(int clusteringTotalCount, int cloningTotalCount, int eventTracesTotalCount) {
        this.clusteringTotalCount = clusteringTotalCount;
        this.cloningTotalCount = cloningTotalCount;
        this.eventTracesTotalCount = eventTracesTotalCount;
    }

    public void setAspectMineByCategory(AspectMiningByCategory[] aspectMineByCategory) {
        this.aspectMineByCategory = aspectMineByCategory;
    }

    public AspectMiningByCategory[] getAspectMineByCategory() {
        return aspectMineByCategory;
    }

    public int getClusteringTotalCount() {
        return clusteringTotalCount;
    }

    public void setClusteringTotalCount(int clusteringTotalCount) {
        this.clusteringTotalCount = clusteringTotalCount;
    }

    public int getCloningTotalCount() {
        return cloningTotalCount;
    }

    public void setCloningTotalCount(int cloningTotalCount) {
        this.cloningTotalCount = cloningTotalCount;
    }

    public int getEventTracesTotalCount() {
        return eventTracesTotalCount;
    }

    public void setEventTracesTotalCount(int eventTracesTotalCount) {
        this.eventTracesTotalCount = eventTracesTotalCount;
    }

    public int getOverallTotalCount() {
        return overallTotalCount;
    }

    public void setAspectMiningByCategory(AspectMiningByCategory[] aspectMineByCategory) {
        this.aspectMineByCategory = aspectMineByCategory;
        calculateTotalNumberOfCrossCuttingConcerns();
    }

    private void calculateTotalNumberOfCrossCuttingConcerns() {
        int totalClusteringCount = 0;
        int totalCloningCount = 0;
        int totalEventTracingCount = 0;
        for (int i = 0; i < aspectMineByCategory.length; i++) {
            totalClusteringCount += aspectMineByCategory[i].getClusteringCount();
            totalCloningCount += aspectMineByCategory[i].getCloningCount();
            totalEventTracingCount += aspectMineByCategory[i].getEventTracesCount();
        }
        overallTotalCount = totalClusteringCount + totalCloningCount + totalEventTracingCount;
    }

    @Override
    public String toString() {
        return "AspectMiningSummary{" +
                "clusteringTotalCount=" + clusteringTotalCount +
                ", cloningTotalCount=" + cloningTotalCount +
                ", eventTracesTotalCount=" + eventTracesTotalCount +
                ", overallTotalCount=" + overallTotalCount +
                ", aspectMineByCategory=" + Arrays.toString(aspectMineByCategory) +
                '}';
    }
}
