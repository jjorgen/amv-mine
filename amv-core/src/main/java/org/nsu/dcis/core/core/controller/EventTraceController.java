package org.nsu.dcis.core.core.controller;

import org.nsu.dcis.core.core.domain.EventTraceMiningResults;
import org.springframework.stereotype.Controller;

/**
 * Created by John Jorgensen on 3/7/2017.
 */
@Controller
public class EventTraceController {
    public EventTraceMiningResults getMiningResults() {
        return new EventTraceMiningResults();
    }
}
