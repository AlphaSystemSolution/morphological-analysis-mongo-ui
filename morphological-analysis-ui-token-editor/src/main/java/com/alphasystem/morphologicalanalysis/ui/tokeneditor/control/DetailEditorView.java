package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.DetailEditorSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class DetailEditorView extends Control {

    private final ObjectProperty<Location> location = new SimpleObjectProperty<>(this, "location");

    @PostConstruct
    void postConstruct(){
        setSkin(createDefaultSkin());
    }

    public final Location getLocation() {
        return location.get();
    }

    public final void setLocation(Location location) {
        this.location.set(location);
    }

    public final ObjectProperty<Location> locationProperty() {
        return location;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DetailEditorSkin(this);
    }
}
