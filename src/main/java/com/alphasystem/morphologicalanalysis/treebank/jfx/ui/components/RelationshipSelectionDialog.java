package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumAdapter;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.Global;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class RelationshipSelectionDialog extends Dialog<RelationshipType> {

    private final StringProperty firstPartOfSpeech = new SimpleStringProperty();
    private final StringProperty secondPartOfSpeech = new SimpleStringProperty();
    private final ComboBox<ArabicSupportEnumAdapter<RelationshipType>> comboBox;

    public RelationshipSelectionDialog() {
        setTitle(getLabel("title"));
        setHeaderText(getLabel("headerText"));
        comboBox = ComboBoxFactory.getInstance().getGrammaticalRelationshipComboBox();
        comboBox.disableProperty().bind(secondPartOfSpeech.isEqualTo(NONE_SELECTED));
        reset();
        initDialogPane();
    }

    private static String getLabel(String label) {
        return Global.getLabel(RelationshipSelectionDialog.class, label);
    }

    private void initDialogPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label(getLabel("firstPartOfSpeech"));
        gridPane.add(label, 0, 0);
        Label firstPartOfSpeechLabel = new Label(getFirstPartOfSpeech());
        firstPartOfSpeechLabel.textProperty().bindBidirectional(firstPartOfSpeech);
        gridPane.add(firstPartOfSpeechLabel, 1, 0);

        label = new Label(getLabel("secondPartOfSpeech"));
        gridPane.add(label, 0, 1);
        Label secondPartOfSpeechLabel = new Label(getSecondPartOfSpeech());
        secondPartOfSpeechLabel.textProperty().bindBidirectional(secondPartOfSpeech);
        gridPane.add(secondPartOfSpeechLabel, 1, 1);

        label = new Label(getLabel("comboBox"));
        gridPane.add(label, 0, 2);
        gridPane.add(comboBox, 1, 2);

        firstPartOfSpeech.addListener((observable, oldValue, newValue) -> {
            Font font = NONE_SELECTED.equals(newValue) ? ENGLISH_FONT : ARABIC_FONT_SMALL;
            firstPartOfSpeechLabel.setFont(font);
        });

        secondPartOfSpeech.addListener((observable, oldValue, newValue) -> {
            Font font = NONE_SELECTED.equals(newValue) ? ENGLISH_FONT : ARABIC_FONT_SMALL;
            secondPartOfSpeechLabel.setFont(font);
        });

        setResultConverter(param -> {
            ButtonBar.ButtonData buttonData = param.getButtonData();
            return buttonData.isCancelButton() ? RelationshipType.NONE :
                    comboBox.getSelectionModel().getSelectedItem().getValue();
        });
        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(OK);
        okButton.disableProperty().bind(secondPartOfSpeech.isNotEqualTo(NONE_SELECTED)
                .and(comboBox.getSelectionModel().selectedIndexProperty().isEqualTo(0)));
        getDialogPane().setContent(gridPane);
        getDialogPane().setPrefWidth(400);
    }


    public final String getFirstPartOfSpeech() {
        return firstPartOfSpeech.get();
    }

    public final void setFirstPartOfSpeech(String firstPartOfSpeech) {
        this.firstPartOfSpeech.set(firstPartOfSpeech);
    }

    public final StringProperty firstPartOfSpeechProperty() {
        return firstPartOfSpeech;
    }

    public final String getSecondPartOfSpeech() {
        return secondPartOfSpeech.get();
    }

    public final void setSecondPartOfSpeech(String secondPartOfSpeech) {
        this.secondPartOfSpeech.set(secondPartOfSpeech);
    }

    public final StringProperty secondPartOfSpeechProperty() {
        return secondPartOfSpeech;
    }

    public void reset() {
        setFirstPartOfSpeech(NONE_SELECTED);
        setSecondPartOfSpeech(NONE_SELECTED);
        comboBox.getSelectionModel().select(0);
    }

}
