package com.alphasystem.morphologicalanalysis.treebank.ui.support;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

import static java.awt.Font.PLAIN;

/**
 * @author sali
 */
public class TreeBankNodeCellRenderer extends JLabel implements TreeCellRenderer {

    private static final long serialVersionUID = -7078841516591142979L;

    public TreeBankNodeCellRenderer(){
        // MUST do this for background to show up.
        setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        TreeBankUserObject userObject = (TreeBankUserObject) value;
        setText(userObject.toString());
        setFont(new Font("Traditional Arabic", PLAIN, 20));
        setForeground(tree.getForeground());
        setBackground(tree.getBackground());
        return this;
    }
}
