package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisPreferences;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Verse;
import com.alphasystem.util.GenericPreferences;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.model.ArabicTool.getArabicNumberWord;
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
        MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
        label.setFont(preferences.getArabicFont24());
    }

    @Override
    protected void updateItem(Verse item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            int chapterNumber = item.getChapterNumber();
            int verseNumber = item.getVerseNumber();
            label.setText((chapterNumber <= -1 || verseNumber <= -1) ? "" : format(
                    "(%s:%s)", getArabicNumberWord(chapterNumber).toUnicode(),
                    getArabicNumberWord(verseNumber).toUnicode()));
        }
        setGraphic(label);
    }
}
