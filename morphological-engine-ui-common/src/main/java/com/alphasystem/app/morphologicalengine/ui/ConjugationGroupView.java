package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.ConjugationGroup;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

/**
 * @author sali
 */
public abstract class ConjugationGroupView<G extends ConjugationGroup> extends Control {

    private final ObjectProperty<G> group = new SimpleObjectProperty<G>(null, "group", null);

    public abstract boolean isEmpty();

    public final G getGroup() {
        return group.get();
    }

    public final ObjectProperty<G> groupProperty() {
        return group;
    }

    public final void setGroup(G group) {
        this.group.set(group);
    }
}
