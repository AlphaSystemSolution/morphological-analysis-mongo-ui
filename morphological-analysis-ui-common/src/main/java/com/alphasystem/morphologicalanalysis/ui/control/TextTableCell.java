package com.alphasystem.morphologicalanalysis.ui.control;

import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author sali
 */
public class TextTableCell<S, T extends ArabicSupport> extends TableCell<S, T> {

    @SuppressWarnings({"unused"}) private final TableColumn<S, T> column;
    private final Text text;

    public TextTableCell(TableColumn<S, T> column) {
        this.column = column;
        text = new Text();
        text.setTextAlignment(TextAlignment.RIGHT);
        text.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        text.fontProperty().bind(fontProperty());
        setFont(ApplicationHelper.PREFERENCES.getArabicFont24());
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null && !empty) {
            text.setText(item.toLabel().toUnicode());
        }
        setGraphic(text);
    }
}
