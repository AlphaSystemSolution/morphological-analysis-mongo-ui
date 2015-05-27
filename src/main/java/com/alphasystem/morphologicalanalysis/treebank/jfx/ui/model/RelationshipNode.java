package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.morphologicalanalysis.model.support.GrammaticalRelationship;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.RELATIONSHIP;

/**
 * @author sali
 */
public class RelationshipNode extends GraphNode {

    private final DoubleProperty startX;

    private final DoubleProperty startY;

    private final DoubleProperty controlX1;

    private final DoubleProperty controlY1;

    private final DoubleProperty controlX2;

    private final DoubleProperty controlY2;

    private final DoubleProperty endX;

    private final DoubleProperty endY;

    /**
     * @param grammaticalRelationship
     * @param id
     * @param x
     * @param y
     * @param startX
     * @param startY
     * @param controlX1
     * @param controlY1
     * @param controlX2
     * @param controlY2
     * @param endX
     * @param endY
     */
    public RelationshipNode(GrammaticalRelationship grammaticalRelationship, String id, double x, double y, double startX,
                            double startY, double controlX1, double controlY1, double controlX2, double controlY2,
                            double endX, double endY) {
        super(RELATIONSHIP, grammaticalRelationship.getLabel().toUnicode(), id, x, y);
        this.startX = new SimpleDoubleProperty(startX);
        this.startY = new SimpleDoubleProperty(startY);
        this.controlX1 = new SimpleDoubleProperty(controlX1);
        this.controlY1 = new SimpleDoubleProperty(controlY1);
        this.controlX2 = new SimpleDoubleProperty(controlX2);
        this.controlY2 = new SimpleDoubleProperty(controlY2);
        this.endX = new SimpleDoubleProperty(endX);
        this.endY = new SimpleDoubleProperty(endY);
    }

    public double getControlX1() {
        return controlX1.get();
    }

    public void setControlX1(double controlX1) {
        this.controlX1.set(controlX1);
    }

    public final DoubleProperty controlX1Property() {
        return controlX1;
    }

    public double getControlX2() {
        return controlX2.get();
    }

    public void setControlX2(double controlX2) {
        this.controlX2.set(controlX2);
    }

    public final DoubleProperty controlX2Property() {
        return controlX2;
    }

    public double getControlY1() {
        return controlY1.get();
    }

    public void setControlY1(double controlY1) {
        this.controlY1.set(controlY1);
    }

    public final DoubleProperty controlY1Property() {
        return controlY1;
    }

    public double getControlY2() {
        return controlY2.get();
    }

    public void setControlY2(double controlY2) {
        this.controlY2.set(controlY2);
    }

    public final DoubleProperty controlY2Property() {
        return controlY2;
    }

    public double getEndX() {
        return endX.get();
    }

    public void setEndX(double endX) {
        this.endX.set(endX);
    }

    public final DoubleProperty endXProperty() {
        return endX;
    }

    public double getEndY() {
        return endY.get();
    }

    public void setEndY(double endY) {
        this.endY.set(endY);
    }

    public final DoubleProperty endYProperty() {
        return endY;
    }

    public double getStartX() {
        return startX.get();
    }

    public void setStartX(double startX) {
        this.startX.set(startX);
    }

    public final DoubleProperty startXProperty() {
        return startX;
    }

    public double getStartY() {
        return startY.get();
    }

    public void setStartY(double startY) {
        this.startY.set(startY);
    }

    public final DoubleProperty startYProperty() {
        return startY;
    }
}
