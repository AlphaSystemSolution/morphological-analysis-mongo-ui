package com.alphasystem.morphologicalanalysis.treebank.ui.support;

import javax.swing.*;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author sali
 */
public class TreeBankNodeCellEditor extends AbstractCellEditor
        implements TreeCellEditor, ActionListener {

    private static final long serialVersionUID = 2968432029924296533L;

    private TreeBankUserObject current;

    private static final String EDIT = "edit";

    private JButton editorComponent;

    public TreeBankNodeCellEditor() {

        // Set up the editor (from the tree's point of view), which is a
        // button. This button brings up the dialog, which is the
        // editor from the user's point of view.
        editorComponent = new JButton();
        editorComponent.setActionCommand(EDIT);
        editorComponent.addActionListener(this);
        editorComponent.setBorderPainted(false);
    }

    @Override
    public Object getCellEditorValue() {
        return current;
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected,
                                                boolean expanded, boolean leaf, int row) {
        current = (TreeBankUserObject) value;
        return editorComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (EDIT.equals(actionCommand)) {

            fireEditingStopped();
        }
    }
}
