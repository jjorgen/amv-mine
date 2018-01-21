package org.nsu.dcis.amv.core.util.enumeration;

public enum AspectMiningType {

    CLUSTERING(1),
    EVENT_TRACES(2),
    CLONE_DETECTION(3);

    private final Integer seqId;

    AspectMiningType(Integer seqId) {
        this.seqId = seqId;
    }

    public Integer getSeqId() {
        return seqId;
    }
}
