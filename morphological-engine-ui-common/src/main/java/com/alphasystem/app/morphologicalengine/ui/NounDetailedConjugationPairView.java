package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.NounConjugationGroup;
import com.alphasystem.app.morphologicalengine.conjugation.model.NounDetailedConjugationPair;
import com.alphasystem.app.morphologicalengine.ui.skin.NounDetailedConjugationPairSkin;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class NounDetailedConjugationPairView extends DetailedConjugationPairView<NounConjugationGroup, NounDetailedConjugationPair> {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
    }

    private class DefaultSkin extends SkinBase<NounDetailedConjugationPairView>{

        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        private DefaultSkin(NounDetailedConjugationPairView control) {
            super(control);
            getChildren().setAll(new NounDetailedConjugationPairSkin(control));
        }
    }
}
