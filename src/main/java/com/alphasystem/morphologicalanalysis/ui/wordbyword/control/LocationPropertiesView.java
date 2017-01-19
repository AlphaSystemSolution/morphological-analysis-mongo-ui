package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.LocationPropertiesSkin;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class LocationPropertiesView extends Control {

    private final RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private final ObjectProperty<Location> location = new SimpleObjectProperty<>(null, "location");
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>(null, "rootLetters");
    private final ReadOnlyBooleanWrapper emptyRootLetters = new ReadOnlyBooleanWrapper(true, "emptyRootLetters");
    private final ObjectProperty<NamedTemplate> form = new SimpleObjectProperty<>(null, "form");
    private final BooleanProperty reload = new SimpleBooleanProperty(null, "reload");

    public LocationPropertiesView() {
        rootLettersProperty().addListener((o, ov, nv) -> emptyRootLetters.setValue(nv == null || nv.isEmpty()));
        emptyRootLetters.set(true);
        reloadProperty().addListener((o, ov, nv) -> {
            if (nv) {
                Location location = getLocation();
                if (location != null) {
                    location = repositoryTool.getLocation(location.getDisplayName());
                    setLocation(null);
                    setLocation(location);
                }
            }
        });
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

    public ReadOnlyBooleanProperty emptyRootLettersProperty() {
        return emptyRootLetters.getReadOnlyProperty();
    }

    public final ObjectProperty<RootLetters> rootLettersProperty() {
        return rootLetters;
    }

    public final void setRootLetters(RootLetters rootLetters) {
        this.rootLetters.set(rootLetters);
    }

    public final ObjectProperty<NamedTemplate> formProperty() {
        return form;
    }

    public final void setForm(NamedTemplate form) {
        this.form.set(form);
    }

    public final BooleanProperty reloadProperty() {
        return reload;
    }

    public final void setReload(boolean reload) {
        this.reload.set(reload);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LocationPropertiesSkin(this);
    }
}
