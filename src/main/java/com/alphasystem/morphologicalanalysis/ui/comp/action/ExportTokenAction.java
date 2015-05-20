/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.comp.action;

import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.KeyEvent.VK_X;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import com.alphasystem.morphologicalanalysis.ui.comp.MorphologicalAnalysisContentPanel;

/**
 * @author sali
 * 
 */
public class ExportTokenAction extends MorphologicalAnalysisUiAction {

	private static final long serialVersionUID = -7080537172935598129L;

	public ExportTokenAction(MorphologicalAnalysisContentPanel contentPanel) {
		super(contentPanel);
		setKeyStroke(KeyStroke.getKeyStroke(VK_X, CTRL_MASK));
		setMnemonic(VK_X);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		contentPanel.exportTokens();
	}

}
