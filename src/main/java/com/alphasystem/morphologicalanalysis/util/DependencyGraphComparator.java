package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;

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
            List<VerseTokensPair> l1 = new ArrayList<>(o1.getTokens());
            List<VerseTokensPair> l2 = new ArrayList<>(o2.getTokens());
            if (l1.size() == l2.size()) {
                for (int i = 0; i < l1.size(); i++) {
                    VerseTokensPair t1 = l1.get(i);
                    VerseTokensPair t2 = l2.get(i);
                    result = t1.compareTo(t2);
                }
            }
        }
        return result;
    }
}
