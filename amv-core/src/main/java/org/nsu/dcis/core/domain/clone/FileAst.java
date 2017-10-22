package org.nsu.dcis.core.domain.clone;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import org.apache.log4j.Logger;
import org.nsu.dcis.core.exception.AspectCloneException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class FileAst {

    private CompilationUnit compilationUnit;
    private Logger log = Logger.getLogger(getClass().getName());

    public FileAst(String fileName) {

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new AspectCloneException("An error occurred when trying to read Java File from which " +
                                           "to create FileAst, file name '" + fileName + "'.");
        }

        try {
            compilationUnit = JavaParser.parse(fileInputStream);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                throw new AspectCloneException("An error occurred when closing Java File from which FileAst was created," +
                                               " file name '" + fileName + "' was created.");
            }
        }
    }




//    public List<MethodAst> getMethodAstList() {
//        extractMethodAstList();
//        return null;
//    }

//    private void extractMethodAstList() {
//
//        NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
//        for (TypeDeclaration type : types) {
////            log.info(type);
//            List<Node> childrenNodes = type.getChildrenNodes();
//            for (Node node : childrenNodes) {
//                log.info(node);
//            }
//
//
//            List<MethodDeclaration> methods = type.getMethods();
//            for (MethodDeclaration methodDeclaration : methods) {
//                log.info(methodDeclaration);
//            }
//        }
//    }

    public String getStringifiedAst() {
        return compilationUnit.toString();
    }

    public void exploreNodeHierarchy() {
        traverseTree(compilationUnit.getChildrenNodes());
    }

    private void traverseTree(List<Node> childrenNodes) {
        for (Node node : childrenNodes) {

//            if (NodeList.class.isInstance(node)) {
//                Iterator iterator = ((NodeList) node).iterator();
//                while (iterator.hasNext()) {
//                    Node node1 = (Node)iterator.next();
//                    log.info("NodeList node: " + node1.getClass());
//                }
//            }

            log.info("Node to process " + node.getClass());
            if (node.getChildrenNodes().isEmpty()) {

//                if (MethodDeclaration.class.isInstance(node.getParentNode())) {

                    Node parentNode = node.clone();
//                    while(parentNode.getParentNode() != null) {
//                        parentNode = parentNode.getParentNode();
//                        log.info("Inside Parent Node Type: " + parentNode.getClass());
//                    }
                    log.info("Parent Node Type: " + parentNode.getClass());
//                    log.info("Method Declaration");
//                    log.info("Current Node Type: " + node.getClass());
//                }
//                log.info(node.toString());
            } else {
                log.info("Recurse");
                traverseTree(node.getChildrenNodes());
            }
        }
    }
}
