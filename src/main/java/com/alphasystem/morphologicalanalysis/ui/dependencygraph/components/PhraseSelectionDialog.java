package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.PhraseNode;
import com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.Global;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.AlternateStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.controlsfx.control.ListSelectionView;

import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.LABEL_ONLY;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_MEDIUM_BOLD;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.FI_MAHL;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;
import static javafx.scene.control.DialogEvent.DIALOG_CLOSE_REQUEST;

/**
 * @author sali
 */
public class PhraseSelectionDialog extends Dialog<PhraseNode> {

    private final Label phraseLabel;
    private final ComboBox<AlternateStatus> alternateStatusComboBox;
    private final ListSelectionView<RelationshipType> relationshipsListView;

    public PhraseSelectionDialog() {
        setTitle(getLabel("title"));
        ComboBoxFactory comboBoxFactory = ComboBoxFactory.getInstance();
        alternateStatusComboBox = comboBoxFactory.getAlternateStatusComboBox();
        relationshipsListView = new ListSelectionView<>();
        relationshipsListView.setCellFactory(new ArabicSupportEnumCellFactory<>(LABEL_ONLY));
        relationshipsListView.setMinHeight(5);
        relationshipsListView.getSourceItems().addAll(RelationshipType.values());
        phraseLabel = new Label();
        phraseLabel.setFont(ARABIC_FONT_MEDIUM_BOLD);
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
        ObservableList<RelationshipType> targetItems = relationshipsListView.getTargetItems();
        targetItems.forEach(relationshipType -> {
            phraseNode.getRelationships().add(relationshipType);
        });
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
        gridPane.add(phraseLabel, 0, 1);

        label = new Label(getLabel("relationshipTypeComboBox"));
        gridPane.add(label, 0, 2);
        gridPane.add(relationshipsListView, 0, 3);

        gridPane.add(getAlternateNounStatusLabel(), 0, 4);
        gridPane.add(alternateStatusComboBox, 0, 5);

        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(OK);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        getDialogPane().setContent(borderPane);
        setOnCloseRequest(event -> {
            EventType<DialogEvent> eventType = event.getEventType();
            if (event.equals(DIALOG_CLOSE_REQUEST)) {

            }
        });
    }

    private Label getAlternateNounStatusLabel() {
        Text text = new Text(FI_MAHL.toUnicode());
        text.setFont(Global.ARABIC_FONT_SMALL);
        Label label = new Label("", text);
        return label;
    }

    private void reset() {
        relationshipsListView.getSourceItems().remove(0, relationshipsListView.getSourceItems().size());
        relationshipsListView.getTargetItems().remove(0, relationshipsListView.getTargetItems().size());
        relationshipsListView.getSourceItems().addAll(RelationshipType.values());
        alternateStatusComboBox.getSelectionModel().select(0);
    }

    public void reset(String labelText) {
        reset();
        phraseLabel.setText(labelText);
    }
}
