package com.alphasystem.morphologicalanalysis.ui.comp;

import com.alphasystem.arabic.model.ArabicWord;
import com.jidesoft.swing.JideToggleButton;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static com.alphasystem.arabic.model.ArabicWord.fromUnicode;
import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_BOLD_20;
import static java.awt.Color.BLACK;
import static java.awt.Color.RED;
import static java.lang.String.valueOf;

/**
 * @author sali
 * 
 */
public class ArabicWordLabel extends JideToggleButton {

	private static final long serialVersionUID = -929262915397792839L;

	public static final Border DEFAULT_BORDER = BorderFactory
			.createLineBorder(BLACK);

	public static final Border SELECTED_BORDER = BorderFactory
			.createLineBorder(RED, 2);

	private static final int WIDTH = 72;

	private static final int HEIGHT = 72;

	private static final Dimension DEFAULT_DIMENSION = new Dimension(WIDTH,
			HEIGHT);

	private ArabicWord arabicWord;

	private boolean useHtmlCode;

	public ArabicWordLabel() {
		this(null);
	}

	public ArabicWordLabel(ArabicWord arabicWord) {
		this(arabicWord, false);
	}

	public ArabicWordLabel(ArabicWord arabicWord, boolean useHtmlCode) {
		super();
		this.useHtmlCode = useHtmlCode;
		setButtonStyle(TOOLBAR_STYLE);
		setArabicWord(arabicWord);
		initLabel();

	}

	public ArabicWord getArabicWord() {
		return arabicWord;
	}

	private void initLabel() {
		setMaximumSize(DEFAULT_DIMENSION);
		setPreferredSize(DEFAULT_DIMENSION);
		setMinimumSize(DEFAULT_DIMENSION);
		setHorizontalAlignment(CENTER);
		setHorizontalTextPosition(CENTER);
		setVerticalAlignment(CENTER);
		setVerticalTextPosition(CENTER);
		setFont(ARABIC_FONT_BOLD_20);
		setBorder(DEFAULT_BORDER);
	}

	public void setArabicWord(ArabicWord arabicWord) {
		this.arabicWord = arabicWord;
		String text = this.arabicWord == null ? ""
				: valueOf(useHtmlCode ? arabicWord.toHtmlCode() : arabicWord
						.toUnicode());
		super.setText(text);
	}

	@Override
	public void setText(String text) {
		String value = null;
		if (StringUtils.isBlank(text) || text.length() > 1) {
			value = "";
		} else {
			arabicWord = fromUnicode(text);
			value = arabicWord == null ? "" : valueOf(useHtmlCode ? arabicWord
					.toHtmlCode() : arabicWord.toUnicode());
		}
		super.setText(value);
	}
}
