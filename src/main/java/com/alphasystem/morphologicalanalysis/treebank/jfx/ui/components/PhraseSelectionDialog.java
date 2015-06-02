package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.model.support.GrammaticalRelationship;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.PhraseSelectionModel;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumAdapter;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global.*;
import static javafx.scene.control.ButtonType.*;

/**
 * @author sali
 */
public class PhraseSelectionDialog extends Dialog<PhraseSelectionModel> {

    private final ComboBox<ArabicSupportEnumAdapter<GrammaticalRelationship>> comboBox;
    private PhraseSelectionModel phraseSelectionModel = new PhraseSelectionModel();
    private ObjectProperty<TerminalNode> terminalNode = new SimpleObjectProperty<>();

    public PhraseSelectionDialog() {
        setTitle(getLabel("title"));
        setHeaderText(getLabel("headerText"));

        comboBox = ComboBoxFactory.getInstance().getGrammaticalRelationshipComboBox();
        comboBox.disableProperty().bind(phraseSelectionModel.uninitialized());
        initDialogPane();
        setResultConverter(param -> {
            ButtonBar.ButtonData buttonData = param.getButtonData();
            PhraseSelectionModel result = phraseSelectionModel;
            if (buttonData.isCancelButton()) {
                reset();
                result = null;
            } else if (buttonData.name().equals(OK.getButtonData().name())) {
                result = null;
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
            phraseSelectionModel.setRelationship(newValue.getValue());
        });
        phraseSelectionModel.relationshipProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.equals(GrammaticalRelationship.NONE)) {
                comboBox.getSelectionModel().select(0);
            }
        });

        getDialogPane().getButtonTypes().addAll(OK, FINISH, CANCEL);

        Button cancelButton = (Button) getDialogPane().lookupButton(CANCEL);
        cancelButton.disableProperty().bind(phraseSelectionModel.uninitialized());
        Button finishButton = (Button) getDialogPane().lookupButton(FINISH);
        finishButton.disableProperty().bind(firstNodeLabel.textProperty().isNotEqualTo(NONE_SELECTED)
                .and(comboBox.getSelectionModel().selectedIndexProperty().isEqualTo(0)));

        getDialogPane().setContent(gridPane);
        getDialogPane().setPrefWidth(400);
    }

    private Font getFont(String text) {
        return ((text == null) || NONE_SELECTED.equals(text)) ? ENGLISH_FONT : ARABIC_FONT_MEDIUM;
    }

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
