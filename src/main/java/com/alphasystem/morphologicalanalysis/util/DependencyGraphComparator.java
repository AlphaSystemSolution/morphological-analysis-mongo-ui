package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.graph.model.DependencyGraph;

import java.util.Comparator;

/**
 * @author sali
 */
public class DependencyGraphComparator implements Comparator<DependencyGraph> {

    @Override
    public int compare(DependencyGraph o1, DependencyGraph o2) {
        int result = o1.getChapterNumber().compareTo(o2.getChapterNumber());
        if (result == 0) {
            result = o1.getVerseNumber().compareTo(o2.getVerseNumber());
            if (result == 0) {
                result = o1.getFirstTokenIndex().compareTo(o2.getFirstTokenIndex());
                if (result == 0) {
                    result = o1.getLastTokenIndex().compareTo(o2.getLastTokenIndex());
                }
            }
        }
        return result;
    }
}
