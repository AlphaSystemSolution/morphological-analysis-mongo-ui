/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.comp;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.jidesoft.swing.JideToggleButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_PLAIN_28;
import static com.alphasystem.ui.util.SpringUtilities.makeCompactGrid;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.awt.Color.BLACK;
import static java.awt.Font.PLAIN;
import static javax.swing.BorderFactory.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 * 
 */
public class TokenWordPreviewPanel extends JPanel {

	private static final long serialVersionUID = -7224144061955965087L;

	private JideToggleButton[] buttons;

	private JPanel innerPanel;

	private JTextArea transaltionField;

	public TokenWordPreviewPanel(ArabicLetter[] letters) {
		super(new BorderLayout());

		innerPanel = new JPanel(new SpringLayout());
		int cols = loadButtons(letters);
		makeCompactGrid(innerPanel, 1, cols);
		Border emptyBorder = createEmptyBorder(6, 6, 6, 6);
		innerPanel.setBorder(createCompoundBorder(emptyBorder,
				createEtchedBorder()));

		setBorder(createCompoundBorder(createLineBorder(BLACK), emptyBorder));
		add(innerPanel, CENTER);

		transaltionField = new JTextArea(2, 2);
		transaltionField.setFont(new Font("Candara", PLAIN, 16));
		JPanel translationPanel = new JPanel(new BorderLayout());
		translationPanel.add(new JScrollPane(transaltionField), CENTER);
		translationPanel.setBorder(createCompoundBorder(emptyBorder,
				createTitledBorder("Translation")));
		add(translationPanel, SOUTH);
	}

	private int loadButtons(ArabicLetter[] letters) {
		int len = letters.length;
		int cols = 0;
		buttons = new JideToggleButton[len];

		for (int i = len - 1; i >= 0; i--) {
			JideToggleButton button = new JideToggleButton(
					letters[i].toUnicode());
			buttons[i] = button;
			button.setFont(ARABIC_FONT_PLAIN_28);

			Dimension size = new Dimension(64, 64);
			button.setPreferredSize(size);
			button.setMaximumSize(size);
			button.setMinimumSize(size);

			innerPanel.add(button);
			cols++;
		}
		return cols;
	}

	/**
	 * Select button(s) according to this location.
	 * 
	 * @param location
	 * @param letters
	 */
	void selectButtons(Location location, ArabicLetter[] letters) {
		updateTranslation(location);
		for (JideToggleButton button : buttons) {
			button.setSelected(false);
		}
		int startIndex = location.getStartIndex();
		int endIndex = location.getEndIndex();
		if (startIndex == 0 && endIndex == 0) {
			return;
		}
		for (int i = startIndex; i < endIndex; i++) {
			buttons[i].setSelected(true);
		}
	}

	/**
	 * Called upon loading new dialog to update button texts. This method will
	 * not select the buttons according to this location.
	 * 
	 * @param letters
	 */
	void updateButtons(ArabicLetter[] letters) {
		innerPanel.removeAll();
		int cols = loadButtons(letters);
		makeCompactGrid(innerPanel, 1, cols);
	}

	/**
	 * Updates the current location with the <code>startIndex</code>,
	 * <code>endIndex</code>, and <code>translation</code>.
	 * 
	 * Called upon saving of this location.
	 * 
	 * @param location
	 */
	void updateLocation(Location location) {
		int startIndex = -1;
		int endIndex = 0;
		for (int i = 0; i < buttons.length; i++) {
			JideToggleButton button = buttons[i];
			if (button.isSelected()) {
				if (startIndex == -1) {
					startIndex = i;
				}
				endIndex = i + 1;
			}
		}
		location.setStartIndex(startIndex);
		location.setEndIndex(endIndex);

		String translation = transaltionField.getText();
		if (!isBlank(translation)) {
			location.setTranslation(translation);
		}
	}

	private void updateTranslation(Location location) {
		String translation = location.getTranslation();
		translation = isBlank(translation) ? "" : translation;
		transaltionField.setText(translation);
	}

}
