package org.nsu.dcis.amv.core.domain;

import java.util.List;

/**
 * Created by John Jorgensen on 3/7/2017.
 */
public class FileScanResult {
    List<String> fileList;

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public FileScanResult(List<String> fileList) {
        this.fileList = fileList;
    }
}
