package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

/**
 * @author sali
 */
public abstract class PropertiesEditor<N extends GraphNode, A extends GraphNodeAdapter<N>> extends Control {

    private final ObjectProperty<A> node = new SimpleObjectProperty<>(null, "node");

    public PropertiesEditor() {
        nodeProperty().addListener((observable, oldValue, newValue) -> setValues(newValue));
    }

    protected void setValues(A node) {
    }

    public final A getNode() {
        return node.get();
    }

    public final void setNode(A node) {
        this.node.set(node);
    }

    public final ObjectProperty<A> nodeProperty() {
        return node;
    }
}
