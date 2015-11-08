package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.VerseTokenPairGroup;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static com.alphasystem.util.AppUtil.getResource;
import static java.lang.Integer.parseInt;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

/**
 * @author sali
 */
public class VerseTokensPairsReader {

    public static Map<Integer, List<VerseTokenPairGroup>> read() {
        Map<Integer, List<VerseTokenPairGroup>> resultMap = new LinkedHashMap<>();

        try {
            URL url = getResource("verse-tokens-pairs.txt");
            List<String> list = readAllLines(get(url.toURI()));
            ListIterator<String> listIterator = list.listIterator();
            List<VerseTokenPairGroup> currentGroupList = new ArrayList<>();
            int chapterNumber = 0;
            while (listIterator.hasNext()) {
                String line = listIterator.next();
                try {
                    // if no exception occurs then this the start of new chapter line
                    int cn = parseInt(line);
                    chapterNumber = cn;
                    currentGroupList = new ArrayList<>();
                    resultMap.put(chapterNumber, currentGroupList);
                    listIterator.remove();
                    line = listIterator.next();
                } catch (NumberFormatException e) {
                }

                String[] pairs = line.split("\\|");
                //System.out.println(format("Number of pairs found %s", pairs.length));
                VerseTokenPairGroup currentGroup = new VerseTokenPairGroup();
                currentGroup.setChapterNumber(chapterNumber);
                currentGroupList.add(currentGroup);
                for (String pair : pairs) {
                    currentGroup.getPairs().add(createPair(pair));
                }

                listIterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    private static VerseTokensPair createPair(String line) {
        String[] values = line.split(":");
        return new VerseTokensPair(parseInt(values[0]), parseInt(values[1]), parseInt(values[2]));
    }


}