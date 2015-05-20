/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui;

import static com.alphasystem.morphologicalanalysis.util.SpringContextHelper.getInstance;
import static com.alphasystem.ui.UIUtilities.initLookAndFeel;
import static javax.swing.SwingUtilities.invokeLater;

/**
 * @author sali
 * 
 */
public class MorphologicalAnalysisMain {

	static {
		initLookAndFeel();

		// initialize Spring context
		getInstance();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final MorphologicalAnalysisFrame frame = new MorphologicalAnalysisFrame();
		invokeLater(() -> frame.setVisible(true));
	}
}
