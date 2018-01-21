package org.nsu.dcis.amv.core.util;

import java.util.ArrayList;
import java.util.List;

public class PdgWrapper {
    List<Pdg> pdgList = new ArrayList<Pdg>();

    public void add(Pdg pdg) {
        pdgList.add(pdg);
    }

    public List<Pdg> getPdgs() {
        return pdgList;
    }
}
