package org.nsu.dcis.amv.core.service;

import org.nsu.dcis.amv.core.domain.FileScanResult;
import org.nsu.dcis.amv.core.util.FileScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by John Jorgensen on 3/7/2017.
 */
@Service
public class FileScanningService {

    @Autowired
    private FileScanner fileScanner;

    public FileScanResult scan(String rootDir, List<String> excludedDirectoryList, Set<String> includeFileExtensions) {
        List<String> scanList = fileScanner.scan(rootDir, excludedDirectoryList, includeFileExtensions);
        return new FileScanResult(scanList);
    }
}
