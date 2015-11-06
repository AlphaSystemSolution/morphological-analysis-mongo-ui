package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraphTokenInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author sali
 */
public class DependencyGraphComparator implements Comparator<DependencyGraph> {

    @Override
    public int compare(DependencyGraph o1, DependencyGraph o2) {
        int result = o1.getChapterNumber().compareTo(o2.getChapterNumber());
        if (result == 0) {
            List<DependencyGraphTokenInfo> l1 = new ArrayList<>(o1.getTokens());
            List<DependencyGraphTokenInfo> l2 = new ArrayList<>(o2.getTokens());
            if (l1.size() == l2.size()) {
                for (int i = 0; i < l1.size(); i++) {
                    DependencyGraphTokenInfo t1 = l1.get(i);
                    DependencyGraphTokenInfo t2 = l2.get(i);
                    result = t1.compareTo(t2);
                }
            }
        }
        return result;
    }
}
