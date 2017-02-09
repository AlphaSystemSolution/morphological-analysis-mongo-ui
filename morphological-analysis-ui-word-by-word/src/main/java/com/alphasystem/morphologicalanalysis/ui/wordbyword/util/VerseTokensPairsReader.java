package com.alphasystem.morphologicalanalysis.ui.wordbyword.util;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.alphasystem.util.AppUtil.readAllLines;
import static java.lang.Integer.parseInt;

/**
 * @author sali
 */
public class VerseTokensPairsReader {

    public static Map<Integer, List<VerseTokenPairGroup>> read() {
        Map<Integer, List<VerseTokenPairGroup>> resultMap = new LinkedHashMap<>();

        try {
            List<String> list = readAllLines("verse-tokens-pairs.txt");
            ListIterator<String> listIterator = list.listIterator();
            List<VerseTokenPairGroup> currentGroupList = new ArrayList<>();
            int chapterNumber = 0;
            while (listIterator.hasNext()) {
                String line = listIterator.next();
                try {
                    // if no exception occurs then this the start of new chapter line
                    chapterNumber = parseInt(line);
                    currentGroupList = new ArrayList<>();
                    resultMap.put(chapterNumber, currentGroupList);
                    listIterator.remove();
                    line = listIterator.next();
                } catch (NumberFormatException e) {
                    // ignore
                }

                currentGroupList.add(createGroup(chapterNumber, line));
                listIterator.remove();
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    private static VerseTokenPairGroup createGroup(Integer chapterNumber, String line) {
        VerseTokenPairGroup group = new VerseTokenPairGroup();
        String[] pairs = line.split("\\|");
        group.setChapterNumber(chapterNumber);
        for (String pair : pairs) {
            group.getPairs().add(createPair(pair));
        }
        return group;
    }

    private static VerseTokensPair createPair(String line) {
        String[] values = line.split(":");
        return new VerseTokensPair(parseInt(values[0]), parseInt(values[1]), parseInt(values[2]));
    }


}
