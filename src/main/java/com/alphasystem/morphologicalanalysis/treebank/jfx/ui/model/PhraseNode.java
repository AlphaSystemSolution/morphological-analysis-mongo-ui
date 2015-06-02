package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.morphologicalanalysis.model.support.GrammaticalRelationship;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.PHRASE;

/**
 * @author sali
 */
public class PhraseNode extends LineSupport {

    /**
     * x location for circle.
     */
    private final DoubleProperty cx;

    /**
     * y location for circle
     */
    private final DoubleProperty cy;

    private final ObjectProperty<GrammaticalRelationship> grammaticalRelationship;

    /**
     * @param grammaticalRelationship
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param cx
     * @param cy
     */
    public PhraseNode(GrammaticalRelationship grammaticalRelationship, String id, Double x, Double y, Double x1,
                      Double y1, Double x2, Double y2, Double cx, Double cy) {
        super(PHRASE, id, grammaticalRelationship.getLabel().toUnicode(), x, y, x1, y1, x2, y2);
        this.grammaticalRelationship = new SimpleObjectProperty<>(grammaticalRelationship);
        this.cx = new SimpleDoubleProperty(cx);
        this.cy = new SimpleDoubleProperty(cy);
    }

    public final GrammaticalRelationship getGrammaticalRelationship() {
        return grammaticalRelationship.get();
    }

    public final void setGrammaticalRelationship(GrammaticalRelationship grammaticalRelationship) {
        this.grammaticalRelationship.set(grammaticalRelationship);
    }

    public final ObjectProperty<GrammaticalRelationship> grammaticalRelationshipProperty() {
        return grammaticalRelationship;
    }

    public double getCx() {
        return cx.get();
    }

    public void setCx(double cx) {
        this.cx.set(cx);
    }

    public final DoubleProperty cxProperty() {
        return cx;
    }

    public double getCy() {
        return cy.get();
    }

    public void setCy(double cy) {
        this.cy.set(cy);
    }

    public final DoubleProperty cyProperty() {
        return cy;
    }

}
