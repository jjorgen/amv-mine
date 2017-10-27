package org.nsu.dcis.amv.core.service;

import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.domain.ClusteringResults;
import org.springframework.stereotype.Service;

@Service
public class ClusteringService {
    private Logger log = Logger.getLogger(getClass().getName());

    public ClusteringResults
    mine() {
        log.info("Mine using clustering.");
        return null;
    }
}
