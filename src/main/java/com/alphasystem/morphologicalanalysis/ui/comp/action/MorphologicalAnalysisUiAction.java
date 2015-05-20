/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.comp.action;

import com.alphasystem.morphologicalanalysis.ui.comp.MorphologicalAnalysisContentPanel;
import com.alphasystem.ui.AbstractComponentAction;

/**
 * @author sali
 * 
 */
public abstract class MorphologicalAnalysisUiAction extends
		AbstractComponentAction {

	private static final long serialVersionUID = 6610618067492109613L;
	
	protected MorphologicalAnalysisContentPanel contentPanel;

	public MorphologicalAnalysisUiAction(
			MorphologicalAnalysisContentPanel contentPanel) {
		super();
		this.contentPanel = contentPanel;
	}
}
