package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import javafx.beans.property.*;

/**
 * @author sali
 */
public abstract class GraphNode {

    protected final StringProperty id;

    protected final ObjectProperty<NodeType> nodeType;

    protected final StringProperty text;

    protected final DoubleProperty x;

    protected final DoubleProperty y;

    protected final BooleanProperty _transient;

    protected GraphNode(NodeType nodeType, String id, String text, double x, double y) {
        this.id = new SimpleStringProperty(id);
        this.nodeType = new ReadOnlyObjectWrapper<>(nodeType);
        this.text = new SimpleStringProperty(text);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
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

    public NodeType getNodeType() {
        return nodeType.get();
    }

    public final ObjectProperty<NodeType> nodeTypeProperty() {
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

    public void setX(double x) {
        this.x.set(x);
    }

    public final DoubleProperty xProperty() {
        return x;
    }

    public double getY() {
        return y.get();
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public final DoubleProperty yProperty() {
        return y;
    }

}
