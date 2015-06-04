package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static java.lang.String.format;

/**
 * @author sali
 * 
 */
public class ChapterAdapter {

	private final Chapter chapter;

	/**
	 * @param chapter
	 */
	public ChapterAdapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public Chapter getChapter() {
		return chapter;
	}

	private String getChapterNumber() {
		int chapterNumber = chapter.getChapterNumber();
		return getArabicNumber(chapterNumber).toUnicode();
	}

	@Override
	public String toString() {
		return chapter == null ? "" : format("(%s) %s", getChapterNumber(),
				chapter.getChapterNameWord().toUnicode());
	}
}
