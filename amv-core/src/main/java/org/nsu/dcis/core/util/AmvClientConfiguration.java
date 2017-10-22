package org.nsu.dcis.core.util;

import org.springframework.stereotype.Component;

public class AmvClientConfiguration {
    private String executionTracesMining;
    private String cloneDetectionMining;
    private String clusteringMining;
    private String jhotdrawSourceRoot;
    private String jhotdrawExcludedDirectoryList;

    public void setExecutionTracesMining(String executionTracesMining) {
        this.executionTracesMining = executionTracesMining;
    }

    public String getExecutionTracesMining() {
        return executionTracesMining;
    }

    public void setCloneDetectionMining(String cloneDetectionMining) {
        this.cloneDetectionMining = cloneDetectionMining;
    }

    public String getCloneDetectionMining() {
        return cloneDetectionMining;
    }

    public void setClusteringMining(String clusteringMining) {
        this.clusteringMining = clusteringMining;
    }

    public String getClusteringMining() {
        return clusteringMining;
    }

    public void setJhotdrawSourceRoot(String jhotdrawSourceRoot) {
        this.jhotdrawSourceRoot = jhotdrawSourceRoot;
    }

    public String getJhotdrawSourceRoot() {
        return jhotdrawSourceRoot;
    }

    public void setJhotdrawExcludedDirectoryList(String jhotdrawExcludedDirectoryList) {
        this.jhotdrawExcludedDirectoryList = jhotdrawExcludedDirectoryList;
    }

    public String getJhotdrawExcludedDirectoryList() {
        return jhotdrawExcludedDirectoryList;
    }

    @Override
    public String toString() {
        return "AmvClientConfiguration{" +
                "executionTracesMining='" + executionTracesMining + '\'' +
                ", cloneDetectionMining='" + cloneDetectionMining + '\'' +
                ", clusteringMining='" + clusteringMining + '\'' +
                ", jhotdrawSourceRoot='" + jhotdrawSourceRoot + '\'' +
                ", jhotdrawExcludedDirectoryList='" + jhotdrawExcludedDirectoryList + '\'' +
                '}';
    }
}
