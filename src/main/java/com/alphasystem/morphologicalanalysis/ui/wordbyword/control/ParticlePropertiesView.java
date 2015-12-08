package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.ParticlePropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class ParticlePropertiesView extends AbstractPropertiesView<ParticleProperties> {

    public ParticlePropertiesView() {
        super();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ParticlePropertiesSkin(this);
    }
}
