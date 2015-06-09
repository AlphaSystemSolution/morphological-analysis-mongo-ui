package com.alphasystem.morphologicalanalysis.wordbyword.ui.model;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import static javafx.collections.FXCollections.observableArrayList;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public final class TokenAdapter {

    private final ObservableList<ArabicLetter> letters = observableArrayList();
    private final ObservableList<Location> locations = observableArrayList();
    private final StringProperty translation = new SimpleStringProperty();
    private final ObjectProperty<PartOfSpeech> partOfSpeech = new SimpleObjectProperty<>();
    private Location location;

    public TokenAdapter() {
        translation.addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                String translation = isBlank(newValue) ? null : newValue;
                location.setTranslation(translation);
            }
        });
        partOfSpeech.addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                location.setPartOfSpeech(newValue);
            }
        });
    }

    public final PartOfSpeech getPartOfSpeech() {
        return partOfSpeechProperty().get();
    }

    public final void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech.set(partOfSpeech);
    }

    public final ObjectProperty<PartOfSpeech> partOfSpeechProperty() {
        return partOfSpeech;
    }

    public final String getTranslation() {
        return translation.get();
    }

    public final void setTranslation(String translation) {
        this.translation.set(translation);
    }

    public final StringProperty translationProperty() {
        return translation;
    }

    public ObservableList<ArabicLetter> getLetters() {
        return letters;
    }

    public ObservableList<Location> getLocations() {
        return locations;
    }

    public void updateToken(Token token, int locationIndex) {
        letters.clear();
        locations.clear();
        if (token == null) {
            return;
        }
        location = token.getLocations().get(locationIndex);
        ArabicWord tokenWord = token.getTokenWord();
        this.letters.addAll(tokenWord.getLetters());
        this.locations.addAll(token.getLocations());
        setTranslation(location.getTranslation());
        setPartOfSpeech(location.getPartOfSpeech());
    }
}
