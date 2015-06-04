package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.DependencyGraphNodeType;
import javafx.beans.property.*;

/**
 * @author sali
 */
public abstract class GraphNode {

    protected final StringProperty id;

    protected final ObjectProperty<DependencyGraphNodeType> nodeType;

    protected final StringProperty text;

    protected final DoubleProperty x;

    protected final DoubleProperty y;

    protected final DoubleProperty translateX;

    protected final DoubleProperty translateY;

    protected final BooleanProperty _transient;

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     */
    protected GraphNode(DependencyGraphNodeType nodeType, String id, String text, Double x, Double y) {
        this(nodeType, id, text, x, y, 0.0, 0.0);
    }

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     * @param translateX
     * @param translateY
     */
    protected GraphNode(DependencyGraphNodeType nodeType, String id, String text, Double x, Double y,
                        Double translateX, Double translateY) {
        this.id = new SimpleStringProperty(id);
        this.nodeType = new ReadOnlyObjectWrapper<>(nodeType);
        this.text = new SimpleStringProperty(text);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.translateX = new SimpleDoubleProperty(translateX);
        this.translateY = new SimpleDoubleProperty(translateY);
        _transient = new ReadOnlyBooleanWrapper(false);
        this.x.addListener((observable, oldValue, newValue) -> {
            double d = (Double) newValue;
            _transient.setValue(d >= 0);
        });
        this.y.addListener((observable, oldValue, newValue) -> {
            double d = (Double) newValue;
            _transient.setValue(d >= 0);
        });
    }


    public final double getTranslateX() {
        return translateX.get();
    }

    public final void setTranslateX(double translateX) {
        this.translateX.set(translateX);
    }

    public final DoubleProperty translateXProperty() {
        return translateX;
    }

    public final double getTranslateY() {
        return translateY.get();
    }

    public final void setTranslateY(double translateY) {
        this.translateY.set(translateY);
    }

    public final DoubleProperty translateYProperty() {
        return translateY;
    }

    public boolean getTransient() {
        return _transient.get();
    }

    public final BooleanProperty transientProperty() {
        return _transient;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public final StringProperty idProperty() {
        return id;
    }

    public DependencyGraphNodeType getNodeType() {
        return nodeType.get();
    }

    public final ObjectProperty<DependencyGraphNodeType> nodeTypeProperty() {
        return nodeType;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public final StringProperty textProperty() {
        return text;
    }

    public double getX() {
        return x.get();
    }

    public void setX(Double x) {
        this.x.set(x);
    }

    public final DoubleProperty xProperty() {
        return x;
    }

    public double getY() {
        return y.get();
    }

    public void setY(Double y) {
        this.y.set(y);
    }

    public final DoubleProperty yProperty() {
        return y;
    }


    @Override
    public String toString() {
        return getText();
    }
}
