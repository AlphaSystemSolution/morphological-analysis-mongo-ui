package com.alphasystem.morphologicalanalysis.ui.comp;

import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_BOLD_20;
import static java.awt.Color.BLACK;
import static java.awt.Color.RED;
import static java.lang.String.valueOf;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import org.apache.commons.lang3.StringUtils;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.jidesoft.swing.JideButton;

/**
 * @author sali
 * 
 */
public class RootWordLabel extends JideButton {

	private static final long serialVersionUID = -929262915397792839L;

	public static final Border DEFAULT_BORDER = BorderFactory
			.createLineBorder(BLACK);

	public static final Border SELECTED_BORDER = BorderFactory
			.createLineBorder(RED, 2);

	private static final int WIDTH = 40;

	private static final int HEIGHT = 40;

	private static final Dimension DEFAULT_DIMENSION = new Dimension(WIDTH,
			HEIGHT);

	private ArabicLetterType rootWord;

	public RootWordLabel() {
		this(null);
	}

	public RootWordLabel(ArabicLetterType rootWord) {
		super();
		setButtonStyle(TOOLBAR_STYLE);
		setRootWord(rootWord);
		initLabel();

	}

	public ArabicLetterType getRootWord() {
		return rootWord;
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

	public void setRootWord(ArabicLetterType rootWord) {
		this.rootWord = rootWord;
		String text = this.rootWord == null ? "" : valueOf(rootWord
				.getUnicode());
		super.setText(text);
	}

	@Override
	public void setText(String text) {
		String value = null;
		if (StringUtils.isBlank(text) || text.length() > 1) {
			value = "";
		} else {
			ArabicLetterType al = ArabicLetterType.getByUnicode(text.charAt(0));
			value = al == null ? "" : valueOf(al.toUnicode());
		}
		super.setText(value);
	}
}
