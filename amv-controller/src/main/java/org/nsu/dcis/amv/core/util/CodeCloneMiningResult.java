package org.nsu.dcis.amv.core.util;

import org.nsu.dcis.amv.core.domain.CodeCloneResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgej2 on 12/17/2017.
 */
public class CodeCloneMiningResult {
    private CodeCloneStatistics codeCloneStatistics;
    private List<CodeCloneResult> codeCloneResults;

    private List<CodeCloneResult> beforeAdviceCandidates = new ArrayList();
    private List<CodeCloneResult> afterAdviceCandidates = new ArrayList();;
    private List<CodeCloneResult> aroundAdviceCandidates = new ArrayList();;
    private List<CodeCloneResult> clone = new ArrayList();;

    public CodeCloneMiningResult(CodeCloneStatistics codeCloneStatistics, List<CodeCloneResult> codeCloneResults) {
        this.codeCloneStatistics = codeCloneStatistics;
        this.codeCloneResults = codeCloneResults;
        prepareAdviceCandidates(codeCloneResults);
    }

    private void prepareAdviceCandidates(List<CodeCloneResult> codeCloneResults) {
        for (CodeCloneResult codeCloneResult : codeCloneResults) {
            if (codeCloneResult.getType().equals(CodeCloneResult.Type.BEFORE_ADVICE_CANDIDATE)) {
                beforeAdviceCandidates.add(codeCloneResult);
            }
            else if (codeCloneResult.getType().equals(CodeCloneResult.Type.AFTER_ADVICE_CANDIDATE)) {
                afterAdviceCandidates.add(codeCloneResult);
            }
            else if (codeCloneResult.getType().equals(CodeCloneResult.Type.AROUND_ADVICE_CANDIDATE)) {
                aroundAdviceCandidates.add(codeCloneResult);
            }
            else if (codeCloneResult.getType().equals(CodeCloneResult.Type.CLONE)) {
                clone.add(codeCloneResult);
            }
        }
    }

    public CodeCloneStatistics getCodeCloneStatistics() {
        return codeCloneStatistics;
    }

    public void setCodeCloneStatistics(CodeCloneStatistics codeCloneStatistics) {
        this.codeCloneStatistics = codeCloneStatistics;
    }

    public List<CodeCloneResult> getCodeCloneResults() {
        return codeCloneResults;
    }

    public void setCodeCloneResults(List<CodeCloneResult> codeCloneResults) {
        this.codeCloneResults = codeCloneResults;
    }

    public List<CodeCloneResult> getBeforeAdviceCandidates() {
        return beforeAdviceCandidates;
    }

    public void setBeforeAdviceCandidates(List<CodeCloneResult> beforeAdviceCandidates) {
        this.beforeAdviceCandidates = beforeAdviceCandidates;
    }

    public List<CodeCloneResult> getAfterAdviceCandidates() {
        return afterAdviceCandidates;
    }

    public void setAfterAdviceCandidates(List<CodeCloneResult> afterAdviceCandidates) {
        this.afterAdviceCandidates = afterAdviceCandidates;
    }

    public List<CodeCloneResult> getAroundAdviceCandidates() {
        return aroundAdviceCandidates;
    }

    public void setAroundAdviceCandidates(List<CodeCloneResult> aroundAdviceCandidates) {
        this.aroundAdviceCandidates = aroundAdviceCandidates;
    }

    public List<CodeCloneResult> getClone() {
        return clone;
    }

    public void setClone(List<CodeCloneResult> clone) {
        this.clone = clone;
    }

    @Override
    public String toString() {
        return "CodeCloneMiningResult{" +
                "codeCloneStatistics=" + codeCloneStatistics +
                ", codeCloneResults=" + codeCloneResults +
                ", beforeAdviceCandidates=" + beforeAdviceCandidates +
                ", afterAdviceCandidates=" + afterAdviceCandidates +
                ", aroundAdviceCandidates=" + aroundAdviceCandidates +
                ", clone=" + clone +
                '}';
    }
}
