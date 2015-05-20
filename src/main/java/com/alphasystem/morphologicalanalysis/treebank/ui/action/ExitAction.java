package com.alphasystem.morphologicalanalysis.treebank.ui.action;

import com.alphasystem.morphologicalanalysis.treebank.ui.panels.MATBContentPanel;

import java.awt.event.ActionEvent;

/**
 * @author sali
 */
public class ExitAction extends MATBAction {

    private static final long serialVersionUID = -1029756622064152092L;

    public ExitAction(MATBContentPanel contentPanel) {
        super(contentPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contentPanel.exit();
    }
}
