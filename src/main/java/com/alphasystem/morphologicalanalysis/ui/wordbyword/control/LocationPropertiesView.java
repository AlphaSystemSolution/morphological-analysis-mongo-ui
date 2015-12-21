package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.LocationPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class LocationPropertiesView extends Control {

    private final ObjectProperty<Location> location = new SimpleObjectProperty<>(null, "location");
    private final ObjectProperty<Object> updatedProperty = new SimpleObjectProperty<>(null, "updatedProperty");

    public LocationPropertiesView() {
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

    public final Object getUpdatedProperty() {
        return updatedProperty.get();
    }

    public final void setUpdatedProperty(Object updatedProperty) {
        this.updatedProperty.set(updatedProperty);
    }

    public final ObjectProperty<Object> updatedPropertyProperty() {
        return updatedProperty;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LocationPropertiesSkin(this);
    }
}
