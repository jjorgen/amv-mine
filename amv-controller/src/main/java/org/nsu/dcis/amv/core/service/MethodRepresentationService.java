package org.nsu.dcis.amv.core.service;

import com.github.javaparser.ast.MethodRepresentation;
import com.github.javaparser.extend.CompilationUnitWrapper;
import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.domain.FileScanResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John Jorgensen on 3/12/2017.
 */
@Service
public class MethodRepresentationService {

    private Object methodRepresentations;
    private CompilationUnitWrapper compilationUnitWrapper;
    private Logger log = Logger.getLogger(getClass().getName());

    public List<MethodRepresentation> getMethodRepresentations(FileScanResult fileScanResult) {
        List<MethodRepresentation> methodRepresentations = new ArrayList<>();

        for (String filePath : fileScanResult.getFileList()) {
            compilationUnitWrapper = new CompilationUnitWrapper(filePath);
            methodRepresentations.addAll(compilationUnitWrapper.getMethodRepresentations());
//            for (MethodRepresentation methodRepresentation : methodRepresentations) {
//                log.info("****** File Path: " + methodRepresentation.getFilePath());
//                log.info("****** Method Name: " + methodRepresentation.getMethodName());
//                log.info("methodRepresentation: " + methodRepresentation);
//                log.info(methodRepresentation.getMethodTokens());
//            }
        }
        return methodRepresentations;
    }
}
