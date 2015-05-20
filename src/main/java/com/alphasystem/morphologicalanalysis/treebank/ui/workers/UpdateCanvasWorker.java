package com.alphasystem.morphologicalanalysis.treebank.ui.workers;

import com.alphasystem.morphologicalanalysis.treebank.ui.panels.MATBContentPanel;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * @author sali
 */
public class UpdateCanvasWorker extends SwingWorker<Void, Boolean> {

    private final MATBContentPanel contentPanel;

    public UpdateCanvasWorker(MATBContentPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    @Override
    protected Void doInBackground() throws Exception {
        publish(true);
        return null;
    }

    @Override
    protected void process(List<Boolean> chunks) {
        contentPanel.updateUI(true);
        contentPanel.saveFile(new File("test.svg"));
        contentPanel.updateCursor(false);
    }
}
