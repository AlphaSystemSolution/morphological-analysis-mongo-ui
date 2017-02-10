package com.alphasystem.morphologicalanalysis.ui.control;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisPreferences;
import com.alphasystem.util.GenericPreferences;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import java.util.List;

import static com.alphasystem.arabic.model.ArabicTool.getArabicNumberWord;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class VerseTokenPairGroupListCell extends ListCell<VerseTokenPairGroup> {

    private final Text label;

    public VerseTokenPairGroupListCell() {
        setContentDisplay(GRAPHIC_ONLY);
        label = new Text();
        MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
        label.setFont(preferences.getArabicFont24());
    }

    @Override
    protected void updateItem(VerseTokenPairGroup item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            List<VerseTokensPair> pairs = item.getPairs();
            if (pairs != null && !pairs.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                int size = pairs.size();
                builder.append(getArabicNumberWord(pairs.get(size - 1).getVerseNumber()).toUnicode());
                if (size > 1) {
                    builder.append(" - ").append(getArabicNumberWord(pairs.get(0).getVerseNumber()).toUnicode());
                }

                label.setText(builder.toString());
            }
        }
        setGraphic(label);
    }
}
