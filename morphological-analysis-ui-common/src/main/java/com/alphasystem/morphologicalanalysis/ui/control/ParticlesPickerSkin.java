package com.alphasystem.morphologicalanalysis.ui.control;

import com.alphasystem.arabic.ui.MultiValuedLabelPicker;
import com.alphasystem.arabic.ui.skin.MultiValuedLabelPickerSkin;
import com.alphasystem.fx.ui.util.FontConstants;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ParticlePartOfSpeechType;

/**
 * @author sali
 */
class ParticlesPickerSkin extends MultiValuedLabelPickerSkin<ParticlePartOfSpeechType> {

    ParticlesPickerSkin(MultiValuedLabelPicker<ParticlePartOfSpeechType> control) {
        super(control, new ParticlePane(), new ParticlesPickerViewer());
    }

    private static class ParticlesPickerViewer extends SelectedValuesView<ParticlePartOfSpeechType> {
        ParticlesPickerViewer() {
            super();
        }

        @Override
        protected void initToggleGroup() {
            toggleGroup.setWidth(96);
            toggleGroup.setHeight(40);
            toggleGroup.setDisable(true);
            toggleGroup.setFont(FontConstants.ARABIC_FONT_24);
        }
    }
}
