package org.nsu.dcis.amv.core.domain;

import com.github.javaparser.ast.MethodRepresentation;
import org.nsu.dcis.amv.core.util.Pair;
import com.github.javaparser.ast.CompilationUnit;

import static org.nsu.dcis.amv.core.domain.CodeCloneMiningResult.Type.*;

/**
 * Created by John Jorgensen on 3/7/2017.
 */
public class CodeCloneMiningResult {
    private int fromTopOfMethodLineCount;
    private int fromBottomMethodLineCount;
    private Pair<MethodRepresentation, MethodRepresentation> methodPair;

    public CodeCloneMiningResult() {
    }

    public enum Type {
        BEFORE_ADVICE_CANDIDATE, AFTER_ADVICE_CANDIDATE, AROUND_ADVICE_CANDIDATE, CLONE, EMPTY;
    };

    public Type getType() {
        if (isEmpty()) {
            return EMPTY;
        } else if (isClone()) {
            return CLONE;
        } else if (isBeforeAdviceCandidate()) {
            return BEFORE_ADVICE_CANDIDATE;
        } else if (isAfterAdviceCandidate()) {
            return AFTER_ADVICE_CANDIDATE;
        } else if (isAroundAdviceCandidate()) {
            return AROUND_ADVICE_CANDIDATE;
        } else {
            throw new IllegalStateException("Invalid code clone mining result");
        }
    }

    public CodeCloneMiningResult(MethodRepresentation compareFrom, MethodRepresentation compareTo, int fromTopOfMethodLineCount, int fromBottomMethodLineCount) {
        this.fromTopOfMethodLineCount = fromTopOfMethodLineCount;
        this.fromBottomMethodLineCount = fromBottomMethodLineCount;
        methodPair = new Pair<MethodRepresentation, MethodRepresentation>(compareFrom, compareTo);
    }

    public boolean isClone() {
        if (isEmpty()) return false;
        int firsMethodSize = ((MethodRepresentation)methodPair.getFirst()).getMethodTokens().size();
        int secondMethodSize = ((MethodRepresentation)methodPair.getSecond()).getMethodTokens().size();

        if (fromTopOfMethodLineCount == fromBottomMethodLineCount && firsMethodSize == secondMethodSize &&
            fromTopOfMethodLineCount == firsMethodSize && fromBottomMethodLineCount == firsMethodSize) {
            return true;
        }
        return false;
    }

    public boolean isBeforeAdviceCandidate() {
        if (isEmpty()) return false;
        if (fromTopOfMethodLineCount > 0 && fromBottomMethodLineCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAfterAdviceCandidate() {
        if (isEmpty()) return false;
        if (fromBottomMethodLineCount > 0 && fromTopOfMethodLineCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAroundAdviceCandidate() {
        if (isEmpty()) return false;
        if (fromTopOfMethodLineCount > 0 && fromBottomMethodLineCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        if (methodPair == null) return true;
        return false;
    }

    public Pair<MethodRepresentation, MethodRepresentation> getMethodPair() {
        return methodPair;
    }
}
