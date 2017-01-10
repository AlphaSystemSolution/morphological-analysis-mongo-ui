package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisPreferences;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.util.GenericPreferences;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.model.ArabicTool.getArabicNumberWord;
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
        MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
        label.setFont(preferences.getArabicFont24());
    }

    @Override
    protected void updateItem(Token item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            int chapterNumber = item.getChapterNumber();
            int verseNumber = item.getVerseNumber();
            Integer tokenNumber = item.getTokenNumber();
            label.setText(((chapterNumber <= -1) || (verseNumber <= -1) || (tokenNumber <= -1)) ? "" : format(
                    "(%s:%s:%s)", getArabicNumberWord(chapterNumber).toUnicode(),
                    getArabicNumberWord(verseNumber).toUnicode(),
                    getArabicNumberWord(tokenNumber).toUnicode()));
        }
        setGraphic(label);
    }
}
