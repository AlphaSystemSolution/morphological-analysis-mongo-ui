/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.jidesoft.swing.StyledLabel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_BOLD_20;
import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_PLAIN_28;
import static com.alphasystem.morphologicalanalysis.ui.util.TokenViewTableModel.*;
import static com.jidesoft.swing.StyledLabelBuilder.setStyledText;
import static java.awt.ComponentOrientation.RIGHT_TO_LEFT;
import static java.lang.String.format;

/**
 * @author sali
 * 
 */
public class TokenViewTableCellRendrer extends StyledLabel implements
		TableCellRenderer {

	private static final long serialVersionUID = 1014102293928133157L;

	public TokenViewTableCellRendrer() {
		setOpaque(true);
		setComponentOrientation(RIGHT_TO_LEFT);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		setHorizontalTextPosition(CENTER);
		setVerticalTextPosition(CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Token token = (Token) value;
		Font font = ARABIC_FONT_PLAIN_28;
		String text = "";
		switch (column) {
		case MORPHOLOGICAL_DESCRIPTION_COLUMN_INDEX:
			break;
		case TOKEN_COLUMN_INDEX:
			text = token.getTokenWord().toUnicode();
			break;
		case TOKEN_NUMBER_COLUMN_INDEX:
			font = ARABIC_FONT_BOLD_20;
			text = getTokenNumber(token);
			break;
		default:
			break;
		}
		setStyledText(this, text);
		setFont(font);
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}

		return this;
	}

	private String getTokenNumber(Token token) {
		String chapterNumber = getArabicNumber(token.getChapterNumber())
				.toUnicode();
		String verseNumber = getArabicNumber(token.getVerseNumber())
				.toUnicode();
		String tokenNumber = getArabicNumber(token.getTokenNumber())
				.toUnicode();
		return format("(%s:%s:%s)", chapterNumber, verseNumber, tokenNumber);
	}

}
