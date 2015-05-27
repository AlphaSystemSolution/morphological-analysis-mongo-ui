package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.morphologicalanalysis.model.Token;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.REFERENCE;

/**
 * @author sali
 */
public class ReferenceNode extends TerminalNode {

    /**
     * @param token
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public ReferenceNode(Token token, String id, double x, double y, double x1, double y1, double x2, double y2) {
        super(REFERENCE, token, id, x, y, x1, y1, x2, y2);
    }
}
