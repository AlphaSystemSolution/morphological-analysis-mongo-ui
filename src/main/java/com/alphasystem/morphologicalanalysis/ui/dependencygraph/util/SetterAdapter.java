package com.alphasystem.morphologicalanalysis.ui.dependencygraph.util;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;

/**
 * @author sali
 */
public interface SetterAdapter<N extends GraphNode, A extends GraphNodeAdapter<N>> {
    void set(A node, Double value);
}
