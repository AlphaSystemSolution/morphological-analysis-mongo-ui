package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_SMALL;
import static java.lang.String.format;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class VerseListCell extends ListCell<Verse> {

    private final Text label;

    public VerseListCell() {
        setContentDisplay(GRAPHIC_ONLY);
        label = new Text();
        label.setFont(ARABIC_FONT_SMALL);
    }

    @Override
    protected void updateItem(Verse item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            int chapterNumber = item.getChapterNumber();
            int verseNumber = item.getVerseNumber();
            label.setText((chapterNumber <= -1 || verseNumber <= -1) ? "" : format(
                    "(%s:%s)", getArabicNumber(chapterNumber).toUnicode(),
                    getArabicNumber(verseNumber).toUnicode()));
        }
        setGraphic(label);
    }
}
