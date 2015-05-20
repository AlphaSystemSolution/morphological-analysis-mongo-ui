package com.alphasystem.morphologicalanalysis.treebank.ui.dialog;

import com.alphasystem.ui.BaseDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author sali
 */
public class ImportTokenDialog extends BaseDialog {


    private static final long serialVersionUID = -8450238279138519022L;

    @Override
    protected boolean doOnOK(ActionEvent e) {
        return true;
    }

    @Override
    public JComponent createContentPanel() {
        return null;
    }
}
