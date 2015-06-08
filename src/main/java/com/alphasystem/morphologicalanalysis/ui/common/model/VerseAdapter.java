/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.common.model;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static java.lang.String.format;

/**
 * @author sali
 * 
 */
public class VerseAdapter {

	private final int chapterNumber;

	private final int verseNumber;

	/**
	 * @param chapterNumber
	 * @param verseNumber
	 */
	public VerseAdapter(int chapterNumber, int verseNumber) {
		this.chapterNumber = chapterNumber;
		this.verseNumber = verseNumber;
	}

	public int getChapterNumber() {
		return chapterNumber;
	}

	public int getVerseNumber() {
		return verseNumber;
	}

	@Override
	public String toString() {
		return (chapterNumber <= -1 || verseNumber <= -1) ? "" : format(
				"(%s:%s)", getArabicNumber(chapterNumber).toUnicode(),
				getArabicNumber(verseNumber).toUnicode());
	}
}
