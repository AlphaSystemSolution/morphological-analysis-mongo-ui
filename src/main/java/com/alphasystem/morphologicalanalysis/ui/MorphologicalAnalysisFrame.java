/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui;

import com.alphasystem.morphologicalanalysis.ui.comp.MorphologicalAnalysisContentPanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author sali
 * 
 */
public class MorphologicalAnalysisFrame extends JFrame {

	private static final long serialVersionUID = 3600880089397372662L;

	public MorphologicalAnalysisFrame() {
		super("Morphological Analysis");

		MorphologicalAnalysisContentPanel contentPanel = new MorphologicalAnalysisContentPanel(
				this);
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
