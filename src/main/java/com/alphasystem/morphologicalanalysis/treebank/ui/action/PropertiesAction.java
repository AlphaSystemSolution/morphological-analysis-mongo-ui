package com.alphasystem.morphologicalanalysis.treebank.ui.action;

import com.alphasystem.morphologicalanalysis.treebank.ui.panels.MATBContentPanel;
import com.alphasystem.ui.AbstractComponentAction;

import java.awt.event.ActionEvent;

/**
 * @author sali
 */
public class PropertiesAction extends AbstractComponentAction {

    private final MATBContentPanel contentPanel;

    public PropertiesAction(final MATBContentPanel contentPanel){
        super();
        this.contentPanel = contentPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = getActionCommand();
    }
}
