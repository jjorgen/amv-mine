package org.nsu.dcis.amv.core.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
public class FileScanner {

    private static final String JAVA_FILE_EXTENSION = "JAVA";

    @Autowired
    AmvClientConfiguration amvClientConfiguration;

    @Autowired
    FileUtil fileUtil;

    private Logger log = Logger.getLogger(getClass().getName());

    public List<String> scan() {
        List<String> fileList = new ArrayList<>();
        scanFilesInDirectories(getRootDir(), getExcludedDirectoryList(), fileList);
        return fileList;
    }

    private void scanFilesInDirectories(String rootDir, List<String> excludedDirectoryList, List<String> fileList) {
        File root = new File(rootDir);
        File[] list = root.listFiles();

        if (list == null) return;

        for (File file : list) {
            if ( file.isDirectory() && (!inExcludedDirectoryList(file.getAbsolutePath(), excludedDirectoryList))) {
                scanFilesInDirectories(file.getAbsolutePath(), excludedDirectoryList, fileList);
                System.out.println( "Dir:" + file.getAbsoluteFile());
            }
            else {
                if (isSearchableFileExtension(fileUtil.getFileExtension(file))) {
                    fileList.add(file.getAbsolutePath());
                }
            }
        }
    }

    private boolean inExcludedDirectoryList(String absolutePath, List<String> excludedDirectoryList) {
        boolean excludedDirectory = false;
        for (String dir : excludedDirectoryList) {
            if (absolutePath.toUpperCase().contains(dir)) {
                excludedDirectory = true;
                return excludedDirectory;
            }
        }
        return excludedDirectory;
    }

    private boolean isSearchableFileExtension(String fileExtension) {
        return fileExtension.toUpperCase().contains(JAVA_FILE_EXTENSION);
    }

    public List<String> getExcludedDirectoryList() {
        String jhotdrawExcludedDirectoryList = amvClientConfiguration.getJhotdrawExcludedDirectoryList();
        StringTokenizer st = new StringTokenizer(jhotdrawExcludedDirectoryList, ",");
        List<String> excludedDirectoryList = new ArrayList<>();
        while(st.hasMoreElements()) {
            excludedDirectoryList.add(((String)st.nextElement()).toUpperCase());
        }
        return excludedDirectoryList;
    }

    public String getRootDir() {
        String rootDirectory = amvClientConfiguration.getJhotdrawSourceRoot();
        log.info("Root directory: " + rootDirectory);
        return rootDirectory;
    }
}
