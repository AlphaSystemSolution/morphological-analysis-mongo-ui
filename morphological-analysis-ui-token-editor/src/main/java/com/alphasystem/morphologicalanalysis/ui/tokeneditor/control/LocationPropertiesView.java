package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.LocationPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class LocationPropertiesView extends Control {

    private final ObjectProperty<Location> location = new SimpleObjectProperty<>(this, "location");
    private final BooleanProperty morphologicalEntry = new SimpleBooleanProperty(this, "morphologicalEntry", false);

    @PostConstruct
    void postConstruct(){
        setSkin(createDefaultSkin());
        setMorphologicalEntry(false);
    }

    public final Location getLocation() {
        return location.get();
    }

    public final ObjectProperty<Location> locationProperty() {
        return location;
    }

    public final void setLocation(Location location) {
        this.location.set(location);
    }

    public final boolean isMorphologicalEntry() {
        return morphologicalEntry.get();
    }

    public final BooleanProperty morphologicalEntryProperty() {
        return morphologicalEntry;
    }

    public final void setMorphologicalEntry(boolean morphologicalEntry) {
        this.morphologicalEntry.set(morphologicalEntry);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LocationPropertiesSkin(this);
    }
}
