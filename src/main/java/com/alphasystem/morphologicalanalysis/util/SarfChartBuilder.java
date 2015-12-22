package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.app.sarfengine.conjugation.model.ConjugationStack;
import com.alphasystem.app.sarfengine.conjugation.model.SarfChart;
import com.alphasystem.app.sarfengine.conjugation.model.SarfKabeer;
import com.alphasystem.app.sarfengine.conjugation.model.SarfKabeerPair;
import com.alphasystem.morphologicalanalysis.morphology.model.RootWord;
import com.alphasystem.util.AppUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.ArrayUtils.*;

/**
 * @author sali
 */
public class SarfChartBuilder {

    private static final int NUM_OF_COLUMNS = 3;
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("sarf-html-template");
    private static File outputFolder = new File(AppUtil.USER_HOME_DIR, ".sarfengine");
    private static File outFile = new File(outputFolder, "chart.html");

    static {
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
    }

    public static File createChart(SarfChart sarfChart) {
        if (sarfChart == null) {
            return null;
        }
        List<String> lines = new ArrayList<>();
        lines.add(resourceBundle.getString("html_deceleration_start"));
        lines.add(resourceBundle.getString("header"));

        // start of detailed conjugation
        String s = resourceBundle.getString("detailed_cojugation_table_start");
        s = format(s, sarfChart.hashCode());
        lines.add(s);

        SarfKabeer sarfKabeer = sarfChart.getSarfKabeer();
        buildSarfKabeerPair(sarfKabeer.getActiveTensePair(), lines);
        addEmptyRow(lines);
        buildSarfKabeerPairs(sarfKabeer.getVerbalNounPairs(), lines);
        buildSarfKabeerPair(sarfKabeer.getActiveParticiplePair(), lines);
        addEmptyRow(lines);
        SarfKabeerPair passiveTensePair = sarfKabeer.getPassiveTensePair();
        if (passiveTensePair != null) {
            buildSarfKabeerPair(passiveTensePair, lines);
            addEmptyRow(lines);
        }
        SarfKabeerPair passiveParticiplePair = sarfKabeer.getPassiveParticiplePair();
        if (passiveParticiplePair != null) {
            buildSarfKabeerPair(passiveParticiplePair, lines);
            addEmptyRow(lines);
        }
        buildSarfKabeerPairs(sarfKabeer.getAdverbPairs(), lines);

        // end of detailed conjugation
        lines.add(resourceBundle.getString("detailed_cojugation_table_end"));
        lines.add(resourceBundle.getString("html_deceleration_end"));
        try {
            Files.write(Paths.get(outFile.toURI()), lines);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return outFile;
    }

    private static void addEmptyRow(List<String> lines) {
        String emptyRow = resourceBundle.getString("detailed_cojugation_empty_row");
        lines.add(emptyRow);
    }

    private static void buildSarfKabeerPair(SarfKabeerPair sarfKabeerPair, List<String> lines) {
        ConjugationStack rightSideStack = sarfKabeerPair.getRightSideStack();
        ConjugationStack leftSideStack = sarfKabeerPair.getLeftSideStack();
        createCaption(lines, rightSideStack, leftSideStack);
        createConjugations(lines, rightSideStack, leftSideStack);
    }

    private static void buildSarfKabeerPairs(SarfKabeerPair[] sarfKabeerPairs, List<String> lines) {
        if (!isEmpty(sarfKabeerPairs)) {
            for (SarfKabeerPair sarfKabeerPair : sarfKabeerPairs) {
                buildSarfKabeerPair(sarfKabeerPair, lines);
                addEmptyRow(lines);
            }
        }
    }

    private static void createConjugations(List<String> lines, ConjugationStack rightSideStack, ConjugationStack leftSideStack) {
        RootWord[] rightConjugations = rightSideStack.getConjugations();
        RootWord[] leftConjugations = leftSideStack.getConjugations();
        int length = leftConjugations.length;
        int startIndex = 0;
        int endIndex = NUM_OF_COLUMNS;
        while (startIndex < length) {
            RootWord[] rightSubArray = subarray(rightConjugations, startIndex, endIndex);
            reverse(rightSubArray);
            RootWord[] leftSubArray = subarray(leftConjugations, startIndex, endIndex);
            reverse(leftSubArray);
            String[] values = new String[0];
            for (RootWord rootWord : rightSubArray) {
                values = addValue(rightSideStack.isEmpty(), values, rootWord);
            }
            for (RootWord rootWord : leftSubArray) {
                values = addValue(leftSideStack.isEmpty(), values, rootWord);
            }
            lines.add(format(resourceBundle.getString("detailed_cojugation_column"), new Object[]{values}));
            startIndex = endIndex;
            endIndex += NUM_OF_COLUMNS;
        }
    }

    private static String[] addValue(boolean empty, String[] values, RootWord rootWord) {
        String resourceKey = empty ? "detailed_cojugation_column_no_border" :
                "detailed_cojugation_column_border";
        String value = resourceBundle.getString(resourceKey);
        String html = (rootWord == null) ? "" : rootWord.getLabel().toHtmlCode();
        value = empty ? value : format(value, html);
        values = add(values, value);
        return values;
    }

    private static void createCaption(List<String> lines, ConjugationStack rightSideStack,
                                      ConjugationStack leftSideStack) {
        String resourceKey = leftSideStack.isEmpty() ? "detailed_cojugation_caption_no_border" :
                "detailed_cojugation_caption_border";
        String leftCaption = resourceBundle.getString(resourceKey);
        leftCaption = leftSideStack.isEmpty() ? leftCaption : format(leftCaption,
                leftSideStack.getLabel().getLabel().toHtmlCode());

        resourceKey = rightSideStack.isEmpty() ? "detailed_cojugation_caption_no_border" :
                "detailed_cojugation_caption_border";
        String rightCaption = resourceBundle.getString(resourceKey);
        rightCaption = rightSideStack.isEmpty() ? rightCaption : format(rightCaption,
                rightSideStack.getLabel().getLabel().toHtmlCode());

        String midColumn = resourceBundle.getString("detailed_cojugation_caption_mid");
        midColumn = format(midColumn, (leftSideStack.getConjugations().length / NUM_OF_COLUMNS) + 1);

        String caption = resourceBundle.getString("detailed_cojugation_caption");
        caption = format(caption, rightCaption, midColumn, leftCaption);
        lines.add(caption);
    }
}
