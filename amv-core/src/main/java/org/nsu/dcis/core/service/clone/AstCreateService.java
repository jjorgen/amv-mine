package org.nsu.dcis.core.service.clone;

import org.nsu.dcis.core.domain.clone.MethodAst;
import org.nsu.dcis.core.util.FileScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AstCreateService {

    @Autowired
    FileScanner fileScanner;

    public List<MethodAst> getMethodAstList() {
        List<MethodAst> allMethodAstList = new ArrayList<>();

        List<String> fileList = fileScanner.scan();
        List<MethodAst> methodAstList = new ArrayList<>();

//        for (String fileName : fileList) {
//            FileAst fileAst = new FileAst(fileName);
//            fileAst.getMethodAstList();
//            allMethodAstList.addAll(fileAst.getMethodAstList());
//        }
        return methodAstList;
    }
}
