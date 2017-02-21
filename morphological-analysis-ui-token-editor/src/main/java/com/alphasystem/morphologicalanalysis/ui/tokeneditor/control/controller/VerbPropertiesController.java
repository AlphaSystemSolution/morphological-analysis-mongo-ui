package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbCategory;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbMode;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbPartOfSpeechType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbCategory.DUMMY;

/**
 * @author sali
 */
@Component
public class VerbPropertiesController extends BorderPane {

    @Autowired private VerbPropertiesView control;
    @FXML private ComboBox<VerbPartOfSpeechType> partOfSpeechComboBox;
    @FXML private ComboBox<VerbType> verbTypeComboBox;
    @FXML private ComboBox<ConversationType> conversationTypeComboBox;
    @FXML private ComboBox<NumberType> numberTypeComboBox;
    @FXML private ComboBox<GenderType> genderTypeComboBox;
    @FXML private ComboBox<VerbMode> verbModeComboBox;
    @FXML private ComboBox<IncompleteVerbCategory> incompleteVerbCategoryComboBox;
    @FXML private ComboBox<IncompleteVerbType> incompleteVerbTypeComboBox;

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        partOfSpeechComboBox.valueProperty().bindBidirectional(control.partOfSpeechTypeProperty());
        verbTypeComboBox.valueProperty().bindBidirectional(control.verbTypeProperty());
        conversationTypeComboBox.valueProperty().bindBidirectional(control.conversationTypeProperty());
        numberTypeComboBox.valueProperty().bindBidirectional(control.numberTypeProperty());
        genderTypeComboBox.valueProperty().bindBidirectional(control.genderTypeProperty());
        verbModeComboBox.valueProperty().bindBidirectional(control.verbModeProperty());
        incompleteVerbCategoryComboBox.valueProperty().bindBidirectional(control.incompleteVerbCategoryProperty());
        incompleteVerbCategoryComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                setIncompleteVerbCategoryComboBoxValue(newValue, null));
    }

    private void setIncompleteVerbCategoryComboBoxValue(final IncompleteVerbCategory incompleteVerbCategory,
                                                        final IncompleteVerbType incompleteVerbType) {
        IncompleteVerbCategory newValue = ((incompleteVerbCategory == null) || DUMMY.equals(incompleteVerbCategory)) ?
                null : incompleteVerbCategory;
        boolean disable = newValue == null;
        incompleteVerbTypeComboBox.getItems().clear();
        incompleteVerbTypeComboBox.setDisable(disable);
        if (!disable) {
            incompleteVerbTypeComboBox.getItems().addAll(incompleteVerbCategory.getMembers());
            if (incompleteVerbType == null) {
                incompleteVerbTypeComboBox.getSelectionModel().selectFirst();
            } else {
                incompleteVerbTypeComboBox.setValue(incompleteVerbType);
            }
        }
    }
}

