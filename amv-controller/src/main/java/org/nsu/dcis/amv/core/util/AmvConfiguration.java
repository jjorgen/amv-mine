package org.nsu.dcis.amv.core.util;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by John Jorgensen on 3/7/2017.
 */
@Component
public class AmvConfiguration {

    private String jhotdrawSourceRoot;
    private String jhotdrawExcludedDirectoryList;
    private String jhotdrawIncludeFileExtensions;
    private String topOfMethodTokenThreshold;
    private String bottomOfMethodTokenThreshold;

    public void setJhotdrawSourceRoot(String jhotdrawSourceRoot) {
        this.jhotdrawSourceRoot = jhotdrawSourceRoot;
    }

    public String getJhotdrawSourceRoot() {
        return jhotdrawSourceRoot;
    }

    public void setJhotdrawExcludedDirectoryList(String jhotdrawExcludedDirectoryList) {
        this.jhotdrawExcludedDirectoryList = jhotdrawExcludedDirectoryList;
    }

    public List<String> getExcludedDirectoryList() {
        String jhotdrawExcludedDirectoryList = getJhotdrawExcludedDirectoryCommaDelimitedList();
        StringTokenizer st = new StringTokenizer(jhotdrawExcludedDirectoryList, ",");
        List<String> excludedDirectoryList = new ArrayList<>();
        while(st.hasMoreElements()) {
            excludedDirectoryList.add(((String)st.nextElement()).toUpperCase());
        }
        return excludedDirectoryList;
    }

    public String getJhotdrawExcludedDirectoryCommaDelimitedList() {
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

    public void setTopOfMethodTokenThreshold(String topOfMethodTokenThreshold) {
        this.topOfMethodTokenThreshold = topOfMethodTokenThreshold;
    }

    public String getTopOfMethodTokenThreshold() {
        return topOfMethodTokenThreshold;
    }

    public void setBottomOfMethodTokenThreshold(String bottomOfMethodTokenThreshold) {
        this.bottomOfMethodTokenThreshold = bottomOfMethodTokenThreshold;
    }

    public String getBottomOfMethodTokenThreshold() {
        return bottomOfMethodTokenThreshold;
    }
}
