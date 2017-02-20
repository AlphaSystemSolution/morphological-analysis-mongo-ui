package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.ParticlePropertiesView;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class ParticlePropertiesController extends BorderPane {

    @Autowired private ParticlePropertiesView control;

    @PostConstruct
    void postConstruct(){

    }
}
