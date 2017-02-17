package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.DetailEditorSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
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
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>(this, "rootLetters");
    private final ReadOnlyBooleanWrapper emptyRootLetters = new ReadOnlyBooleanWrapper(this, "emptyRootLetters", true);
    private final ObjectProperty<NamedTemplate> form = new SimpleObjectProperty<>(this, "form", NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE);

    @PostConstruct
    void postConstruct(){
        setSkin(createDefaultSkin());
        rootLettersProperty().addListener((o, ov, nv) -> emptyRootLetters.setValue(nv == null || nv.isEmpty()));
        emptyRootLetters.set(true);
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

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DetailEditorSkin(this);
    }
}
