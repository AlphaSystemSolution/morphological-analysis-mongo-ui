package com.alphasystem.morphologicalanalysis.treebank.ui;

import com.alphasystem.morphologicalanalysis.util.SpringContextHelper;

import static com.alphasystem.ui.UIUtilities.initLookAndFeel;
import static javax.swing.SwingUtilities.invokeLater;

/**
 * @author sali
 */
public class MorphologicalAnalysisTreeBankMain {

    static {
        initLookAndFeel();

        // initialize Spring context
        SpringContextHelper.getInstance();
    }

    public static void main(String[] args) {
        final MATBFrame frame = new MATBFrame();
        invokeLater(() -> frame.setVisible(true));
    }
}
