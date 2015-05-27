package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import javafx.beans.property.*;

/**
 * @author sali
 */
public abstract class GraphNode {

    protected final StringProperty id;

    protected final ObjectProperty<NodeType> nodeType;

    protected final StringProperty textProperty;

    protected final DoubleProperty xProperty;

    protected final DoubleProperty yProperty;

    protected GraphNode(NodeType nodeType, String id, String text, double x, double y) {
        this.id = new SimpleStringProperty(id);
        this.nodeType = new ReadOnlyObjectWrapper<>(nodeType);
        this.textProperty = new SimpleStringProperty(text);
        this.xProperty = new SimpleDoubleProperty(x);
        this.yProperty = new SimpleDoubleProperty(y);
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

    public String getTextProperty() {
        return textProperty.get();
    }

    public void setTextProperty(String textProperty) {
        this.textProperty.set(textProperty);
    }

    public final StringProperty textPropertyProperty() {
        return textProperty;
    }

    public double getxProperty() {
        return xProperty.get();
    }

    public void setxProperty(double xProperty) {
        this.xProperty.set(xProperty);
    }

    public final DoubleProperty xPropertyProperty() {
        return xProperty;
    }

    public double getyProperty() {
        return yProperty.get();
    }

    public void setyProperty(double yProperty) {
        this.yProperty.set(yProperty);
    }

    public final DoubleProperty yPropertyProperty() {
        return yProperty;
    }

    public String getDescription() {
        return toString();
    }

}
