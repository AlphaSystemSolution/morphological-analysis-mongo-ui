package com.alphasystem.app.morphologicalengine.ui.skin;

import com.alphasystem.morphologicalengine.model.NounConjugationGroup;
import com.alphasystem.morphologicalengine.model.NounDetailedConjugationPair;
import com.alphasystem.app.morphologicalengine.ui.NounConjugationGroupView;
import com.alphasystem.app.morphologicalengine.ui.NounDetailedConjugationPairView;

/**
 * @author sali
 */
public class NounDetailedConjugationPairSkin extends DetailedConjugationPairSkin<NounConjugationGroup, NounDetailedConjugationPair,
        NounConjugationGroupView, NounDetailedConjugationPairView> {

    public NounDetailedConjugationPairSkin(NounDetailedConjugationPairView control) {
        super(control);
    }
}
