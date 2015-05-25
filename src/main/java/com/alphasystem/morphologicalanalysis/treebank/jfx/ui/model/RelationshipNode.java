package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.RELATIONSHIP;

/**
 * @author sali
 */
public class RelationshipNode extends GraphNode {

    public RelationshipNode(String text, double x, double y) {
        super(RELATIONSHIP, text, x, y);
    }
}
