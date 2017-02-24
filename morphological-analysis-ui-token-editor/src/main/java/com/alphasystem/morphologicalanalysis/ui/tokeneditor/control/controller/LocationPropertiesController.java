package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.AbstractPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.MorphologicalEntryView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.NounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
    @Autowired private MorphologicalEntryView morphologicalEntryView;

    @PostConstruct
    void postConstruct() {
        refresh(control.getLocation());
        control.locationProperty().addListener((observable, oldValue, newValue) -> refresh(newValue));
        commonPropertiesView.wordTypeProperty().addListener((observable, oldValue, newValue) -> refreshPropertiesView(newValue));
    }

    @SuppressWarnings({"unchecked"})
    private void refresh(Location location) {
        getChildren().clear();
        commonPropertiesView.setLocation(location);
        getChildren().addAll(commonPropertiesView, refreshPropertiesView(location), morphologicalEntryView);
    }

    @SuppressWarnings({"unchecked"})
    private AbstractPropertiesView refreshPropertiesView(Location location) {
        final WordType wordType = (location == null) ? WordType.NOUN : location.getWordType();
        final AbstractPropertiesView propertiesView = getPropertiesView(wordType);
        setLeftAnchor(commonPropertiesView, DEFAULT_OFFSET);
        setTopAnchor(commonPropertiesView, DEFAULT_OFFSET);

        double offset = 470 + DEFAULT_OFFSET;
        setLeftAnchor(propertiesView, offset);
        setTopAnchor(propertiesView, DEFAULT_OFFSET);
        propertiesView.setLocation(null);
        propertiesView.setLocation(location);

        switch (wordType) {
            case NOUN:
                offset += 340;
                break;
            case PRO_NOUN:
                offset += 390;
                break;
            case VERB:
                offset += 480;
                break;
            case PARTICLE:
                offset += 330;
                break;
        }
        setLeftAnchor(morphologicalEntryView, offset);
        setTopAnchor(morphologicalEntryView, DEFAULT_OFFSET);
        morphologicalEntryView.setMorphologicalEntry(null);
        morphologicalEntryView.setMorphologicalEntry((location == null) ? null : location.getMorphologicalEntry());

        return propertiesView;
    }

    private void refreshPropertiesView(WordType wordType) {
        final Location location = control.getLocation();
        if (location != null) {
            location.setWordType(wordType);
            final ObservableList<Node> children = getChildren();
            if (children != null && !children.isEmpty()) {
                children.set(1, refreshPropertiesView(location));
            }
        }
    }

    private AbstractPropertiesView getPropertiesView(WordType wordType) {
        AbstractPropertiesView node = null;
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
        return node;
    }
}
