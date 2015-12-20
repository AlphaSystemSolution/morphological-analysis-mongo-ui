package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Chapter;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static com.alphasystem.arabic.ui.util.FontConstants.ARABIC_FONT_24;
import static java.lang.String.format;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class ChapterListCell extends ListCell<Chapter> {

    private final Text label;

    public ChapterListCell() {
        setContentDisplay(GRAPHIC_ONLY);
        label = new Text();
        label.setFont(ARABIC_FONT_24);
    }

    private static String getChapterNumber(Chapter chapter) {
        int chapterNumber = chapter.getChapterNumber();
        return getArabicNumber(chapterNumber).toUnicode();
    }

    @Override
    protected void updateItem(Chapter item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            label.setText(format("(%s) %s", getChapterNumber(item), item.getChapterNameWord().toUnicode()));
        }
        setGraphic(label);
    }
}
