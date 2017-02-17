package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.CommonPropertiesSkin;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class CommonPropertiesView extends Control {

    private final ObjectProperty<Location> location = new SimpleObjectProperty<>(this, "location");
    private final ObjectProperty<WordType> wordType = new SimpleObjectProperty<>(this, "wordType");
    private final ObjectProperty<NamedTag> namedTag = new SimpleObjectProperty<>(this, "namedTag");
    private final StringProperty text = new SimpleStringProperty(this, "text");
    private final ReadOnlyStringWrapper derivedText = new ReadOnlyStringWrapper(this, "derivedText");
    private final StringProperty translation = new SimpleStringProperty(this, "translation");

    @PostConstruct
    void postConstruct() {
        setSkin(createDefaultSkin());
        locationProperty().addListener((observable, oldValue, newValue) -> setValues(newValue));
        wordTypeProperty().addListener((observable, oldValue, newValue) -> updateWordType(newValue));
        namedTagProperty().addListener((observable, oldValue, newValue) -> updateNamedTag(newValue));
        textProperty().addListener((observable, oldValue, newValue) -> updateText(newValue));
        translationProperty().addListener((observable, oldValue, newValue) -> updateTranslation(newValue));
    }

    @Override
    public String getUserAgentStylesheet() {
        return ApplicationHelper.STYLE_SHEET_PATH;
    }

    private void setValues(Location location) {
        setWordType((location == null) ? WordType.NOUN : location.getWordType());
        setNamedTag((location == null) ? null : location.getNamedTag());
        setText((location == null) ? null : location.getText());
        setDerivedText((location == null) ? null : location.getDerivedText());
        setTranslation((location == null) ? null : location.getTranslation());
    }

    private void updateWordType(WordType wordType) {
        final Location location = getLocation();
        if (location != null) {
            final WordType currentWordType = location.getWordType();
            if (!currentWordType.equals(wordType)) {
                System.out.println(String.format("<<<<< Current Word Type: %s, Word Type: %s >>>>>", currentWordType, wordType));
            }
            location.setWordType(wordType);
        }
    }

    private void updateNamedTag(NamedTag namedTag) {
        final Location location = getLocation();
        if (location != null) {
            location.setNamedTag(namedTag);
        }
    }

    private void updateText(String text) {
        final Location location = getLocation();
        if (location != null) {
            location.setText(text);
        }
    }

    private void updateTranslation(String translation) {
        final Location location = getLocation();
        if (location != null) {
            location.setTranslation(translation);
        }
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

    public final WordType getWordType() {
        return wordType.get();
    }

    public final ObjectProperty<WordType> wordTypeProperty() {
        return wordType;
    }

    public final void setWordType(WordType wordType) {
        this.wordType.set(wordType);
    }

    public final NamedTag getNamedTag() {
        return namedTag.get();
    }

    public final ObjectProperty<NamedTag> namedTagProperty() {
        return namedTag;
    }

    public final void setNamedTag(NamedTag namedTag) {
        this.namedTag.set(namedTag);
    }

    public final String getText() {
        return text.get();
    }

    public final StringProperty textProperty() {
        return text;
    }

    public final void setText(String text) {
        this.text.set(text);
    }

    public final String getDerivedText() {
        return derivedText.get();
    }

    public final ReadOnlyStringProperty derivedTextProperty() {
        return derivedText.getReadOnlyProperty();
    }

    private final void setDerivedText(String derivedText) {
        this.derivedText.set(derivedText);
    }

    public final String getTranslation() {
        return translation.get();
    }

    public final StringProperty translationProperty() {
        return translation;
    }

    public final void setTranslation(String translation) {
        this.translation.set(translation);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CommonPropertiesSkin(this);
    }
}
