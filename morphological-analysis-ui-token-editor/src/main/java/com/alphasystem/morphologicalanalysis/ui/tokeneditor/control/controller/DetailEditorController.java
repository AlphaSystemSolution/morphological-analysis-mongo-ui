package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.DetailEditorView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ResourceBundle;

/**
 * @author sali
 */
@Component
public class DetailEditorController extends BorderPane {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(DetailEditorView.class.getSimpleName());
    @Autowired private DetailEditorView control;
    @Autowired private LocationPropertiesView locationPropertiesView;
    private final TabPane tabPane = new TabPane();

    @PostConstruct
    void postConstruct() {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        control.locationProperty().addListener((observable, oldValue, newValue) -> refresh(newValue));
        setCenter(tabPane);
        initializeSkin();
        refresh(control.getLocation());
    }

    private void initializeSkin(){
        locationPropertiesView.setLocation(control.getLocation());
        Tab tab = new Tab(RESOURCE_BUNDLE.getString("locationProperties.label"), locationPropertiesView);
        tabPane.getTabs().add(tab);
    }

    private void refresh(Location location) {
        locationPropertiesView.setLocation(location);
    }
}
