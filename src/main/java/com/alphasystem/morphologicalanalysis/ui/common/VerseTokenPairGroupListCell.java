package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.morphologicalanalysis.common.model.VerseTokensPair;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import java.util.List;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static com.alphasystem.arabic.ui.util.FontConstants.ARABIC_FONT_24;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class VerseTokenPairGroupListCell extends ListCell<VerseTokenPairGroup> {

    private final Text label;

    public VerseTokenPairGroupListCell() {
        setContentDisplay(GRAPHIC_ONLY);
        label = new Text();
        label.setFont(ARABIC_FONT_24);
    }

    @Override
    protected void updateItem(VerseTokenPairGroup item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            List<VerseTokensPair> pairs = item.getPairs();
            if (pairs != null && !pairs.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                int size = pairs.size();
                builder.append(getArabicNumber(pairs.get(size - 1).getVerseNumber()).toUnicode());
                if (size > 1) {
                    builder.append(" - ").append(getArabicNumber(pairs.get(0).getVerseNumber()).toUnicode());
                }

                label.setText(builder.toString());
            }
        }
        setGraphic(label);
    }
}
