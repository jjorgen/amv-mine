package org.nsu.dcis.amv.core.instrumentation;

import org.nsu.dcis.amv.core.util.AmvConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by John Jorgensen on 3/15/2017.
 */
@Component
public class AmvConfigurationInstrumentation {

    @Autowired
    private AmvConfiguration amvConfiguration;

    public String  getRootDir(){
        String rootDirectory = amvConfiguration.getJhotdrawSourceRoot();
        return rootDirectory;
    }

    public List<String> getExcludedDirectoryList() {
        String jhotdrawExcludedDirectoryList = amvConfiguration.getJhotdrawExcludedDirectoryList();
        StringTokenizer st = new StringTokenizer(jhotdrawExcludedDirectoryList, ",");
        List<String> excludedDirectoryList = new ArrayList<>();
        while(st.hasMoreElements()) {
            excludedDirectoryList.add(((String)st.nextElement()).toUpperCase());
        }
        return excludedDirectoryList;
    }

    public Set<String> getFileExtensions() {
        return amvConfiguration.getFileExtensions();
    }
}
