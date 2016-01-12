package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static com.alphasystem.fx.ui.util.FontConstants.ARABIC_FONT_24;
import static java.lang.String.format;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class TokenNumberListCell extends ListCell<Token> {

    private final Text label;

    public TokenNumberListCell() {
        setContentDisplay(GRAPHIC_ONLY);
        label = new Text();
        label.setFont(ARABIC_FONT_24);
    }

    @Override
    protected void updateItem(Token item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            int chapterNumber = item.getChapterNumber();
            int verseNumber = item.getVerseNumber();
            Integer tokenNumber = item.getTokenNumber();
            label.setText((chapterNumber <= -1 || verseNumber <= -1 || tokenNumber <= -1) ? "" : format(
                    "(%s:%s:%s)", getArabicNumber(chapterNumber).toUnicode(),
                    getArabicNumber(verseNumber).toUnicode(),
                    getArabicNumber(tokenNumber).toUnicode()));
        }
        setGraphic(label);
    }
}
