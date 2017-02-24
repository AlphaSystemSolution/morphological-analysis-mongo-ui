package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.arabic.ui.RootLettersPicker;
import com.alphasystem.arabic.ui.VerbalNounsPicker;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.MorphologicalEntryView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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
public class MorphologicalEntryController extends BorderPane {

    @Autowired private MorphologicalEntryView control;
    @FXML private RootLettersPicker rootLettersPicker;
    @FXML private ComboBox<NamedTemplate> namedTemplateComboBox;
    @FXML private VerbalNounsPicker verbalNounsPicker;
    @FXML private TextArea translationField;
    @FXML private CheckBox removePassiveLine;
    @FXML private CheckBox skipRuleProcessing;

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        rootLettersPicker.rootLettersProperty().bindBidirectional(control.rootLettersProperty());
        namedTemplateComboBox.valueProperty().bindBidirectional(control.formProperty());
        translationField.textProperty().bindBidirectional(control.shortTranslationProperty());
        removePassiveLine.selectedProperty().bindBidirectional(control.removePassiveLineProperty());
        skipRuleProcessing.selectedProperty().bindBidirectional(control.skipRuleProcessingProperty());
        verbalNounsPicker.getValues().addAll(control.getVerbalNouns());
        verbalNounsPicker.getValues().addListener((ListChangeListener<? super VerbalNoun>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    control.getVerbalNouns().removeAll(c.getRemoved());
                }
                if (c.wasAdded()) {
                    control.getVerbalNouns().addAll(c.getAddedSubList());
                }
            }
        });
    }

    public void updateVerbalNouns() {
        NamedTemplate template = control.getForm();
        verbalNounsPicker.getValues().clear();
        ObservableSet<VerbalNoun> verbalNounsFromControl = control.getVerbalNouns();
        if (verbalNounsFromControl != null && !verbalNounsFromControl.isEmpty()) {
            verbalNounsPicker.getValues().addAll(verbalNounsFromControl);
        } else {
            verbalNounsPicker.getValues().addAll(VerbalNoun.getByTemplate(template));
        }
    }
}
