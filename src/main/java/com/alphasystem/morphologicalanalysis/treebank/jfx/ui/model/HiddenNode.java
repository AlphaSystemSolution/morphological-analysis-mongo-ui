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
     * @param x3
     * @param y3
     */
    public HiddenNode(ArabicWord arabicWord, String id, Double x, Double y, Double x1, Double y1, Double x2,
                      Double y2, Double x3, Double y3) {
        super(HIDDEN, id, arabicWord.toUnicode(), x, y, x1, y1, x2, y2, x3, y3);
    }
}
