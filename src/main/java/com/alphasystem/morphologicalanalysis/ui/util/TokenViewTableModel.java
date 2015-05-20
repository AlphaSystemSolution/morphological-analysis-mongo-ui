/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.morphologicalanalysis.model.Verse;
import com.alphasystem.morphologicalanalysis.util.TokenComparator;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.util.Collections.sort;

/**
 * @author sali
 * 
 */
public class TokenViewTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -8064277892076927818L;

	public static final int MORPHOLOGICAL_DESCRIPTION_COLUMN_INDEX = 0;

	public static final int TOKEN_COLUMN_INDEX = 1;

	public static final int TOKEN_NUMBER_COLUMN_INDEX = 2;

	public static final int CHECK_BOX_COLUMN_INDEX = 3;

	private List<Token> tokens;

	private Boolean[] selected;

	public TokenViewTableModel(Verse verse) {
		tokens = verse.getTokens();
		sort(tokens, new TokenComparator());
		selected = new Boolean[tokens.size()];
		for (int i = 0; i < selected.length; i++) {
			selected[i] = FALSE;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> klass = super.getColumnClass(columnIndex);
		switch (columnIndex) {
		case CHECK_BOX_COLUMN_INDEX:
			klass = Boolean.class;
			break;
		default:
			break;
		}
		return klass;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return tokens.size();
	}

	public Token getSelectedToken(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= tokens.size()) {
			return null;
		}
		return tokens.get(rowIndex);
	}

	public List<Token> getSelectedTokens() {
		List<Token> selectedTokens = new ArrayList<>();
		for (int i = 0; i < getRowCount(); i++) {
			if (selected[i]) {
				selectedTokens.add(tokens.get(i));
				setValueAt(FALSE, i, CHECK_BOX_COLUMN_INDEX);
			}
		}
		return selectedTokens;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return columnIndex == CHECK_BOX_COLUMN_INDEX ? selected[rowIndex]
				: tokens.get(rowIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == CHECK_BOX_COLUMN_INDEX;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case CHECK_BOX_COLUMN_INDEX:
			selected[rowIndex] = (Boolean) aValue;
			break;
		default:
			break;
		}
	}

}
