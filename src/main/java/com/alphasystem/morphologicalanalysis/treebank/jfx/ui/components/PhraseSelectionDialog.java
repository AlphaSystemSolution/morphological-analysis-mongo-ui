package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.PhraseSelectionModel;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.Global;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static javafx.scene.control.ButtonType.*;

/**
 * @author sali
 */
public class PhraseSelectionDialog extends Dialog<PhraseSelectionModel> {

    private final ComboBox<RelationshipType> comboBox;
    private PhraseSelectionModel phraseSelectionModel = new PhraseSelectionModel();
    private ObjectProperty<TerminalNode> terminalNode = new SimpleObjectProperty<>();

    public PhraseSelectionDialog() {
        setTitle(getLabel("title"));
        setHeaderText(getLabel("headerText"));

        comboBox = ComboBoxFactory.getInstance().getRelationshipTypeComboBox();
        comboBox.disableProperty().bind(phraseSelectionModel.uninitialized());
        initDialogPane();
        setResultConverter(param -> {
            ButtonBar.ButtonData buttonData = param.getButtonData();
            PhraseSelectionModel result = phraseSelectionModel;
            if (buttonData.isCancelButton()) {
                TerminalNode firstNode = phraseSelectionModel.getFirstNode();
                TerminalNode lastNode = phraseSelectionModel.getLastNode();
                if (firstNode != null && lastNode != null) {
                    reset();
                    result = null;
                } else if (lastNode != null) {
                    phraseSelectionModel.setLastNode(null);
                } else if (firstNode != null) {
                    phraseSelectionModel.setFirstNode(null);
                }
            }
            return result;
        });
    }

    private static String getLabel(String label) {
        return Global.getLabel(PhraseSelectionDialog.class, label);
    }

    private void initDialogPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label(getLabel("firstNode"));
        gridPane.add(label, 0, 0);

        Label firstNodeLabel = new Label(phraseSelectionModel.getFirstNodeLabel());
        firstNodeLabel.setFont(getFont(null));
        firstNodeLabel.textProperty().bind(phraseSelectionModel.firstNodeLabelProperty());
        firstNodeLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            firstNodeLabel.setFont(getFont(newValue));
        });
        gridPane.add(firstNodeLabel, 1, 0);

        label = new Label(getLabel("lastNode"));
        gridPane.add(label, 0, 1);

        Label lastNodeLabel = new Label(phraseSelectionModel.getLastNodeLabel());
        lastNodeLabel.setFont(getFont(null));
        lastNodeLabel.textProperty().bind(phraseSelectionModel.lastNodeLabelProperty());
        lastNodeLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            lastNodeLabel.setFont(getFont(newValue));
        });
        gridPane.add(lastNodeLabel, 1, 1);

        label = new Label(getLabel("comboBox"));
        gridPane.add(label, 0, 2);
        gridPane.add(comboBox, 1, 2);
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            phraseSelectionModel.setRelationship(newValue);
        });
        phraseSelectionModel.relationshipProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals(RelationshipType.NONE)) {
                comboBox.getSelectionModel().select(0);
            }
        });

        getDialogPane().getButtonTypes().addAll(OK, FINISH, CANCEL);

        Button finishButton = (Button) getDialogPane().lookupButton(FINISH);
        finishButton.disableProperty().bind(comboBox.getSelectionModel().selectedIndexProperty().isNotEqualTo(0));

        getDialogPane().setContent(gridPane);
        getDialogPane().setPrefWidth(400);
    }

    private Font getFont(String text) {
        return ((text == null) || NONE_SELECTED.equals(text)) ? ENGLISH_FONT : ARABIC_FONT_MEDIUM;
    }

    @SuppressWarnings({"unused"})
    public ObjectProperty<TerminalNode> terminalNodeProperty() {
        return terminalNode;
    }

    public PhraseSelectionModel getPhraseSelectionModel() {
        return phraseSelectionModel;
    }

    public void reset() {
        phraseSelectionModel.reset();
    }
}
