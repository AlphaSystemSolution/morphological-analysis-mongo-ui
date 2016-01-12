package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static com.alphasystem.fx.ui.util.FontConstants.ARABIC_FONT_24;
import static java.lang.String.format;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class LocationListCell extends ListCell<Location> {

    private final Text label;

    public LocationListCell() {
        setContentDisplay(GRAPHIC_ONLY);
        label = new Text();
        label.setFont(ARABIC_FONT_24);
    }

    @Override
    protected void updateItem(Location item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            String result = null;
            Integer chapterNumber = item.getChapterNumber();
            Integer verseNumber = item.getVerseNumber();
            Integer tokenNumber = item.getTokenNumber();
            Integer locationIndex = item.getLocationNumber();
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
                result = format("%s          ", item.getDisplayName());
            }
            label.setText(result);
        }
        setGraphic(label);
    }
}
