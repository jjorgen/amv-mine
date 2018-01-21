package org.nsu.dcis.amv.core.controller;

import org.springframework.stereotype.Controller;

@Controller
public class AspectMiningController {

//    @Autowired
//    CloneDetectionService cloneDetectionService;
//
//    @Autowired
//    EventTracesService eventTracesService;
//
//    @Autowired
//    ClusteringService clusteringService;
//
//    @Autowired
//    SynthesizingService synthesizingService;
//
//    public void mineForAspects() {
//
//        CloneDetectionResults cloneDetectionResults = cloneDetectionService.mineForAspects();
//        EventTracesResults eventTracesResults = eventTracesService.mineForAspects();
//        ClusteringResults clusteringResults = clusteringService.mineForAspects();
//        AspectMiningResults aspectMiningResults = synthesizingService.synthesize(cloneDetectionResults,
//                                                                                 eventTracesResults,
//                                                                                 clusteringResults);
//        save(aspectMiningResults);
//    }
//
//    private void save(AspectMiningResults aspectMiningResults) {
//    }
//
//    public void start(String[] args) {
//        switch (args[1]) {
//            case "CloneDetection" :
//                CloneDetectionResults cloneDetectionResults = cloneDetectionService.mineForAspects();
//                break;
//            case "EventTracing" :
//                EventTracesResults eventTracesResults = eventTracesService.mineForAspects();
//                break;
//            case "Clustering" :
//                ClusteringResults clusteringResults = clusteringService.mineForAspects();
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid value for mining method: " + args[1]);
//        }
//    }
}
