package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ProNounPartOfSpeechType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ProNounType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author sali
 */
@Component
public class ProNounPropertiesController extends BorderPane {

    @Autowired private ProNounPropertiesView control;
    @FXML private ComboBox<ProNounPartOfSpeechType> partOfSpeechComboBox;
    @FXML private ComboBox<ProNounType> proNounTypeComboBox;
    @FXML private ComboBox<NounStatus> nounStatusComboBox;
    @FXML private ComboBox<NumberType> numberTypeComboBox;
    @FXML private ComboBox<GenderType> genderTypeComboBox;
    @FXML private ComboBox<ConversationType> conversationTypeComboBox;

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        partOfSpeechComboBox.valueProperty().bindBidirectional(control.partOfSpeechTypeProperty());
        proNounTypeComboBox.valueProperty().bindBidirectional(control.proNounTypeProperty());
        nounStatusComboBox.valueProperty().bindBidirectional(control.nounStatusProperty());
        numberTypeComboBox.valueProperty().bindBidirectional(control.numberTypeProperty());
        genderTypeComboBox.valueProperty().bindBidirectional(control.genderTypeProperty());
        conversationTypeComboBox.valueProperty().bindBidirectional(control.conversationTypeProperty());
    }
}
