package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.model.TokenAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class VerbPropertiesPane extends AbstractPropertiesPane {

    private ComboBox<NumberType> numberTypeComboBox;
    private ComboBox<GenderType> genderTypeComboBox;
    private ComboBox<ConversationType> conversationTypeComboBox;
    private ComboBox<VerbType> verbTypeComboBox;
    private ComboBox<VerbMode> verbModeComboBox;

    public VerbPropertiesPane(final TokenAdapter tokenAdapter) {
        super(tokenAdapter);
    }

    @Override
    protected void initComboBoxes() {
        numberTypeComboBox = instance.getNumberTypeComboBox();
        genderTypeComboBox = instance.getGenderTypeComboBox();
        conversationTypeComboBox = instance.getConversationTypeComboBox();
        verbTypeComboBox = instance.getVerbTypeComboBox();
        verbModeComboBox = instance.getVerbModeComboBox();
    }

    @Override
    protected void initPane() {
        Label label = new Label(RESOURCE_BUNDLE.getString("verbType.label"));
        add(label, 0, 0);
        label.setLabelFor(verbTypeComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("conversationType.label"));
        add(label, 1, 0);
        label.setLabelFor(conversationTypeComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("verbMode.label"));
        add(label, 2, 0);
        label.setLabelFor(verbModeComboBox);

        add(verbTypeComboBox, 0, 1);
        add(conversationTypeComboBox, 1, 1);
        add(verbModeComboBox, 2, 1);

        label = new Label(RESOURCE_BUNDLE.getString("numberType.label"));
        add(label, 0, 2);
        label.setLabelFor(numberTypeComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("genderType.label"));
        add(label, 1, 2);
        label.setLabelFor(genderTypeComboBox);

        add(new Pane(), 2, 2);

        add(numberTypeComboBox, 0, 3);
        add(genderTypeComboBox, 1, 3);
        add(new Pane(), 2, 3);

    }

    @Override
    protected void initListeners() {
        numberTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNumberType(newValue);
                });
        genderTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setGenderType(newValue);
                });
        conversationTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setConversationType(newValue);
                });
        verbTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setVerbType(newValue);
                });
        verbModeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setVerbMode(newValue);
                });
    }

    @Override
    public void updateComboBoxes() {
        Location location = tokenAdapter.getLocation();
        if (location != null) {
            VerbProperties properties = (VerbProperties) location.getProperties();
            numberTypeComboBox.getSelectionModel().select(properties.getNumber());
            genderTypeComboBox.getSelectionModel().select(properties.getGender());
            verbModeComboBox.getSelectionModel().select(properties.getMode());
            verbTypeComboBox.getSelectionModel().select(properties.getVerbType());
            conversationTypeComboBox.getSelectionModel().select(properties.getConversationType());
        }
    }
}
