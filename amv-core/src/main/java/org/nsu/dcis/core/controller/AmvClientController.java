package org.nsu.dcis.core.controller;

import org.springframework.beans.factory.annotation.Autowired;

public class AmvClientController {

    @Autowired
    AspectMiningController aspectMiningController;


    public void start(String[] args) {
//        if ("AspectMining".equalsIgnoreCase(AmvClient.ASPECT_MINING_USE_CASE)) {
//            aspectMiningController.start(args);
//        } else {
//            throw new IllegalArgumentException("Invalid use case: " + args[0]);
//        }
    }
}
