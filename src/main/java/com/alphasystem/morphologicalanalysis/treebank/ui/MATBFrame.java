package com.alphasystem.morphologicalanalysis.treebank.ui;

import com.alphasystem.morphologicalanalysis.treebank.ui.panels.MATBContentPanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author sali
 */
public class MATBFrame extends JFrame {

    private static final long serialVersionUID = 3669860863883691140L;

    public MATBFrame(){
        super("Morphological Analysis Tree Bank");

        MATBContentPanel contentPanel = new MATBContentPanel(this);
        setContentPane(contentPanel);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        pack();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
    }

    public void exit() {
        dispose();
        setVisible(false);
        System.exit(0);
    }
}
