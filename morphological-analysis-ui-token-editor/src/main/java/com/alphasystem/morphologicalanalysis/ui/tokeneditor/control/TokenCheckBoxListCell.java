package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.model.TokenCellModel;
import com.alphasystem.morphologicalanalysis.ui.util.MorphologicalAnalysisPreferences;
import com.alphasystem.util.GenericPreferences;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * @author sali
 */
public class TokenCheckBoxListCell extends ListCell<TokenCellModel> {

    private static MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
    private final CheckBox checkBox;
    private final TextFlow label;

    public TokenCheckBoxListCell() {
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        checkBox = new CheckBox();
        label = new TextFlow();
        label.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
    }

    @Override
    protected void updateItem(TokenCellModel item, boolean empty) {
        super.updateItem(item, empty);

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(5);
        flowPane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        if (item != null && !empty) {
            createLabel(item);
            flowPane.getChildren().addAll(checkBox, label);

            final BooleanProperty booleanProperty = item.checkedProperty();
            if (booleanProperty != null) {
                checkBox.selectedProperty().unbindBidirectional(booleanProperty);
                checkBox.selectedProperty().bindBidirectional(booleanProperty);
            }
        }

        setGraphic(flowPane);
    }

    private void createLabel(TokenCellModel item) {
        label.getChildren().remove(0, label.getChildren().size());
        Text tokenText = new Text(item.getText());
        tokenText.setFont(preferences.getArabicFont30());

        Text tokenNumber = new Text(item.getDisplayName());
        tokenNumber.setFont(preferences.getArabicFont24());

        label.getChildren().addAll(tokenNumber, new Text(" "), tokenText);
    }
}
