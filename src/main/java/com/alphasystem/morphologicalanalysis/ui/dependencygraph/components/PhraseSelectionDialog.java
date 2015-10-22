package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.PhraseNode;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.Global;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.AlternateStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_SMALL_BOLD;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.FI_MAHL;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class PhraseSelectionDialog extends Dialog<PhraseNode> {

    private final Label phraseLabel;
    private final ComboBox<RelationshipType> relationshipTypeComboBox;
    private final ComboBox<AlternateStatus> alternateStatusComboBox;

    public PhraseSelectionDialog() {
        setTitle(getLabel("title"));
        ComboBoxFactory comboBoxFactory = ComboBoxFactory.getInstance();
        relationshipTypeComboBox = comboBoxFactory.getRelationshipTypeComboBox();
        alternateStatusComboBox = comboBoxFactory.getAlternateStatusComboBox();
        phraseLabel = new Label();
        phraseLabel.setFont(ARABIC_FONT_SMALL_BOLD);
        reset();
        initDialog();
        setResultConverter(param -> createResult(param));
    }

    private static String getLabel(String label) {
        return Global.getLabel(PhraseSelectionDialog.class, label);
    }

    private PhraseNode createResult(ButtonType param) {
        ButtonBar.ButtonData buttonData = param.getButtonData();
        if (buttonData.isCancelButton()) {
            return null;
        }
        PhraseNode phraseNode = new PhraseNode();
        phraseNode.setRelationshipType(relationshipTypeComboBox.getValue());
        phraseNode.setAlternateStatus(alternateStatusComboBox.getValue());
        phraseNode.initDisplayName();
        return phraseNode;
    }

    private void initDialog() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label(getLabel("phraseText"));
        gridPane.add(label, 0, 0);
        gridPane.add(phraseLabel, 1, 0);

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
        text.setFont(Global.ARABIC_FONT_SMALL);
        Label label = new Label("", text);
        return label;
    }

    private void reset() {
        relationshipTypeComboBox.getSelectionModel().select(0);
        alternateStatusComboBox.getSelectionModel().select(0);
    }

    public void reset(String labelText) {
        reset();
        phraseLabel.setText(labelText);
    }
}
