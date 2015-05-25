package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.PHRASE;

/**
 * @author sali
 */
public class PhraseNode extends GraphNode {

    /**
     * x1 location for line
     */
    private final DoubleProperty x1;

    /**
     * y1 location for line
     */
    private final DoubleProperty y1;

    /**
     * x2 location for line
     */
    private final DoubleProperty x2;

    /**
     * y2 location for line
     */
    private final DoubleProperty y2;

    /**
     * x location for circle.
     */
    private final DoubleProperty cx;

    /**
     * y location for circle
     */
    private final DoubleProperty cy;

    public PhraseNode(String text, double x, double y, double x1, double y1, double x2,
                      double y2, double cx, double cy) {
        super(PHRASE, text, x, y);
        this.x1 = new SimpleDoubleProperty(x1);
        this.y1 = new SimpleDoubleProperty(y1);
        this.x2 = new SimpleDoubleProperty(x2);
        this.y2 = new SimpleDoubleProperty(y2);
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

    public double getX1() {
        return x1.get();
    }

    public void setX1(double x1) {
        this.x1.set(x1);
    }

    public final DoubleProperty x1Property() {
        return x1;
    }

    public double getX2() {
        return x2.get();
    }

    public void setX2(double x2) {
        this.x2.set(x2);
    }

    public final DoubleProperty x2Property() {
        return x2;
    }

    public double getY1() {
        return y1.get();
    }

    public void setY1(double y1) {
        this.y1.set(y1);
    }

    public final DoubleProperty y1Property() {
        return y1;
    }

    public double getY2() {
        return y2.get();
    }

    public void setY2(double y2) {
        this.y2.set(y2);
    }

    public final DoubleProperty y2Property() {
        return y2;
    }
}
