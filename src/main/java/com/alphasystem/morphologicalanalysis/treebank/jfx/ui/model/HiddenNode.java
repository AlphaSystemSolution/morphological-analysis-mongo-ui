package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.arabic.model.ArabicWord;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.HIDDEN;

/**
 * @author sali
 */
public class HiddenNode extends TerminalNode {

    /**
     * @param arabicWord
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public HiddenNode(ArabicWord arabicWord, String id, double x, double y, double x1, double y1, double x2, double y2) {
        super(HIDDEN, arabicWord.toUnicode(), id, x, y, x1, y1, x2, y2);
    }
}
