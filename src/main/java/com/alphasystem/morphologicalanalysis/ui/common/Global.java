package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.AlternateStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.ResourceBundle;

import static com.alphasystem.arabic.model.ArabicLetterType.*;
import static com.alphasystem.arabic.model.ArabicLetters.WORD_SPACE;
import static com.alphasystem.morphologicalanalysis.graph.model.support.GraphNodeType.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javafx.scene.text.Font.font;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BOLD;

/**
 * @author sali
 */
public class Global {

    public static final String SPACE_STR = WORD_SPACE.toUnicode();
    public static final ArabicWord FI_MAHL = ArabicWord.getWord(FA, YA, SPACE, MEEM, HHA, LAM);
    public static final List<GraphNodeType> TERMINALS = asList(TERMINAL, IMPLIED, HIDDEN, REFERENCE);
    public static final List<GraphNodeType> NON_TERMINALS = asList(IMPLIED, HIDDEN, REFERENCE);
    public static final List<PartOfSpeech> PART_OF_SPEECH_EXCLUDE_LIST = asList(
            DEFINITE_ARTICLE, QURANIC_PUNCTUATION, CONJUNCTION_PARTICLE_WAW, CONJUNCTION_PARTICLE_FA);
    public static final String ARABIC_FONT_NAME = System.getProperty("arabic-font-name", "Arabic Typesetting");
    public static final String TRANSLATION_FONT_FAMILY = "Candara";
    public static final int DEFAULT_TOKEN_FONT_SIZE = 48;
    public static final int DEFAULT_POS_FONT_SIZE = 24;
    public static final int DEFAULT_TRANSLATION_FONT_SIZE = 14;
    public static final Font ARABIC_FONT_BIG = font(ARABIC_FONT_NAME, REGULAR, DEFAULT_TOKEN_FONT_SIZE);
    public static final Font ARABIC_FONT_MEDIUM = font(ARABIC_FONT_NAME, REGULAR, 36);
    public static final Font ARABIC_FONT_MEDIUM_BOLD = font(ARABIC_FONT_NAME, BOLD, REGULAR, 30);
    public static final Font ARABIC_FONT_SMALL_BOLD = font(ARABIC_FONT_NAME, BOLD, REGULAR, 20);
    public static final Font ARABIC_FONT_SMALL = font(ARABIC_FONT_NAME, REGULAR, DEFAULT_POS_FONT_SIZE);
    public static final Font ENGLISH_FONT = font(TRANSLATION_FONT_FAMILY, REGULAR, DEFAULT_TRANSLATION_FONT_SIZE);
    public static final Font ENGLISH_FONT_SMALL = font(TRANSLATION_FONT_FAMILY, REGULAR, 14);
    public static final double MIN_WIDTH = 20;
    public static final double MIN_HEIGHT = 20;
    public static final int AMOUNT_STEP_BY = 20;
    public static final int MAX_WIDTH = 1200;
    public static final int MAX_HEIGHT = 1000;
    public static final double RECTANGLE_WIDTH = 100;
    public static final double RECTANGLE_HEIGHT = 100;
    public static final double MIN_RECTANGLE_WIDTH = 60;
    public static final double MIN_RECTANGLE_HEIGHT = 10;
    public static final double GAP_BETWEEN_TOKENS = 60;
    public static final double MIN_GAP_BETWEEN_TOKENS = 10;
    public static final double INITIAL_X = 0;
    public static final double INITIAL_Y = 40;
    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("resources");
    public static final String MAWRID_READER_URL = System.getProperty("mawrid-reader.url");
    private static Global instance;

    private Global() {
    }

    public synchronized static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public static String getLabel(Class<?> klass, String label) {
        String key = format("%s.%s.label", klass.getSimpleName(), label);
        return RESOURCE_BUNDLE.getString(key);
    }

    public static Font createFont(int size) {
        return font(ARABIC_FONT_NAME, size);
    }

    public static boolean isFakeTerminal(TerminalNodeAdapter src) {
        GraphNodeType graphNodeType = src.getGraphNodeType();
        return graphNodeType.equals(IMPLIED) || graphNodeType.equals(HIDDEN) || graphNodeType.equals(REFERENCE);
    }

    public static boolean isTerminal(GraphNodeAdapter src) {
        return TERMINALS.contains(src.getGraphNodeType());
    }

    public static boolean isTerminal(GraphNode src) {
        return TERMINALS.contains(src.getGraphNodeType());
    }

    public static AlternateStatus getFromNounStatus(NounStatus nounStatus) {
        return AlternateStatus.valueOf(nounStatus.name());
    }

    public static Font fromFontMetaInfo(FontMetaInfo fmi) {
        return font(fmi.getFamily(), FontWeight.valueOf(fmi.getWeight()),
                FontPosture.valueOf(fmi.getPosture()), fmi.getSize());
    }

    public static FontMetaInfo fromFont(Font font) {
        return new FontMetaInfo(font.getFamily(), "NORMAL", "REGULAR", font.getSize());
    }
}
