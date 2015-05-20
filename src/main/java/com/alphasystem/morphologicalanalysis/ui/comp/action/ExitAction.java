/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.comp.action;

import static java.awt.event.InputEvent.ALT_MASK;
import static java.awt.event.KeyEvent.VK_X;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import com.alphasystem.morphologicalanalysis.ui.comp.MorphologicalAnalysisContentPanel;

/**
 * @author sali
 * 
 */
public class ExitAction extends MorphologicalAnalysisUiAction {

	private static final long serialVersionUID = -8057007091856629373L;

	/**
	 * @param contentPanel
	 */
	public ExitAction(MorphologicalAnalysisContentPanel contentPanel) {
		super(contentPanel);
		setKeyStroke(KeyStroke.getKeyStroke(VK_X, ALT_MASK));
		setMnemonic(VK_X);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		contentPanel.exit();
	}

}
