package org.nsu.dcis.core.core.controller;

import org.nsu.dcis.core.core.domain.ClusteringMiningResults;
import org.springframework.stereotype.Controller;

/**
 * Created by John Jorgensen on 3/7/2017.
 */
@Controller
public class ClusteringController {

    public ClusteringMiningResults getMiningResults() {
        return new ClusteringMiningResults();
    }
}
