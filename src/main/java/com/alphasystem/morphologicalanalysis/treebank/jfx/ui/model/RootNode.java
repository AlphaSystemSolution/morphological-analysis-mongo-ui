package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.morphologicalanalysis.graph.model.support.DependencyGraphNodeType;

import static com.alphasystem.morphologicalanalysis.graph.model.support.DependencyGraphNodeType.ROOT;

/**
 * @author sali
 */
public class RootNode extends GraphNode {

    private final DependencyGraphNodeType childType;

    /**
     *
     * @param childType
     */
    public RootNode(DependencyGraphNodeType childType) {
        super(ROOT, "dummy", childType.name(), -1.0, -1.0, 0.0, 0.0);
        this.childType = childType;

    }

    public DependencyGraphNodeType getChildType() {
        return childType;
    }

    @Override
    public String toString() {
        return getText();
    }
}
