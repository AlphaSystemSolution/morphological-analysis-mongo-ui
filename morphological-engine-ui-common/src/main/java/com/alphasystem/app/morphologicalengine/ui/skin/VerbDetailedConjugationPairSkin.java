package com.alphasystem.app.morphologicalengine.ui.skin;

import com.alphasystem.morphologicalengine.model.VerbConjugationGroup;
import com.alphasystem.morphologicalengine.model.VerbDetailedConjugationPair;
import com.alphasystem.app.morphologicalengine.ui.VerbConjugationGroupView;
import com.alphasystem.app.morphologicalengine.ui.VerbDetailedConjugationPairView;

/**
 * @author sali
 */
public class VerbDetailedConjugationPairSkin extends DetailedConjugationPairSkin<VerbConjugationGroup, VerbDetailedConjugationPair,
        VerbConjugationGroupView, VerbDetailedConjugationPairView> {

    public VerbDetailedConjugationPairSkin(VerbDetailedConjugationPairView control) {
        super(control);
    }
}
