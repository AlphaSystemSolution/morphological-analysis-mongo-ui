package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.AbstractPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.NounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class LocationPropertiesController extends AnchorPane {

    private static final double DEFAULT_OFFSET = 10.0;

    @Autowired private LocationPropertiesView control;
    @Autowired private CommonPropertiesView commonPropertiesView;
    @Autowired private NounPropertiesView nounPropertiesView;
    @Autowired private ProNounPropertiesView proNounPropertiesView;
    @Autowired private VerbPropertiesView verbPropertiesView;
    @Autowired private ParticlePropertiesView particlePropertiesView;

    @PostConstruct
    void postConstruct() {
        refresh(control.getLocation());
        control.locationProperty().addListener((observable, oldValue, newValue) -> refresh(newValue));
    }

    private void refresh(Location location) {
        getChildren().clear();
        commonPropertiesView.setLocation(location);
        final AbstractPropertiesView propertiesView = getPropertiesView(location);
        setLeftAnchor(commonPropertiesView, DEFAULT_OFFSET);
        setTopAnchor(commonPropertiesView, DEFAULT_OFFSET);
        setLeftAnchor(propertiesView, 470 + DEFAULT_OFFSET);
        setTopAnchor(propertiesView, DEFAULT_OFFSET);
        getChildren().addAll(commonPropertiesView, propertiesView);
    }

    @SuppressWarnings({"unchecked"})
    private AbstractPropertiesView getPropertiesView(Location location) {
        if (location == null) {
            return nounPropertiesView;
        }
        AbstractPropertiesView node = null;
        final WordType wordType = location.getWordType();
        switch (wordType) {
            case NOUN:
                node = nounPropertiesView;
                break;
            case PRO_NOUN:
                node = proNounPropertiesView;
                break;
            case VERB:
                node = verbPropertiesView;
                break;
            case PARTICLE:
                node = particlePropertiesView;
                break;
        }
        if (!WordType.PARTICLE.equals(wordType)) {
            node.setLocationProperties(location.getProperties().get(0));
        }
        return node;
    }
}
