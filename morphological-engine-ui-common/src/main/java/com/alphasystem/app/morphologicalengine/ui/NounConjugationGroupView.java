package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.ConjugationTuple;
import com.alphasystem.app.morphologicalengine.conjugation.model.NounConjugationGroup;
import com.alphasystem.app.morphologicalengine.ui.skin.NounConjugationGroupSkin;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class NounConjugationGroupView extends ConjugationGroupView<NounConjugationGroup> {

    @Override
    public boolean isEmpty() {
        final NounConjugationGroup group = getGroup();
        return (group == null) || (isEmpty(group.getNominative()) && isEmpty(group.getAccusative()) &&
                isEmpty(group.getGenitive()));
    }

    private boolean isEmpty(ConjugationTuple tuple) {
        return (tuple == null) || tuple.isEmpty();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
    }

    private class DefaultSkin extends SkinBase<NounConjugationGroupView> {

        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        private DefaultSkin(NounConjugationGroupView control) {
            super(control);
            getChildren().setAll(new NounConjugationGroupSkin(control));
        }
    }
}
