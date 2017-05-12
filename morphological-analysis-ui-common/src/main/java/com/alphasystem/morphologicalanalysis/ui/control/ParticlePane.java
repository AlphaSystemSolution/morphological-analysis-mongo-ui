package com.alphasystem.morphologicalanalysis.ui.control;

import com.alphasystem.arabic.ui.ArabicSupportGroupPane;
import com.alphasystem.fx.ui.util.FontConstants;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ParticlePartOfSpeechType;

/**
 * @author sali
 */
class ParticlePane extends ArabicSupportGroupPane<ParticlePartOfSpeechType> {

    ParticlePane() {
        super(ParticlePartOfSpeechType.values());
    }

    @Override
    protected void initToggleGroup() {
        toggleGroup.setWidth(128);
        toggleGroup.setHeight(48);
        toggleGroup.setFont(FontConstants.ARABIC_FONT_24);
    }
}
