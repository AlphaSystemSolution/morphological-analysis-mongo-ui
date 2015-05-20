package com.alphasystem.morphologicalanalysis.treebank.ui.action;

import com.alphasystem.morphologicalanalysis.treebank.ui.panels.MATBContentPanel;
import com.alphasystem.ui.AbstractComponentAction;

/**
 * @author sali
 */
public abstract class MATBAction extends AbstractComponentAction {

    protected final MATBContentPanel contentPanel;

    public MATBAction(MATBContentPanel contentPanel) {
        super();
        this.contentPanel = contentPanel;
    }
}
