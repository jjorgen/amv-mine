package org.nsu.dcis.amv.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class PdgScanner {

    private static final String JAVA_FILE_EXTENSION = "JAVA";

    @Autowired
    PdgFileParser pdgFileParser;

    @Autowired
    FileUtil fileUtil;

    public PdgWrapper scan(String rootDir, List<String> excludedDirectoryList) {
        PdgWrapper pdgWrapper = new PdgWrapper();
        scanFilesInDirectories(rootDir, excludedDirectoryList, pdgWrapper);
        return pdgWrapper;
    }

    private void scanFilesInDirectories(String rootDir, List<String> excludedDirectoryList, PdgWrapper pdgWrapper) {
        File root = new File(rootDir);
        File[] list = root.listFiles();

        if (list == null) return;

        for (File file : list) {
            if ( file.isDirectory() && (!inExcludedDirectoryList(file.getAbsolutePath(), excludedDirectoryList))) {
                scanFilesInDirectories(file.getAbsolutePath(), excludedDirectoryList, pdgWrapper);
                System.out.println( "Dir:" + file.getAbsoluteFile());
            }
            else {
                if (isSearchableFileExtension(fileUtil.getFileExtension(file))) {
                    pdgFileParser.getPdgs(file.getAbsolutePath(), pdgWrapper);
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
}
