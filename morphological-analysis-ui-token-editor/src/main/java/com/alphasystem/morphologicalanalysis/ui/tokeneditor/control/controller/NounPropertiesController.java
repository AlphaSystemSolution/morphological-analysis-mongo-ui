package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.NounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounKind;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounPartOfSpeechType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType;
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
public class NounPropertiesController extends BorderPane {

    @Autowired private NounPropertiesView control;
    @FXML private ComboBox<NounPartOfSpeechType> partOfSpeechComboBox;
    @FXML private ComboBox<NounStatus> nounStatusComboBox;
    @FXML private ComboBox<NumberType> numberTypeComboBox;
    @FXML private ComboBox<GenderType> genderTypeComboBox;
    @FXML private ComboBox<NounType> nounTypeComboBox;
    @FXML private ComboBox<NounKind> nounKindComboBox;

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        partOfSpeechComboBox.valueProperty().bindBidirectional(control.partOfSpeechTypeProperty());
        nounStatusComboBox.valueProperty().bindBidirectional(control.nounStatusProperty());
        numberTypeComboBox.valueProperty().bindBidirectional(control.numberTypeProperty());
        genderTypeComboBox.valueProperty().bindBidirectional(control.genderTypeProperty());
        nounTypeComboBox.valueProperty().bindBidirectional(control.nounTypeProperty());
        nounKindComboBox.valueProperty().bindBidirectional(control.nounKindProperty());
    }
}
