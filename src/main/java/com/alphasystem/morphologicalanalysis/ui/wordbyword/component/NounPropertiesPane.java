package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.model.TokenAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class NounPropertiesPane extends AbstractPropertiesPane {

    private ComboBox<NounStatus> nounStatusComboBox;
    private ComboBox<NumberType> numberTypeComboBox;
    private ComboBox<GenderType> genderTypeComboBox;
    private ComboBox<NounType> nounTypeComboBox;
    private ComboBox<NounKind> nounKindComboBox;

    public NounPropertiesPane(final TokenAdapter tokenAdapter) {
        super(tokenAdapter);
    }

    @Override
    protected void initComboBoxes() {
        nounStatusComboBox = instance.getNounStatusComboBox();
        numberTypeComboBox = instance.getNumberTypeComboBox();
        genderTypeComboBox = instance.getGenderTypeComboBox();
        nounTypeComboBox = instance.getNounTypeComboBox();
        nounKindComboBox = instance.getNounKindComboBox();
    }

    @Override
    protected void initListeners() {
        nounStatusComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    System.out.println("HERE: " + newValue);
                    tokenAdapter.setNounStatus(newValue);
                });
        numberTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNumberType(newValue);
                });
        genderTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setGenderType(newValue);
                });
        nounTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNounType(newValue);
                });
        nounKindComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNounKind(newValue);
                });
    }

    @Override
    protected void initPane() {
        Label label = new Label(RESOURCE_BUNDLE.getString("nounStatus.label"));
        add(label, 0, 0);
        label.setLabelFor(nounStatusComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("numberType.label"));
        add(label, 1, 0);
        label.setLabelFor(numberTypeComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("genderType.label"));
        add(label, 2, 0);
        label.setLabelFor(genderTypeComboBox);

        add(nounStatusComboBox, 0, 1);
        add(numberTypeComboBox, 1, 1);
        add(genderTypeComboBox, 2, 1);

        label = new Label(RESOURCE_BUNDLE.getString("nounType.label"));
        add(label, 0, 2);
        label.setLabelFor(nounTypeComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("nounKind.label"));
        add(label, 1, 2);
        label.setLabelFor(nounKindComboBox);

        add(new Pane(), 2, 2);

        add(nounTypeComboBox, 0, 3);
        add(nounKindComboBox, 1, 3);
        add(new Pane(), 2, 3);
    }

    @Override
    public void updateComboBoxes() {
        Location location = tokenAdapter.getLocation();
        if (location != null) {
            NounProperties properties = (NounProperties) location.getProperties();
            numberTypeComboBox.getSelectionModel().select(properties.getNumber());
            genderTypeComboBox.getSelectionModel().select(properties.getGender());
            nounStatusComboBox.getSelectionModel().select(properties.getStatus());
            nounKindComboBox.getSelectionModel().select(properties.getNounKind());
            nounTypeComboBox.getSelectionModel().select(properties.getNounType());
        }
    }
}
