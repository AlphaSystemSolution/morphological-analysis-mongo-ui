package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.VerbConjugationGroup;
import com.alphasystem.app.morphologicalengine.conjugation.model.VerbDetailedConjugationPair;
import com.alphasystem.app.morphologicalengine.ui.skin.VerbDetailedConjugationPairSkin;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class VerbDetailedConjugationPairView extends DetailedConjugationPairView<VerbConjugationGroup, VerbDetailedConjugationPair> {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
    }

    private class DefaultSkin extends SkinBase<VerbDetailedConjugationPairView> {


        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        private DefaultSkin(VerbDetailedConjugationPairView control) {
            super(control);
            getChildren().setAll(new VerbDetailedConjugationPairSkin(control));
        }
    }
}
