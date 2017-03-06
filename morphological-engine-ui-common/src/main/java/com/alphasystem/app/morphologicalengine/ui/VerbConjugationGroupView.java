package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.ConjugationTuple;
import com.alphasystem.app.morphologicalengine.conjugation.model.VerbConjugationGroup;
import com.alphasystem.app.morphologicalengine.ui.skin.VerbConjugationGroupSkin;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class VerbConjugationGroupView extends ConjugationGroupView<VerbConjugationGroup> {

    @Override
    public boolean isEmpty() {
        VerbConjugationGroup group = getGroup();
        return (group == null) || (isEmpty(group.getMasculineThirdPerson()) && isEmpty(group.getFeminineThirdPerson()) &&
                isEmpty(group.getMasculineSecondPerson()) && isEmpty(group.getFeminineSecondPerson()) &&
                isEmpty(group.getFirstPerson()));
    }

    private boolean isEmpty(ConjugationTuple tuple) {
        return (tuple == null) || tuple.isEmpty();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
    }

    private class DefaultSkin extends SkinBase<VerbConjugationGroupView> {

        /**
         * Constructor for all SkinBase instances.
         *
         * @param control The control for which this Skin should attach to.
         */
        private DefaultSkin(VerbConjugationGroupView control) {
            super(control);
            getChildren().setAll(new VerbConjugationGroupSkin(control));
        }
    }
}
