package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicSupportEnum;
import com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.CODE_ONLY;
import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.LABEL_AND_CODE;
import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.LABEL_ONLY;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_SMALL;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ENGLISH_FONT_SMALL;
import static java.lang.String.format;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class ArabicSupportEnumListCell<T extends ArabicSupportEnum> extends ListCell<T> {

    private final Text codeText;
    private final Text arabicText;
    private final ListType type;

    public ArabicSupportEnumListCell() {
        this(LABEL_AND_CODE);
    }

    public ArabicSupportEnumListCell(ListType type) {
        this.type = type;
        setContentDisplay(GRAPHIC_ONLY);
        codeText = new Text();
        codeText.setFont(ENGLISH_FONT_SMALL);
        arabicText = new Text();
        arabicText.setFont(ARABIC_FONT_SMALL);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        TextFlow textFlow = new TextFlow();
        Node graphic = null;
        if (item != null && !empty) {
            codeText.setText(format("(%s) ", item.getCode()));
            arabicText.setText(item.getLabel().toUnicode());
            if (type.equals(LABEL_ONLY)) {
                textFlow.getChildren().add(arabicText);
            } else if (type.equals(CODE_ONLY)) {
                textFlow.getChildren().add(codeText);
            } else {
                textFlow.getChildren().addAll(codeText, arabicText);
            }
            graphic = new Group(textFlow);
        }
        setGraphic(graphic);
    }
}
