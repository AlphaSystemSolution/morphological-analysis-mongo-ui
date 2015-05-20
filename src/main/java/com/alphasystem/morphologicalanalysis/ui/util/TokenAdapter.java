/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.util;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static java.lang.String.format;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.model.Token;

/**
 * @author sali
 * 
 */
public class TokenAdapter {

	private final Token token;

	/**
	 * @param token
	 */
	public TokenAdapter(Token token) {
		this.token = token;
	}

	public Token getToken() {
		return token;
	}

	@Override
	public String toString() {
		String result = null;
		if (token == null) {
			result = "";
		} else {
			Integer chapterNumber = token.getChapterNumber();
			Integer verseNumber = token.getVerseNumber();
			Integer tokenNumber = token.getTokenNumber();
			if (chapterNumber != null && verseNumber != null
					&& tokenNumber != null) {
				ArabicWord chapterNumberWord = getArabicNumber(chapterNumber);
				ArabicWord verseNumberWord = getArabicNumber(verseNumber);
				ArabicWord tokenNumberWord = getArabicNumber(tokenNumber);
				result = format("(%s:%s:%s)                     ",
						chapterNumberWord.toUnicode(),
						verseNumberWord.toUnicode(),
						tokenNumberWord.toUnicode());
			}
			if (result == null) {
				result = format("%s          ", token.getDisplayName());
			}
		}
		return result;
	}
}
