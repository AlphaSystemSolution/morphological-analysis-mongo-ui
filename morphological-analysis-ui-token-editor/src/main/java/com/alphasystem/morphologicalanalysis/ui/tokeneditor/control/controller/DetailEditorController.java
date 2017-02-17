package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.DetailEditorView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class DetailEditorController extends BorderPane {

    @Autowired private DetailEditorView control;

    @PostConstruct
    void postConstruct() {
        control.locationProperty().addListener((observable, oldValue, newValue) -> refresh(newValue));
        TabPane tabPane = new TabPane();
        setCenter(tabPane);
        refresh(control.getLocation());
    }

    private void refresh(Location location) {
        if (location == null) {
            return;
        }
    }
}
