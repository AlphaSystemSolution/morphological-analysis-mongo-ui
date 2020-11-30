package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper.PREFERENCES;

/**
 * @author sali
 */
@Component
public class CommonPropertiesController extends BorderPane {

    @Autowired private CommonPropertiesView control;
    @FXML private TextField textField;
    @FXML private TextField derivedTextField;
    @FXML private TextArea translationField;
    @FXML private ComboBox<WordType> wordTypeComboBox;
    @FXML private ComboBox<NamedTag> namedTagComboBox;

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        textField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        textField.setFont(PREFERENCES.getArabicFont24());
        textField.textProperty().bindBidirectional(control.textProperty());
        derivedTextField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        derivedTextField.setFont(PREFERENCES.getArabicFont24());
        derivedTextField.textProperty().bind(control.derivedTextProperty());
        translationField.textProperty().bindBidirectional(control.translationProperty());
        wordTypeComboBox.valueProperty().bindBidirectional(control.wordTypeProperty());
        namedTagComboBox.valueProperty().bindBidirectional(control.namedTagProperty());
    }
}
