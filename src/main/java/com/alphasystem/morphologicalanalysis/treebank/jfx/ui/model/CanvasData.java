package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sali
 */
public class CanvasData {

    private List<GraphNode> nodes = new ArrayList<>();

    public boolean add(GraphNode graphNode) {
        return nodes.add(graphNode);
    }

    public void add(int index, GraphNode element) {
        nodes.add(index, element);
    }

    public GraphNode get(int index) {
        return nodes.get(index);
    }

    public GraphNode remove(int index) {
        return nodes.remove(index);
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }
}
