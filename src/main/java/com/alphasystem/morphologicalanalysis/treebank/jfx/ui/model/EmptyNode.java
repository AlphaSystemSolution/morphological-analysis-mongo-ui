package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.EMPTY;

/**
 * @author sali
 */
public class EmptyNode extends TerminalNode {

    /**
     * @param text
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public EmptyNode(String text, String id, double x, double y, double x1, double y1, double x2, double y2) {
        super(EMPTY, id, text, x, y, x1, y1, x2, y2);
    }
}
