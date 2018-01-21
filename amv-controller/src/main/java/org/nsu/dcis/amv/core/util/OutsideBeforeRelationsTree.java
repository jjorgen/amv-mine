package org.nsu.dcis.amv.core.util;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgej2 on 1/3/2018.
 */
public class OutsideBeforeRelationsTree {

    private Logger log = Logger.getLogger(getClass().getName());
    private TraceMethod nextRootTraceMethod = null;
    private List<TraceMethod> traceMethodsInRelation = new ArrayList<>();

    public OutsideBeforeRelationsTree() {
    }

    public OutsideBeforeRelationsTree(TraceMethod nextRootTraceMethod) {
        this.nextRootTraceMethod = nextRootTraceMethod;
    }

    public OutsideBeforeRelationsTree(ArrayList<TraceMethod> traceMethodsInRelation, TraceMethod nextRootTraceMethod) {
        this.traceMethodsInRelation = traceMethodsInRelation;
        this.nextRootTraceMethod = nextRootTraceMethod;
    }

    public boolean isValid() {
        if (isValidRelationStartingWithInsideFirst()) {
            return true;
        } else if (isValidRelationStartingWithOutsideFirst()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidRelationStartingWithInsideFirst() {
        if (traceMethodsInRelation.size() < 3) {
            return false;
        }
        int firstTraceMethodLevel = traceMethodsInRelation.get(0).getLevel();
        int secondTraceMethodLevel = traceMethodsInRelation.get(1).getLevel();
        int thirdTraceMethodLevel = traceMethodsInRelation.get(2).getLevel();

        if (firstTraceMethodLevel + 1 == secondTraceMethodLevel &&
                firstTraceMethodLevel + 1 == thirdTraceMethodLevel) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidRelationStartingWithOutsideFirst() {
        if (traceMethodsInRelation.size() < 3) {
            return false;
        }
        int firstTraceMethodLevel = traceMethodsInRelation.get(0).getLevel();
        int secondTraceMethodLevel = traceMethodsInRelation.get(1).getLevel();
        int thirdTraceMethodLevel = traceMethodsInRelation.get(2).getLevel();

        if (firstTraceMethodLevel  == secondTraceMethodLevel &&
            firstTraceMethodLevel  == thirdTraceMethodLevel) {
            return true;
        } else {
            return false;
        }
    }

    public boolean willStillBeValidWhenAdding(TraceMethod traceMethod) {
        return (traceMethodsInRelation.get(1).getLevel() == traceMethod.getLevel());
    }

    public void rebuildWith(TraceMethod traceMethod) {
        if (isDuplicateMethod(traceMethod)) {
            return;
        }
        if (traceMethodsInRelation.size() > 2) {
            traceMethodsInRelation.remove(0);
        }
        traceMethodsInRelation.add(traceMethod);
    }

    private boolean isDuplicateMethod(TraceMethod traceMethod) {
        if (traceMethodsInRelation.size() > 1) {
            if (traceMethod.equals(traceMethodsInRelation.get(traceMethodsInRelation.size() -1))) {
                return true;
            }
        }
        return false;
    }

    public void add(TraceMethod traceMethod) {
        traceMethodsInRelation.add(traceMethod);
    }


    public boolean containsTestRelation() {
        for (TraceMethod traceMethod : traceMethodsInRelation) {
            if (traceMethod.isTestMethod()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the root trace method for the next inside relations tree.
     *
     * @return root trace method
     */
    public TraceMethod getRootForNextRelationTree() {
        return nextRootTraceMethod;
    }

    public boolean isLast() {
        return nextRootTraceMethod.isEmpty();
    }

    public List<Relation> getAllOutsideBeforeRelationsInATree() {
        TraceMethod[] traceMethods = traceMethodsInRelation.toArray(new TraceMethod[0]);
        List<Relation> relations = new ArrayList<>();
        int topNodeInTree = 0;
        for (int i = 0; i < traceMethods.length; i++ ) {
            // At the last line in the tree. No more relations can be extracted.
            if (i + 1 == traceMethods.length) {
                // Do nothing.
            }
            // There is this line and at least one more line in tree. Extract relation with no context.
            else if (topNodeInTree == i && i + 2 <= traceMethods.length) {
                // Do nothing only two more lines left in tree
//                relations.add(new Relation(traceMethods[i], traceMethods[i + 1]));
            }
            // There is this line, a prior line, and at least one more line in the tree. Extract relation with context.
            else if ((i - 1) >= topNodeInTree && (i + 1) <= traceMethods.length) {
                relations.add(new Relation(traceMethods[i - 1],   // context
                                           traceMethods[i],       // First method in relation
                                           traceMethods[i + 1])); // Second method in relation
            }
        }
        return relations;
    }

    public void print() {
        for (TraceMethod traceMethod : traceMethodsInRelation) {
            log.info(traceMethod);
        }
    }

}
