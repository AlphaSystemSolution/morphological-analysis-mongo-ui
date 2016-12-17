package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;

/**
 * @author sali
 */
public class PropertyAccessor<N extends GraphNode, A extends GraphNodeAdapter<N>> {

    private final GetterAdapter<N, A> getterAdapter;
    private final SetterAdapter<N, A> setterAdapter;
    private final A node;

    public PropertyAccessor(GetterAdapter<N, A> getterAdapter, SetterAdapter<N, A> setterAdapter, A node) {
        this.getterAdapter = getterAdapter;
        this.setterAdapter = setterAdapter;
        this.node = node;
    }

    public Double get() {
        return (node == null) ? 0.0 : getterAdapter.get(node);
    }

    public void set(Double value) {
        if (node != null) {
            setterAdapter.set(node, value);
        }
    }
}
