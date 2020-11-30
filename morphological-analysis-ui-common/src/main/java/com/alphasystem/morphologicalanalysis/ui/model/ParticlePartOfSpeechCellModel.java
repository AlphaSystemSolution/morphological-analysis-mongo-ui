package com.alphasystem.morphologicalanalysis.ui.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ParticlePartOfSpeechType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public class ParticlePartOfSpeechCellModel {

    private final BooleanProperty checked = new SimpleBooleanProperty(this, "checked", false);
    private final ObjectProperty<ParticlePartOfSpeechType> particlePartOfSpeechType = new SimpleObjectProperty<>(this, "particlePartOfSpeechType");

    public ParticlePartOfSpeechCellModel(ParticlePartOfSpeechType particlePartOfSpeechType) {
        setParticlePartOfSpeechType(particlePartOfSpeechType);
    }

    public final ParticlePartOfSpeechType getParticlePartOfSpeechType() {
        return particlePartOfSpeechType.get();
    }

    public final ObjectProperty<ParticlePartOfSpeechType> particlePartOfSpeechTypeProperty() {
        return particlePartOfSpeechType;
    }

    private void setParticlePartOfSpeechType(ParticlePartOfSpeechType particlePartOfSpeechType) {
        this.particlePartOfSpeechType.set(particlePartOfSpeechType);
    }

    public final boolean isChecked() {
        return checked.get();
    }

    public final BooleanProperty checkedProperty() {
        return checked;
    }

    public final void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", getParticlePartOfSpeechType().ordinal(), getParticlePartOfSpeechType());
    }
}
