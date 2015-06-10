package com.alphasystem.morphologicalanalysis.wordbyword.ui.model;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import static javafx.collections.FXCollections.observableArrayList;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public final class TokenAdapter {

    private final ObservableList<ArabicLetter> letters = observableArrayList();
    private final ObservableList<Location> locations = observableArrayList();
    private final ObservableList<BooleanProperty> selectedValues = observableArrayList();
    private final StringProperty translation = new SimpleStringProperty();
    private final ObjectProperty<PartOfSpeech> partOfSpeech = new SimpleObjectProperty<>();
    private final ObjectProperty<NamedTemplate> namedTemplate = new SimpleObjectProperty<>();
    private final ObjectProperty<NamedTag> namedTag = new SimpleObjectProperty<>();
    private Token token;
    private Location location;

    public TokenAdapter() {
        initListeners();
    }

    private void initListeners() {
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
        namedTemplate.addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                location.setFormTemplate(newValue);
            }
        });
        namedTag.addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                location.setNamedTag(newValue);
            }
        });
    }

    public ObservableList<BooleanProperty> getSelectedValues() {
        return selectedValues;
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

    public final NamedTemplate getNamedTemplate() {
        return namedTemplateProperty().get();
    }

    public final void setNamedTemplate(NamedTemplate namedTemplate) {
        this.namedTemplate.set(namedTemplate);
    }

    public final ObjectProperty<NamedTemplate> namedTemplateProperty() {
        return namedTemplate;
    }

    public final NamedTag getNamedTag() {
        return namedTagProperty().get();
    }

    public final void setNamedTag(NamedTag namedTag) {
        this.namedTag.set(namedTag);
    }

    public final ObjectProperty<NamedTag> namedTagProperty() {
        return namedTag;
    }

    public Location getLocation() {
        return location;
    }

    public Token getToken() {
        return token;
    }

    public void updateToken(Token token, int locationIndex) {
        letters.clear();
        selectedValues.clear();
        locations.clear();
        if (token == null) {
            return;
        }
        this.token = token;
        location = token.getLocations().get(locationIndex);
        ArabicWord tokenWord = token.getTokenWord();
        this.letters.addAll(tokenWord.getLetters());
        // initialization
        for (int i = 0; i < this.letters.size(); i++) {
            selectedValues.add(new SimpleBooleanProperty(false));
        }
        // sets values for current location
        if(!location.isTransient()){
            for (int i = location.getStartIndex(); i < location.getEndIndex(); i++) {
                selectedValues.get(i).setValue(true);
            }
        }
        this.locations.addAll(token.getLocations());
        setTranslation(location.getTranslation());
        setPartOfSpeech(location.getPartOfSpeech());
        setNamedTemplate(location.getFormTemplate());
        setNamedTag(location.getNamedTag());
    }

    public void updateLocationStartAndEndIndices() {
        int startIndex = -1;
        int endIndex = 0;
        for (int i = 0; i < selectedValues.size(); i++) {
            BooleanProperty value = selectedValues.get(i);
            if (value.get()) {
                if (startIndex == -1) {
                    startIndex = i;
                }
                endIndex = i + 1;
            }
        } // end of for loop

        location.setStartIndex(startIndex);
        location.setEndIndex(endIndex);
    }
}
