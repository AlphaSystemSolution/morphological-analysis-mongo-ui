package com.alphasystem.app.morphologicalengine.ui.util;

import com.alphasystem.arabic.ui.util.UIUserPreferences;

/**
 * @author sali
 */
public class MorphologicalEnginePreferences extends UIUserPreferences {

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
}
