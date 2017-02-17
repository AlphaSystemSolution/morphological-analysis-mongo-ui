package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.spring.support.ApplicationContextProvider;
import com.alphasystem.morphologicalanalysis.ui.util.MorphologicalAnalysisPreferences;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.GenericPreferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.net.URL;
import java.util.ResourceBundle;

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
    void postConstruct() {
        try {
            final String resourcePrefix = control.getClass().getSimpleName();
            final URL url = AppUtil.getPath(String.format("fxml/%s.fxml", resourcePrefix)).toUri().toURL();
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(String.format("fxml.%s", resourcePrefix));
            FXMLLoader fxmlLoader = new FXMLLoader(url, resourceBundle);
            fxmlLoader.setControllerFactory(ApplicationContextProvider::getBean);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @FXML
    void initialize() {
        final MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
        textField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        textField.setFont(preferences.getArabicFont24());
        textField.textProperty().bindBidirectional(control.textProperty());
        derivedTextField.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        derivedTextField.setFont(preferences.getArabicFont24());
        derivedTextField.textProperty().bind(control.derivedTextProperty());
        translationField.textProperty().bindBidirectional(control.translationProperty());
        wordTypeComboBox.valueProperty().bindBidirectional(control.wordTypeProperty());
        namedTagComboBox.valueProperty().bindBidirectional(control.namedTagProperty());
    }
}
