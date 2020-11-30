package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alphasystem.morphologicalanalysis.ui.control.ParticlesPicker;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ParticlePartOfSpeechType;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**
 * @author sali
 */
@Component
public class ParticlePropertiesController extends BorderPane {

    @Autowired private ParticlePropertiesView control;
    @FXML private ParticlesPicker particlesPicker;

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        // update particle picket when location changes
        control.locationProperty().addListener((observable, oldValue, newValue) -> updatePicker(newValue));
    }

    private void updatePicker(Location location) {
        particlesPicker.getValues().clear();
        if (location == null) {
            return;
        }
        final List<AbstractProperties> properties = location.getProperties();
        properties.forEach(abstractProperties -> {
            final ParticlePartOfSpeechType partOfSpeech = ((ParticleProperties) abstractProperties).getPartOfSpeech();
            particlesPicker.getValues().add(partOfSpeech);
        });
    }


}
