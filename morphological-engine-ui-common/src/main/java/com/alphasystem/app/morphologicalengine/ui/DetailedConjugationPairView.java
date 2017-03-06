package com.alphasystem.app.morphologicalengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.ConjugationGroup;
import com.alphasystem.app.morphologicalengine.conjugation.model.DetailedConjugationPair;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

/**
 * @author sali
 */
public class DetailedConjugationPairView<G extends ConjugationGroup, P extends DetailedConjugationPair<G>> extends Control {

    private ObjectProperty<P> pair = new SimpleObjectProperty<P>(null, "pair", null);
    private ReadOnlyObjectWrapper<G> left = new ReadOnlyObjectWrapper<>(null, "left", null);
    private ReadOnlyObjectWrapper<G> right = new ReadOnlyObjectWrapper<>(null, "right", null);

    DetailedConjugationPairView() {
        pairProperty().addListener((observable, oldValue, newValue) -> initializeValues(newValue));
    }

    private void initializeValues(P pair) {
        if (pair == null) {
            setLeft(null);
            setRight(null);
        } else {
            setLeft(pair.getLeftSideConjugations());
            setRight(pair.getRightSideConjugations());
        }
    }

    public final G getLeft() {
        return left.get();
    }

    public final ReadOnlyObjectProperty<G> leftProperty() {
        return left.getReadOnlyProperty();
    }

    private void setLeft(G left) {
        this.left.set(left);
    }

    public final G getRight() {
        return right.get();
    }

    public final ReadOnlyObjectProperty<G> rightProperty() {
        return right.getReadOnlyProperty();
    }

    private void setRight(G right) {
        this.right.set(right);
    }

    public final P getPair() {
        return pair.get();
    }

    private ObjectProperty<P> pairProperty() {
        return pair;
    }

    public final void setPair(P pair) {
        this.pair.set(pair);
    }
}
