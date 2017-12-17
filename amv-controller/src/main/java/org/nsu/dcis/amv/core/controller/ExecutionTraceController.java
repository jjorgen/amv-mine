package org.nsu.dcis.amv.core.controller;

import org.nsu.dcis.amv.core.domain.EventTracesResults;
import org.nsu.dcis.amv.core.service.ExecutionTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by jorgej2 on 12/3/2017.
 */
@Controller
public class ExecutionTraceController {

    @Autowired
    ExecutionTraceService executionTraceService;

    private String PATH_TO_METHOD_EXECUTION_TRACE_LOG = "C:/log/method_execution_trace.log";

    public void mineForAspects() {

        EventTracesResults eventTracesResults = executionTraceService.mineForAspects(PATH_TO_METHOD_EXECUTION_TRACE_LOG);

    }
}
