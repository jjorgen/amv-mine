package org.nsu.dcis.amv.core.controller;

import org.nsu.dcis.amv.core.service.CodeCloneMiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * Created by jorgej2 on 12/16/2017.
 */
@Controller
public class CodeCloneController {

    @Autowired
    CodeCloneMiningService codeCloneMiningService;

    public void mineForAspects() {
        codeCloneMiningService.mineForAspects();
    }
}
