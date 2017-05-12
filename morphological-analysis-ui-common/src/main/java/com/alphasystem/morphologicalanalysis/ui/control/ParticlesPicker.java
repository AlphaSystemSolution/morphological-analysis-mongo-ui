package com.alphasystem.morphologicalanalysis.ui.control;

import com.alphasystem.arabic.ui.MultiValuedLabelPicker;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ParticlePartOfSpeechType;

import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class ParticlesPicker extends MultiValuedLabelPicker<ParticlePartOfSpeechType> {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ParticlesPickerSkin(this);
    }

    @Override
    public String getImageName() {
        return "images.particle.png";
    }

}
