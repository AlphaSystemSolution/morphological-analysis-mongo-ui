package com.alphasystem.app.morphologicalengine.ui.util;

import com.alphasystem.arabic.ui.util.UIUserPreferences;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * @author sali
 */
public class MorphologicalEnginePreferences extends UIUserPreferences {

    private static final String ARABIC_HEADING_FONT_SIZE = "arabicHeadingFontSize";
    private static final String NODE_PREFIX = "MorphologicalEngine";

    public MorphologicalEnginePreferences() {
        this(MorphologicalEnginePreferences.class);
    }

    protected MorphologicalEnginePreferences(Class<?> c) {
        super(c);
    }

    @Override
    protected String nodePrefix() {
        return NODE_PREFIX;
    }

    public long getArabicHeadingFontSize() {
        return getFontNode().getLong(ARABIC_HEADING_FONT_SIZE, 30);
    }

    public void setArabicHeadingFontSize(long size) {
        if (size <= 0) {
            return;
        }
        getFontNode().putLong(ARABIC_HEADING_FONT_SIZE, size);
    }

    public Font getArabicHeadingFont() {
        return Font.font(getArabicFontName(), FontWeight.BOLD, FontPosture.REGULAR, getArabicHeadingFontSize());
    }
}
