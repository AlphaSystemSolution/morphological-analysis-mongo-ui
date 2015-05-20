/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.util;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static java.lang.String.format;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.model.Location;

/**
 * @author sali
 * 
 */
public class LocationAdapter {

	private final Location location;

	/**
	 * @param location
	 */
	public LocationAdapter(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public String toString() {
		String result = null;
		if (location == null) {
			result = "";
		} else {
			Integer chapterNumber = location.getChapterNumber();
			Integer verseNumber = location.getVerseNumber();
			Integer tokenNumber = location.getTokenNumber();
			Integer locationIndex = location.getLocationNumber();
			if (chapterNumber != null && verseNumber != null
					&& tokenNumber != null && locationIndex != null) {
				ArabicWord chapterNumberWord = getArabicNumber(chapterNumber);
				ArabicWord verseNumberWord = getArabicNumber(verseNumber);
				ArabicWord tokenNumberWord = getArabicNumber(tokenNumber);
				ArabicWord locationIndexWord = getArabicNumber(locationIndex);
				result = format("(%s:%s:%s:%s)                     ",
						chapterNumberWord.toUnicode(),
						verseNumberWord.toUnicode(),
						tokenNumberWord.toUnicode(),
						locationIndexWord.toUnicode());
			}
			if (result == null) {
				result = format("%s          ", location.getDisplayName());
			}
		}
		return result;
	}
}
