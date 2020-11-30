package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.ui.RootLettersPickerKeyBoard;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalengine.ui.control.model.TableModel;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

import static com.alphasystem.morphologicalengine.ui.Global.createLabel;
import static com.alphasystem.morphologicalengine.ui.Global.createSpaceLabel;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

/**
 * @author sali
 */
public class RootLettersTableCell extends TableCell<TableModel, RootLetters> {

    private final Popup popup;
    private final RootLettersPickerKeyBoard keyBoard;

    public RootLettersTableCell(@SuppressWarnings({"unused"}) TableColumn<TableModel, RootLetters> column, ObjectProperty<Font> font) {
        setContentDisplay(GRAPHIC_ONLY);
        setAlignment(Pos.CENTER);
        setNodeOrientation(RIGHT_TO_LEFT);

        popup = new Popup();
        keyBoard = new RootLettersPickerKeyBoard();
        keyBoard.setFont(font.get());
        keyBoard.setSelectedLabelWidth(48);
        keyBoard.setSelectedLabelHeight(48);
        fontProperty().bind(font);
        keyBoard.fontProperty().bind(fontProperty());

        popup.getContent().add(keyBoard);
        popup.setAutoHide(true);
        popup.setOnHiding(event -> commitEdit());
        popup.setOnAutoHide(event -> commitEdit());
    }

    private void commitEdit() {
        commitEdit(keyBoard.getRootLetters());
    }

    @Override
    public void startEdit() {
        super.startEdit();
        final Bounds bounds = localToScreen(getBoundsInLocal());
        popup.show(this, bounds.getMinX(), bounds.getMinY() + bounds.getHeight());
    }

    @Override
    public void updateItem(RootLetters item, boolean empty) {
        super.updateItem(item, empty);

        Group label = null;
        if (item != null && !empty) {
            keyBoard.setRootLetters(item.getFirstRadical(), item.getSecondRadical(),
                    item.getThirdRadical(), item.getFourthRadical());

            TextFlow textFlow = new TextFlow();

            ArabicLetterType fourthRadical = item.getFourthRadical();
            textFlow.getChildren().addAll(createLabel(item.getFirstRadical()), createSpaceLabel(5),
                    createLabel(item.getSecondRadical()), createSpaceLabel(5), createLabel(item.getThirdRadical()));
            if (fourthRadical != null) {
                textFlow.getChildren().addAll(createSpaceLabel(5), createLabel(fourthRadical));
            }
            label = new Group(textFlow);
        }
        setGraphic(label);
    }

}
