package com.alphasystem.morphologicalanalysis.treebank.ui.dialog;

import com.alphasystem.morphologicalanalysis.treebank.model.Relationship;
import com.alphasystem.morphologicalanalysis.treebank.model.Text;
import com.alphasystem.morphologicalanalysis.treebank.ui.support.TreeBankUserObject;
import com.alphasystem.ui.BaseDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static com.alphasystem.util.AppUtil.isGivenClass;

/**
 * @author sali
 */
public class TreeBankNodeEditorDialog extends BaseDialog {

    private JPanel contentPanel;

    private JPanel textDataPanel;

    private JPanel relationshipDataPanel;

    private TreeBankUserObject userObject;

    public TreeBankNodeEditorDialog(){

    }

    @Override
    protected boolean doOnOK(ActionEvent e) {
        return true;
    }

    @Override
    public JComponent createContentPanel() {
        setInitFocusedComponent(okButton);
        return null;
    }

    public void updateDialog(TreeBankUserObject tbuo) {
        this.userObject = tbuo;
        Object underlyingObject = tbuo.getUserObject();
        if (isGivenClass(Text.class, underlyingObject)) {
            updateDialog((Text) underlyingObject);
        } else if (isGivenClass(Relationship.class, underlyingObject)) {
            updateDialog((Relationship) underlyingObject);
        }
    }

    private void updateDialog(Text text) {

    }

    private void updateDialog(Relationship relationship) {

    }
}
