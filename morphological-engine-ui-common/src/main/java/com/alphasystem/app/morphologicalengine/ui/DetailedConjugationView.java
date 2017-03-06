package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.DetailedConjugation;
import com.alphasystem.app.morphologicalengine.ui.skin.DetailedConjugationSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class DetailedConjugationView extends Control {

    private final ObjectProperty<DetailedConjugation> detailedConjugation = new SimpleObjectProperty<>(null, "detailedConjugation");

    public final DetailedConjugation getDetailedConjugation() {
        return detailedConjugation.get();
    }

    public final ObjectProperty<DetailedConjugation> detailedConjugationProperty() {
        return detailedConjugation;
    }

    public final void setDetailedConjugation(DetailedConjugation detailedConjugation) {
        this.detailedConjugation.set(detailedConjugation);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DetailedConjugationSkin(this);
    }
}
