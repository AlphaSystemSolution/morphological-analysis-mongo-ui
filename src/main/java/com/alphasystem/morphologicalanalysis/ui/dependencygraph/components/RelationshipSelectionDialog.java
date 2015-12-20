package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.Global;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.AlternateStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.ui.util.FontConstants.ARABIC_FONT_20;
import static com.alphasystem.arabic.ui.util.FontConstants.ARABIC_FONT_24;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.FI_MAHL;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class RelationshipSelectionDialog extends Dialog<RelationshipNode> {

    private final Label dependentLabel;
    private final Label ownerLabel;
    private final ComboBox<RelationshipType> relationshipTypeComboBox;
    private final ComboBox<AlternateStatus> alternateStatusComboBox;

    public RelationshipSelectionDialog() {
        setTitle(getLabel("title"));
        ComboBoxFactory comboBoxFactory = ComboBoxFactory.getInstance();
        relationshipTypeComboBox = comboBoxFactory.getRelationshipTypeComboBox();
        alternateStatusComboBox = comboBoxFactory.getAlternateStatusComboBox();
        dependentLabel = new Label();
        dependentLabel.setFont(ARABIC_FONT_20);
        ownerLabel = new Label();
        ownerLabel.setFont(ARABIC_FONT_20);

        reset();
        initDialogPane();

        setResultConverter(this::createResult);
    }

    private static String getLabel(String label) {
        return Global.getLabel(RelationshipSelectionDialog.class, label);
    }

    private RelationshipNode createResult(ButtonType param) {
        ButtonBar.ButtonData buttonData = param.getButtonData();
        if (buttonData.isCancelButton()) {
            return null;
        }
        RelationshipNode relationship = new RelationshipNode();
        relationship.setRelationshipType(relationshipTypeComboBox.getSelectionModel().getSelectedItem());
        relationship.setAlternateStatus(alternateStatusComboBox.getSelectionModel().getSelectedItem());
        return relationship;
    }

    private void initDialogPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label(getLabel("dependent"));
        gridPane.add(label, 0, 0);
        gridPane.add(dependentLabel, 1, 0);

        label = new Label(getLabel("owner"));
        gridPane.add(label, 0, 1);
        gridPane.add(ownerLabel, 1, 1);

        label = new Label(getLabel("relationshipTypeComboBox"));
        gridPane.add(label, 0, 2);
        gridPane.add(relationshipTypeComboBox, 1, 2);

        gridPane.add(getAlternateNounStatusLabel(), 0, 3);
        gridPane.add(alternateStatusComboBox, 1, 3);

        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(OK);
        okButton.disableProperty().bind(relationshipTypeComboBox.getSelectionModel()
                .selectedIndexProperty().isEqualTo(0));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        getDialogPane().setContent(borderPane);
    }

    private Label getAlternateNounStatusLabel() {
        Text text = new Text(FI_MAHL.toUnicode());
        text.setFont(ARABIC_FONT_24);
        return new Label("", text);
    }

    private void reset() {
        relationshipTypeComboBox.getSelectionModel().select(0);
        alternateStatusComboBox.getSelectionModel().select(0);
    }

    public void reset(String dependentNodeLabel, String ownerNodeLabel){
        reset();
        dependentLabel.setText(dependentNodeLabel);
        ownerLabel.setText(ownerNodeLabel);
    }
}
