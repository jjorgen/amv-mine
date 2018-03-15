package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.instrumentation.AmvConfigurationInstrumentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jorgej2 on 3/13/2018.
 */
@Service
public class CommonMiningService {

    @Autowired
    AmvConfigurationInstrumentation amvConfigurationInstrumentation;

    @Autowired
    private CodeCloneMiningService codeCloneMiningService;

    private Logger log = Logger.getLogger(getClass().getName());

    public List<MethodRepresentation> getAllMethodRepresentations() {
        log.info("getAllMethodRepresentations");
        return codeCloneMiningService.getAllMethodRepresentations("C:/work/0_NSU/CH/ifa",
                amvConfigurationInstrumentation.getExcludedDirectoryList(),
                amvConfigurationInstrumentation.getFileExtensions());
//        return codeCloneMiningService.getAllMethodRepresentations(amvConfigurationInstrumentation.getRootDir(),
//                amvConfigurationInstrumentation.getExcludedDirectoryList(),
//                amvConfigurationInstrumentation.getFileExtensions());
    }

}
