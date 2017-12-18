package org.nsu.dcis.amv.core.util;

/**
 * Created by jorgej2 on 12/17/2017.
 */
public class CodeCloneStatistics {

    private int emptyCount;
    private int cloneCount;
    private int beforeAdviceCount;
    private int aroundAdviceCount;
    private int afterAdviceCount;

    public CodeCloneStatistics(int emptyCount, int cloneCount, int beforeAdviceCount, int aroundAdviceCount, int afterAdviceCount) {
        this.emptyCount = emptyCount;
        this.cloneCount = cloneCount;
        this.beforeAdviceCount = beforeAdviceCount;
        this.aroundAdviceCount = aroundAdviceCount;
        this.afterAdviceCount = afterAdviceCount;
    }

    public int getEmptyCount() {
        return emptyCount;
    }

    public void setEmptyCount(int emptyCount) {
        this.emptyCount = emptyCount;
    }

    public int getCloneCount() {
        return cloneCount;
    }

    public void setCloneCount(int cloneCount) {
        this.cloneCount = cloneCount;
    }

    public int getBeforeAdviceCount() {
        return beforeAdviceCount;
    }

    public void setBeforeAdviceCount(int beforeAdviceCount) {
        this.beforeAdviceCount = beforeAdviceCount;
    }

    public int getAroundAdviceCount() {
        return aroundAdviceCount;
    }

    public void setAroundAdviceCount(int aroundAdviceCount) {
        this.aroundAdviceCount = aroundAdviceCount;
    }

    public int getAfterAdviceCount() {
        return afterAdviceCount;
    }

    public void setAfterAdviceCount(int afterAdviceCount) {
        this.afterAdviceCount = afterAdviceCount;
    }

    @Override
    public String toString() {
        return "CodeCloneStatistics{" +
                "emptyCount=" + emptyCount +
                ", cloneCount=" + cloneCount +
                ", beforeAdviceCount=" + beforeAdviceCount +
                ", aroundAdviceCount=" + aroundAdviceCount +
                ", afterAdviceCount=" + afterAdviceCount +
                '}';
    }
}
