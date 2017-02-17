package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationPropertiesView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class LocationPropertiesController extends AnchorPane {

    @Autowired private LocationPropertiesView control;
    @Autowired private CommonPropertiesView commonPropertiesView;

    @PostConstruct
    void postConstruct(){
       commonPropertiesView.setLocation(control.getLocation());
        control.locationProperty().addListener((observable, oldValue, newValue) -> {
            commonPropertiesView.setLocation(newValue);
        });
        setLeftAnchor(commonPropertiesView, 10.0);
        getChildren().addAll(commonPropertiesView);
    }
}
