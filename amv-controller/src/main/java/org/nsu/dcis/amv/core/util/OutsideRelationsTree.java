package org.nsu.dcis.amv.core.util;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorgej2 on 12/28/2017.
 */
public class OutsideRelationsTree {
    private Logger log = Logger.getLogger(getClass().getName());

    private TraceMethod nextRootTraceMethod = null;
    private ArrayList<TraceMethod> traceMethodsInRelation = new ArrayList<>();

    public OutsideRelationsTree(TraceMethod nextRootTraceMethod) {
        this.nextRootTraceMethod = nextRootTraceMethod;
    }

    public OutsideRelationsTree(ArrayList<TraceMethod> traceMethodsInRelation, TraceMethod nextRootTraceMethod) {
        this.traceMethodsInRelation = traceMethodsInRelation;
        this.nextRootTraceMethod = nextRootTraceMethod;
    }

    public void print() {
        for (TraceMethod traceMethod : traceMethodsInRelation) {
            log.info(traceMethod);
        }
    }

    /**
     * Get the root trace method for the next inside relations tree.
     *
     * @return root trace method
     */
    public TraceMethod getRootForNextRelationTree() {
        return nextRootTraceMethod;
    }

    public boolean isEmpty() {
        return traceMethodsInRelation.size() < 2;
    }

    public boolean isLast() {
        return nextRootTraceMethod.isEmpty();
    }

    public List<Relation> getAllOutsideRelationsInATree() {
        TraceMethod[] traceMethods = traceMethodsInRelation.toArray(new TraceMethod[0]);
        List<Relation> relations = new ArrayList<>();
        int topNodeInTree = 0;
        for (int i = 0; i < traceMethods.length; i++ ) {
            TraceMethod traceMethod = traceMethods[i];
            if (traceMethods[i].isTestMethod()) {
                ++topNodeInTree;
            }
            else {
                // At the last line in the tree. No more relations can be extracted.
                if (i + 1 == traceMethods.length) {
                    // Do nothing.
                }
                // There is this line and at least one more line in tree. Extract relation with no context.
                else if (topNodeInTree == i && i + 2 <= traceMethods.length) {
                    relations.add(new Relation(traceMethods[i], traceMethods[i + 1]));
                }
                // There is this line, a prior line, and at least one more line in the tree. Extract relation with context.
                else if ((i - 1) >= topNodeInTree && (i + 1) <= traceMethods.length) {
                    relations.add(new Relation(traceMethods[i - 1], traceMethods[i], traceMethods[i + 1]));
                }
            }
        }
        return relations;
    }


}
