package org.nsu.dcis.core.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PdgFileParser {

    @Autowired
    FileUtil fileUtil;

    @Autowired
    JavaFile javaFile;

    private Logger log = Logger.getLogger(getClass().getName());

    public void getPdgs(String absolutePath, PdgWrapper pdgWrapper) {
        Pdg pdg = new Pdg();
        pdgWrapper.add(pdg);

        log.info(absolutePath);
        javaFile.read(absolutePath);
    }
}
