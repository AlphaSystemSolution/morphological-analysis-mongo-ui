package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.AbbreviatedConjugation;
import com.alphasystem.app.morphologicalengine.conjugation.model.ConjugationHeader;
import com.alphasystem.app.morphologicalengine.ui.skin.AbbreviatedConjugationSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class AbbreviatedConjugationView extends Control {

    private final ObjectProperty<ConjugationHeader> conjugationHeader = new SimpleObjectProperty<>(null, "conjugationHeader");
    private final ObjectProperty<AbbreviatedConjugation> abbreviatedConjugation = new SimpleObjectProperty<>(null, "abbreviatedConjugation");

    public final ConjugationHeader getConjugationHeader() {
        return conjugationHeader.get();
    }

    public final ObjectProperty<ConjugationHeader> conjugationHeaderProperty() {
        return conjugationHeader;
    }

    public final void setConjugationHeader(ConjugationHeader conjugationHeader) {
        this.conjugationHeader.set(conjugationHeader);
    }

    public final AbbreviatedConjugation getAbbreviatedConjugation() {
        return abbreviatedConjugation.get();
    }

    public final ObjectProperty<AbbreviatedConjugation> abbreviatedConjugationProperty() {
        return abbreviatedConjugation;
    }

    public final void setAbbreviatedConjugation(AbbreviatedConjugation abbreviatedConjugation) {
        this.abbreviatedConjugation.set(abbreviatedConjugation);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AbbreviatedConjugationSkin(this);
    }
}
