package org.nsu.dcis.amv.core.util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by John Jorgensen on 3/7/2017.
 */
@Component
public class AmvConfiguration {

    private String jhotdrawSourceRoot;
    private String jhotdrawExcludedDirectoryList;
    private String jhotdrawIncludeFileExtensions;

    public void setJhotdrawSourceRoot(String jhotdrawSourceRoot) {
        this.jhotdrawSourceRoot = jhotdrawSourceRoot;
    }

    public String getJhotdrawSourceRoot() {
        return jhotdrawSourceRoot;
    }

    public void setJhotdrawExcludedDirectoryList(String jhotdrawExcludedDirectoryList) {
        this.jhotdrawExcludedDirectoryList = jhotdrawExcludedDirectoryList;
    }

    public String getJhotdrawExcludedDirectoryList() {
        return jhotdrawExcludedDirectoryList;
    }

    public void setJhotdrawIncludeFileExtensions(String jhotdrawIncludeFileExtensions) {
        this.jhotdrawIncludeFileExtensions = jhotdrawIncludeFileExtensions.toUpperCase();
    }
    public Set<String> getFileExtensions() {
        Set<String> extensionSet = new HashSet<String>();
        StringTokenizer st = new StringTokenizer(jhotdrawIncludeFileExtensions, ",");
        while (st.hasMoreElements()) {
            extensionSet.add((String)st.nextElement());
        }
        return extensionSet;
    }
}
