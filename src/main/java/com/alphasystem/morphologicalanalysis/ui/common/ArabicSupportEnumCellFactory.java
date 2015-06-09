package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicSupportEnum;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.*;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_SMALL;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ENGLISH_FONT_SMALL;
import static java.lang.String.format;

/**
 * @author sali
 */
public class ArabicSupportEnumCellFactory<T extends ArabicSupportEnum> implements
        Callback<ListView<T>, ListCell<T>> {

    private final ListType type;

    public ArabicSupportEnumCellFactory() {
        this(ARABIC_AND_ENGLISH);
    }

    public ArabicSupportEnumCellFactory(ListType type) {
        this.type = type;
    }

    @Override
    public ListCell<T> call(ListView<T> param) {
        return new ListCell<T>() {

            private final Text codeText;
            private final Text arabicText;

            {
                codeText = new Text();
                codeText.setFont(ENGLISH_FONT_SMALL);
            }

            {
                arabicText = new Text();
                arabicText.setFont(ARABIC_FONT_SMALL);
            }

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                TextFlow textFlow = new TextFlow();
                Node graphic = null;
                if (item != null && !empty) {
                    codeText.setText(format("(%s)", item.getCode()));
                    arabicText.setText(item.getLabel().toUnicode());
                    if (type.equals(ARABIC_ONLY)) {
                        textFlow.getChildren().add(arabicText);
                    } else if (type.equals(ENGLISH_ONLY)) {
                        textFlow.getChildren().add(codeText);
                    } else {
                        textFlow.getChildren().addAll(codeText, arabicText);
                    }
                    graphic = new Group(textFlow);
                }
                setGraphic(graphic);
            }
        };
    }

    public enum ListType {
        ARABIC_ONLY, ENGLISH_ONLY, ARABIC_AND_ENGLISH
    }
}
