package org.nsu.dcis.core.entity;

import org.nsu.dcis.core.util.enumeration.AspectMiningType;

public class AspectMiningConfigurationEntity {

    Integer seqId;
    String name;
    AspectMiningType aspectMiningType;

    public AspectMiningConfigurationEntity() {
    }

    public AspectMiningConfigurationEntity(AspectMiningType aspectMiningType) {
        this.aspectMiningType = aspectMiningType;
    }

    public AspectMiningType getAspectMiningType() {
        return aspectMiningType;
    }

    public void setAspectMiningType(AspectMiningType aspectMiningType) {
        this.aspectMiningType = aspectMiningType;
    }

    public Integer getSeqId() {
        return seqId;
    }

    public void setSeqId(Integer seqId) {
        this.seqId = seqId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AspectMiningConfigurationEntity{" +
                "seqId=" + seqId +
                ", name='" + name + '\'' +
                ", aspectMiningType=" + aspectMiningType +
                '}';
    }
}
