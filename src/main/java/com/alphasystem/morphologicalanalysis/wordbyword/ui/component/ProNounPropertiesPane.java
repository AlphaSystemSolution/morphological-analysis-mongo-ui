package com.alphasystem.morphologicalanalysis.wordbyword.ui.component;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ProNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import com.alphasystem.morphologicalanalysis.wordbyword.ui.model.TokenAdapter;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class ProNounPropertiesPane extends AbstractPropertiesPane {

    private ComboBox<ProNounType> proNounTypeComboBox;
    private ComboBox<ConversationType> conversationTypeComboBox;
    private ComboBox<NounStatus> nounStatusComboBox;
    private ComboBox<NumberType> numberTypeComboBox;
    private ComboBox<GenderType> genderTypeComboBox;

    public ProNounPropertiesPane(final TokenAdapter tokenAdapter) {
        super(tokenAdapter);
    }

    @Override
    protected void initComboBoxes() {
        proNounTypeComboBox = instance.getProNounTypeComboBox();
        conversationTypeComboBox = instance.getConversationTypeComboBox();
        nounStatusComboBox = instance.getNounStatusComboBox();
        numberTypeComboBox = instance.getNumberTypeComboBox();
        genderTypeComboBox = instance.getGenderTypeComboBox();
    }

    @Override
    protected void initListeners() {
        nounStatusComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
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
        conversationTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setConversationType(newValue);
                });
        proNounTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setProNounType(newValue);
                });
    }

    @Override
    protected void initPane() {
        Label label = new Label(RESOURCE_BUNDLE.getString("proNounType.label"));
        add(label, 0, 0);
        label.setLabelFor(proNounTypeComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("numberType.label"));
        add(label, 1, 0);
        label.setLabelFor(numberTypeComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("genderType.label"));
        add(label, 2, 0);
        label.setLabelFor(genderTypeComboBox);

        add(proNounTypeComboBox, 0, 1);
        add(numberTypeComboBox, 1, 1);
        add(genderTypeComboBox, 2, 1);

        label = new Label(RESOURCE_BUNDLE.getString("conversationType.label"));
        add(label, 0, 2);
        label.setLabelFor(conversationTypeComboBox);

        label = new Label(RESOURCE_BUNDLE.getString("nounStatus.label"));
        add(label, 1, 2);
        label.setLabelFor(nounStatusComboBox);

        add(new Pane(), 2, 2);

        add(conversationTypeComboBox, 0, 3);
        add(nounStatusComboBox, 1, 3);
        add(new Pane(), 2, 3);

    }

    @Override
    public void updateComboBoxes() {
        Location location = tokenAdapter.getLocation();
        if (location != null) {
            ProNounProperties properties = (ProNounProperties) location.getProperties();
            numberTypeComboBox.getSelectionModel().select(properties.getNumber());
            genderTypeComboBox.getSelectionModel().select(properties.getGender());
            nounStatusComboBox.getSelectionModel().select(properties.getStatus());
            proNounTypeComboBox.getSelectionModel().select(properties.getProNounType());
            conversationTypeComboBox.getSelectionModel().select(properties.getConversationType());
        }
    }
}
