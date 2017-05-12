package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.AbstractPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.MorphologicalEntryView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.NounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.service.RetrieveMorphologicalEntryService;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

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
        morphologicalEntryView.rootLettersProperty().addListener((observable, oldValue, newValue) -> retrieveEntry(newValue, null));
        morphologicalEntryView.formProperty().addListener((observable, oldValue, newValue) -> retrieveEntry(null, newValue));
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
        switch (wordType) {
            case PARTICLE:
                offset += 60;
                break;
        }
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
                offset += 390;
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

    private void retrieveEntry(RootLetters rootLetters, NamedTemplate form) {
        control.setMorphologicalEntry(false);
        final MorphologicalEntry morphologicalEntry = morphologicalEntryView.getMorphologicalEntry();
        if (morphologicalEntry != null && rootLetters == null) {
            rootLetters = morphologicalEntry.getRootLetters();
        }
        if (rootLetters == null || rootLetters.isEmpty()) {
            return;
        }

        if (morphologicalEntry != null && form == null) {
            form = morphologicalEntry.getForm();
        }
        if (form == null) {
            return;
        }

        UiUtilities.waitCursor(control);
        RetrieveMorphologicalEntryService morphologicalEntryService = new RetrieveMorphologicalEntryService(rootLetters, form);
        morphologicalEntryService.setOnSucceeded(event -> {
            UiUtilities.defaultCursor(control);
            Worker source = event.getSource();
            MorphologicalEntry value = (MorphologicalEntry) source.getValue();
            if (value == null || (morphologicalEntry != null && morphologicalEntry.equals(value))) {
                value = morphologicalEntry;
            } else {
                final Location location = control.getLocation();
                if (location != null) {
                    control.getLocation().setMorphologicalEntry(value);
                }
            }
            control.setMorphologicalEntry(true);
            morphologicalEntryView.setMorphologicalEntry(value);
        });
        morphologicalEntryService.setOnFailed(event -> {
            UiUtilities.defaultCursor(control);
            Worker source = event.getSource();
            try {
                throw source.getException();
            } catch (Throwable throwable) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(String.format("%s:%s", throwable.getClass().getName(), throwable.getMessage()));
                alert.showAndWait();
            }
        });
        morphologicalEntryService.start();
    }
}
