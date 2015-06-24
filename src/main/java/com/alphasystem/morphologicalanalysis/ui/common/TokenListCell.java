package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_SMALL;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class TokenListCell extends ListCell<Token> {

    private final Text label;

    public TokenListCell() {
        setContentDisplay(GRAPHIC_ONLY);
        label = new Text();
        label.setFont(ARABIC_FONT_SMALL);
    }

    @Override
    protected void updateItem(Token item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            label.setText(item.getTokenWord().toUnicode());
        }
        setGraphic(label);
    }
}
