package com.alphasystem.morphologicalanalysis.ui.test;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.util.VerseTokensPairsReader;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Reporter.log;

/**
 * @author sali
 */
public class MiscTest {

    @Test
    public void testVerseTokensPairsReader() {
        Map<Integer, List<VerseTokenPairGroup>> map = VerseTokensPairsReader.read();
        int chapterNumber = 1;
        List<VerseTokenPairGroup> groups = map.get(chapterNumber);
        groups.forEach(group -> {
            log(String.format("Group for Chapter: %s\n%s", group.getChapterNumber(), group), true);
        });

        chapterNumber = 18;
        groups = map.get(chapterNumber);
        groups.forEach(group -> {
            log(String.format("Group for Chapter: %s\n%s", group.getChapterNumber(), group), true);
        });
    }
}
