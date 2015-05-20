/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.comp;

import static com.alphasystem.arabic.model.ArabicLetterType.getByUnicode;
import static com.alphasystem.morphologicalanalysis.ui.comp.RootWordLabel.DEFAULT_BORDER;
import static com.alphasystem.morphologicalanalysis.ui.comp.RootWordLabel.SELECTED_BORDER;
import static com.alphasystem.ui.util.SpringUtilities.makeCompactGrid;
import static com.alphasystem.util.AppUtil.getIcon;
import static java.awt.BorderLayout.CENTER;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.model.DiacriticType;
import com.alphasystem.arabickeyboard.ui.ArabicJideKeyboardPanel;
import com.alphasystem.arabickeyboard.ui.KeyboardButtonHelper;
import com.alphasystem.arabickeyboard.ui.KeyboardTarget;
import com.alphasystem.morphologicalanalysis.model.support.RootWord;
import com.alphasystem.ui.AbstractComponentAction;
import com.jidesoft.popup.JidePopup;

/**
 * @author sali
 * 
 */
public class RootWordSelectionPanel extends JPanel implements KeyboardTarget {

	private class ButtonSelectionListener extends AbstractAction {

		private static final long serialVersionUID = -2646737387313771740L;

		@Override
		public void actionPerformed(ActionEvent e) {
			highlightLabel(selectedLabel, false);
			selectedLabel = (RootWordLabel) e.getSource();
			highlightLabel(selectedLabel, true);
		}

	}

	private static final long serialVersionUID = -6322605817072284660L;

	private static final int HEIGHT = 40;

	private JidePopup keyboardPopup;

	private JButton keyboardButton;

	private RootWordLabel firstRadicalLabel;

	private RootWordLabel secondRadicalLabel;

	private RootWordLabel thirdRadicalLabel;

	private RootWordLabel fourthRadicalLabel;

	private RootWordLabel selectedLabel;

	private RootWord rootWord;

	public RootWordSelectionPanel(RootWord rootWord) {
		super(new BorderLayout());

		initKeyboardButton();
		KeyboardButtonHelper.getInstance(this);
		initKeyboardPopup();
		updatePanel(rootWord);

		JPanel contentPanel = new JPanel(new SpringLayout());
		contentPanel.add(fourthRadicalLabel);
		contentPanel.add(thirdRadicalLabel);
		contentPanel.add(secondRadicalLabel);
		contentPanel.add(firstRadicalLabel);
		contentPanel.add(keyboardButton);
		makeCompactGrid(contentPanel, 1, 5);

		add(contentPanel, CENTER);
	}

	private RootWordLabel createRootWordLabel(ArabicLetterType arabicLetterType) {
		RootWordLabel rootWordLabel = new RootWordLabel(arabicLetterType);
		rootWordLabel.addActionListener(new ButtonSelectionListener());
		return rootWordLabel;
	}

	RootWord getRootWord() {
		ArabicLetterType firstRadical = firstRadicalLabel.getRootWord();
		ArabicLetterType secondRadical = secondRadicalLabel.getRootWord();
		ArabicLetterType thirdRadical = thirdRadicalLabel.getRootWord();
		ArabicLetterType fourthRadical = fourthRadicalLabel.getRootWord();
		if (firstRadical != null && secondRadical != null
				&& thirdRadical != null) {
			rootWord.setFirstRadical(firstRadical);
			rootWord.setSecondRadical(secondRadical);
			rootWord.setThirdRadical(thirdRadical);
			rootWord.setFourthRadical(fourthRadical);
			rootWord.updateDisplayName();
		}
		return rootWord;
	}

	private void highlightLabel(RootWordLabel label, boolean highlight) {
		if (label != null) {
			if (highlight) {
				label.setBorder(SELECTED_BORDER);
			} else {
				label.setBorder(DEFAULT_BORDER);
			}
		}
	}

	private void initKeyboardButton() {
		keyboardButton = new JButton(new AbstractComponentAction("",
				getIcon("images.keyboard-icon.png")) {

			private static final long serialVersionUID = -4064842916384713264L;

			@Override
			public void actionPerformed(ActionEvent e) {
				showHidePopup();
			}
		});

		Dimension size = keyboardButton.getPreferredSize();
		keyboardButton.setPreferredSize(new Dimension(size.width, HEIGHT));

		size = keyboardButton.getMaximumSize();
		keyboardButton.setMaximumSize(new Dimension(size.width, HEIGHT));

		size = keyboardButton.getMinimumSize();
		keyboardButton.setMinimumSize(new Dimension(size.width, HEIGHT));
	}

	private void initKeyboardPopup() {
		keyboardPopup = new JidePopup();
		keyboardPopup.setMovable(true);
		Container contentPane = keyboardPopup.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new ArabicJideKeyboardPanel(), CENTER);
		keyboardPopup.setOwner(keyboardButton);
		keyboardPopup.setResizable(true);
	}

	@Override
	public void insertLetter(ArabicLetterType arabicLetter) {
	}

	@Override
	public void insertLetter(char unicode) {
	}

	@Override
	public void insertLetter(DiacriticType diacriticType) {
	}

	@Override
	public void insertLetter(String unicode) {
		if (selectedLabel != null) {
			selectedLabel.setRootWord(getByUnicode(unicode.charAt(0)));
			selectNextLabel();
		}
	}

	private void selectNextLabel() {
		if (selectedLabel != null) {
			RootWordLabel nextLabel = null;
			if (firstRadicalLabel == selectedLabel) {
				nextLabel = secondRadicalLabel;
			}
			if (secondRadicalLabel == selectedLabel) {
				nextLabel = thirdRadicalLabel;
			}
			if (thirdRadicalLabel == selectedLabel) {
				nextLabel = fourthRadicalLabel;
			}
			if (fourthRadicalLabel == selectedLabel) {
				nextLabel = firstRadicalLabel;
			}

			if (nextLabel != null) {
				nextLabel.doClick();
			}
		}
	}

	private void showHidePopup() {
		if (keyboardPopup.isPopupVisible()) {
			keyboardPopup.hidePopup();
		} else {
			keyboardPopup.showPopup();
		}
	}

	void updatePanel(RootWord rootWord) {
		this.rootWord = rootWord == null ? new RootWord() : rootWord;

		ArabicLetterType al = this.rootWord.getFirstRadical();
		if (firstRadicalLabel == null) {
			firstRadicalLabel = createRootWordLabel(al);
		}
		firstRadicalLabel.setRootWord(al);

		al = this.rootWord.getSecondRadical();
		if (secondRadicalLabel == null) {
			secondRadicalLabel = createRootWordLabel(al);
		}
		secondRadicalLabel.setRootWord(al);

		al = this.rootWord.getThirdRadical();
		if (thirdRadicalLabel == null) {
			thirdRadicalLabel = createRootWordLabel(al);
		}
		thirdRadicalLabel.setRootWord(al);

		al = this.rootWord.getFourthRadical();
		if (fourthRadicalLabel == null) {
			fourthRadicalLabel = createRootWordLabel(al);
		}
		fourthRadicalLabel.setRootWord(al);

		firstRadicalLabel.doClick();
	}
}
