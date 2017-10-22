package org.nsu.dcis.core.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class CuPrinter {

    private FileInputStream in;

    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("C:/WS_NSU/amv/src/main/java/CH/ifa/draw/contrib/dnd/DNDFigures.java");


        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
            System.out.println("After Load");
        } finally {
            in.close();
        }

        // prints the resulting compilation unit to default system output
        System.out.println("Before");
        System.out.println(cu.toString());
        System.out.println("After");
    }

    public void printFile(String fileName) {
        CompilationUnit cu;
        try {
            in = new FileInputStream(fileName);
            cu = JavaParser.parse(in);
            System.out.println(cu.toString());
        } catch (Exception e) {

        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}