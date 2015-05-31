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
     * @param x3
     * @param y3
     */
    public EmptyNode(String text, String id, Double x, Double y, Double x1, Double y1, Double x2,
                     Double y2, Double x3, Double y3) {
        super(EMPTY, id, text, x, y, x1, y1, x2, y2, x3, y3);
    }
}
