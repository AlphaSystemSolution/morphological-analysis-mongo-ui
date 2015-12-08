package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.CommonPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RootWord;
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
    private final ObjectProperty<NamedTemplate> namedTemplate = new SimpleObjectProperty<>(null, "namedTemplate");
    private final ObjectProperty<RootWord> rootWord = new SimpleObjectProperty<>(null, "rootWord");
    private final ObjectProperty<String> translation = new SimpleObjectProperty<>(null, "translation");

    public CommonPropertiesView() {
        locationProperty().addListener((o, ov, nv) -> setValues(nv));
        partOfSpeechProperty().addListener((o, ov, nv) -> getLocation().setPartOfSpeech((nv == null) ? NOUN : nv));
        namedTagProperty().addListener((o, ov, nv) -> getLocation().setNamedTag(nv));
        namedTemplateProperty().addListener((o, ov, nv) ->
                getLocation().setFormTemplate((nv == null) ? null : nv));
        rootWordProperty().addListener((o, ov, nv) -> getLocation().setRootWord((nv == null) ? new RootWord() : nv));
        translationProperty().addListener((o, ov, nv) -> getLocation().setTranslation(nv));
    }

    private void setValues(Location location) {
        setPartOfSpeech((location == null) ? NOUN : location.getPartOfSpeech());
        setNamedTag((location == null) ? null : location.getNamedTag());
        setNamedTemplate((location == null) ? null : location.getFormTemplate());
        setRootWord((location == null) ? new RootWord() : location.getRootWord());
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

    public final NamedTemplate getNamedTemplate() {
        return namedTemplate.get();
    }

    public final void setNamedTemplate(NamedTemplate namedTemplate) {
        this.namedTemplate.set((namedTemplate == null) ? null : namedTemplate);
    }

    public final ObjectProperty<NamedTemplate> namedTemplateProperty() {
        return namedTemplate;
    }

    public final RootWord getRootWord() {
        return rootWord.get();
    }

    public final void setRootWord(RootWord rootWord) {
        this.rootWord.set((rootWord == null) ? new RootWord() : rootWord);
    }

    public final ObjectProperty<RootWord> rootWordProperty() {
        return rootWord;
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
