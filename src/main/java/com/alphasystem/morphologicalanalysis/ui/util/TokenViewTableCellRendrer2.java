/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.util;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_BOLD_20;
import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_NAME;
import static java.lang.String.format;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.alphasystem.morphologicalanalysis.model.Token;

/**
 * @author sali
 * 
 */
public class TokenViewTableCellRendrer2 extends JLabel implements
		TableCellRenderer {

	private static final long serialVersionUID = 1014102293928133157L;

	private static final int ARABIC_FONT_SIZE = 24;

	public TokenViewTableCellRendrer2() {
		setOpaque(true);
		setPreferredSize(new Dimension(100, 60));
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

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Token token = (Token) value;
		String text = "";
		if (column == 0) {
			text = getTokenNumber(token);
			setFont(ARABIC_FONT_BOLD_20);
		} else if (column == 1) {

		} else if (column == 2) {
			setHorizontalAlignment(RIGHT);
			setVerticalAlignment(CENTER);
			setHorizontalTextPosition(RIGHT);
			setVerticalTextPosition(CENTER);
			text = format(
					"<html><nobr><span style=\"font-size:%s; font-family:%s\">%s</span></nobr></html>",
					ARABIC_FONT_SIZE, ARABIC_FONT_NAME, token.getTokenWord()
							.toHtmlCode());
		}
		setText(text);

		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}

		return this;
	}
}
