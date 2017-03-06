package com.alphasystem.morphologicalanalysis.ui.access.control;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class DataExportView extends Control {

    @PostConstruct
    void postConstruct() {
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DataExportSkin(this);
    }
}
