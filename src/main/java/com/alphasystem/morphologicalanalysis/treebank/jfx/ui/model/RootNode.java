package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.ROOT;

/**
 * @author sali
 */
public class RootNode extends GraphNode {

    private final NodeType childType;

    /**
     *
     * @param childType
     */
    public RootNode(NodeType childType) {
        super(ROOT, "dummy", childType.name(), -1.0, -1.0);
        this.childType = childType;

    }

    public NodeType getChildType() {
        return childType;
    }

    @Override
    public String toString() {
        return getText();
    }
}
