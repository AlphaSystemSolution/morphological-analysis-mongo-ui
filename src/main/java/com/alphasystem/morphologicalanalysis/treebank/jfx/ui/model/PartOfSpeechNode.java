package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.morphologicalanalysis.model.support.PartOfSpeech;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.PART_OF_SPEECH;

/**
 * @author sali
 */
public class PartOfSpeechNode extends GraphNode {

    /**
     * x location for circle.
     */
    private final DoubleProperty cx;

    /**
     * y location for circle
     */
    private final DoubleProperty cy;

    /**
     *
     * @param partOfSpeech
     * @param id
     * @param x
     * @param y
     * @param cx
     * @param cy
     */
    public PartOfSpeechNode(PartOfSpeech partOfSpeech, String id, double x, double y, double cx, double cy) {
        super(PART_OF_SPEECH, id, partOfSpeech.getLabel().toUnicode(), x, y);
        this.cx = new SimpleDoubleProperty(cx);
        this.cy = new SimpleDoubleProperty(cy);
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
