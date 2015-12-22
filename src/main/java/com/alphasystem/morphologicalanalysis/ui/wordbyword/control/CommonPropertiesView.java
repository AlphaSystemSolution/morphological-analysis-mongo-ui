package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.CommonPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.NOUN;

/**
 * @author sali
 */
public class CommonPropertiesView extends Control {

    private final ObjectProperty<Location> location = new SimpleObjectProperty<>(null, "location");
    private final ObjectProperty<PartOfSpeech> partOfSpeech = new SimpleObjectProperty<>(null, "partOfSpeech");
    private final ObjectProperty<NamedTag> namedTag = new SimpleObjectProperty<>(null, "namedTag");
    private final ObjectProperty<String> translation = new SimpleObjectProperty<>(null, "translation");

    public CommonPropertiesView() {
        locationProperty().addListener((o, ov, nv) -> setValues(nv));
        partOfSpeechProperty().addListener((o, ov, nv) -> {
            // setting part of speech will make properties re-initialized, which will override the location
            // properties, there is a case where we don't want to have this override to be happened.

            // Case 1: We already have value in the DB and from UI we selected the location from drop down,
            // from "LocationPropertiesSkin" line 47 we changed the common properties view (this class) and
            // that will trigger the change in part of speech, that's why we are here.
            // Now we need to change the part of speech in UI but will reset the properties as well which is we
            // want to be happened. So we want to keep track current sets of properties and put them back once
            // part of speech is set

            // Case 2: This is completely new sets of properties, when properties are initialized by default they
            // are assigned "NOUN" as part of speech, now if we are changing to some other type of part of speech
            // then holding of current sets of properties will not work, in this case we want the override to be
            // happened

            Location location = getLocation();
            AbstractProperties properties = location.getProperties();
            location.setPartOfSpeech((nv == null) ? NOUN : nv);

            // trick is to find out that if current location is transient or not, sets the properties back if and
            // only if the location is not transient (means it does not have start and end index set.
            if (!location.isTransient()) {
                location.setProperties(properties);
            }
        });
        namedTagProperty().addListener((o, ov, nv) -> getLocation().setNamedTag(nv));
        translationProperty().addListener((o, ov, nv) -> getLocation().setTranslation(nv));
    }

    private void setValues(Location location) {
        setPartOfSpeech((location == null) ? NOUN : location.getPartOfSpeech());
        setNamedTag((location == null) ? null : location.getNamedTag());
        setTranslation(location == null ? null : location.getTranslation());
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

    public final PartOfSpeech getPartOfSpeech() {
        return partOfSpeech.get();
    }

    public final void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech.set((partOfSpeech == null) ? NOUN : partOfSpeech);
    }

    public final ObjectProperty<PartOfSpeech> partOfSpeechProperty() {
        return partOfSpeech;
    }

    public final NamedTag getNamedTag() {
        return namedTag.get();
    }

    public final void setNamedTag(NamedTag namedTag) {
        this.namedTag.set(namedTag);
    }

    public final ObjectProperty<NamedTag> namedTagProperty() {
        return namedTag;
    }

    public final String getTranslation() {
        return translation.get();
    }

    public final void setTranslation(String translation) {
        this.translation.set(translation);
    }

    public final ObjectProperty<String> translationProperty() {
        return translation;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CommonPropertiesSkin(this);
    }
}
