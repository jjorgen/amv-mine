package org.nsu.dcis.amv.common;

import java.util.Arrays;

/**
 * Created by jorgej2 on 1/21/2018.
 */
public class AspectMiningSummary {
    int clusteringTotalCount;
    int cloningTotalCount;
    int eventTracesTotalCount;

    public AspectMiningSummary() {
    }

    AspectMiningByCategory[] aspectMineByCategory;

    public AspectMiningSummary(int clusteringTotalCount, int cloningTotalCount, int eventTracesTotalCount) {
        this.clusteringTotalCount = clusteringTotalCount;
        this.cloningTotalCount = cloningTotalCount;
        this.eventTracesTotalCount = eventTracesTotalCount;
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

    public AspectMiningByCategory[] getAspectMineByCategory() {
        return aspectMineByCategory;
    }

    public void setAspectMiningByCategory(AspectMiningByCategory[] aspectMineByCategory) {
        this.aspectMineByCategory = aspectMineByCategory;
    }

    public void setAspectMineByCategory(AspectMiningByCategory[] aspectMineByCategory) {
        this.aspectMineByCategory = aspectMineByCategory;
    }

    @Override
    public String toString() {
        return "AspectMiningSummary{" +
                "clusteringTotalCount=" + clusteringTotalCount +
                ", cloningTotalCount=" + cloningTotalCount +
                ", eventTracesTotalCount=" + eventTracesTotalCount +
                ", aspectMineByCategory=" + Arrays.toString(aspectMineByCategory) +
                '}';
    }
}
