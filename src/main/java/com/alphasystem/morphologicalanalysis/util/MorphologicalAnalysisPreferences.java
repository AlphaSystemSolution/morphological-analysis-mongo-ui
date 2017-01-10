package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;

/**
 * @author sali
 */
public class MorphologicalAnalysisPreferences extends MorphologicalEnginePreferences {

    private static final String NODE_PREFIX = "MorphologicalAnalysis";

    public MorphologicalAnalysisPreferences() {
        super(MorphologicalAnalysisPreferences.class);
    }

    @Override
    protected String nodePrefix() {
        return NODE_PREFIX;
    }
}
