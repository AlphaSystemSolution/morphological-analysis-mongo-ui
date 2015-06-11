package com.alphasystem.morphologicalanalysis.wordbyword.ui.model;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.*;
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
    // common properties
    private final ObjectProperty<NumberType> numberType = new SimpleObjectProperty<>();
    private final ObjectProperty<GenderType> genderType = new SimpleObjectProperty<>();
    // noun properties
    private final ObjectProperty<NounStatus> nounStatus = new SimpleObjectProperty<>();
    private final ObjectProperty<NounKind> nounKind = new SimpleObjectProperty<>();
    private final ObjectProperty<NounType> nounType = new SimpleObjectProperty<>();
    // pronoun properties
    private final ObjectProperty<ConversationType> conversationType = new SimpleObjectProperty<>();
    private final ObjectProperty<ProNounType> proNounType = new SimpleObjectProperty<>();
    // verb properties
    private final ObjectProperty<VerbType> verbType = new SimpleObjectProperty<>();
    private final ObjectProperty<VerbMode> verbMode = new SimpleObjectProperty<>();
    private Token token;
    private Location location;

    public TokenAdapter() {
        initListeners();
    }

    public VerbMode getVerbMode() {
        return verbModeProperty().get();
    }

    public void setVerbMode(VerbMode verbMode) {
        this.verbMode.set(verbMode);
    }

    public final ObjectProperty<VerbMode> verbModeProperty() {
        return verbMode;
    }

    public VerbType getVerbType() {
        return verbTypeProperty().get();
    }

    public void setVerbType(VerbType verbType) {
        this.verbType.set(verbType);
    }

    public ObjectProperty<VerbType> verbTypeProperty() {
        return verbType;
    }

    public ProNounType getProNounType() {
        return proNounTypeProperty().get();
    }

    public void setProNounType(ProNounType proNounType) {
        this.proNounType.set(proNounType);
    }

    public ObjectProperty<ProNounType> proNounTypeProperty() {
        return proNounType;
    }

    public ConversationType getConversationType() {
        return conversationTypeProperty().get();
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType.set(conversationType);
    }

    public ObjectProperty<ConversationType> conversationTypeProperty() {
        return conversationType;
    }

    public NounType getNounType() {
        return nounTypeProperty().get();
    }

    public void setNounType(NounType nounType) {
        this.nounType.set(nounType);
    }

    public ObjectProperty<NounType> nounTypeProperty() {
        return nounType;
    }

    public NounKind getNounKind() {
        return nounKindProperty().get();
    }

    public void setNounKind(NounKind nounKind) {
        this.nounKind.set(nounKind);
    }

    public ObjectProperty<NounKind> nounKindProperty() {
        return nounKind;
    }

    public NounStatus getNounStatus() {
        return nounStatusProperty().get();
    }

    public void setNounStatus(NounStatus nounStatus) {
        this.nounStatus.set(nounStatus);
    }

    public ObjectProperty<NounStatus> nounStatusProperty() {
        return nounStatus;
    }

    public GenderType getGenderType() {
        return genderTypeProperty().get();
    }

    public void setGenderType(GenderType genderType) {
        this.genderType.set(genderType);
    }

    public ObjectProperty<GenderType> genderTypeProperty() {
        return genderType;
    }

    public NumberType getNumberType() {
        return numberTypeProperty().get();
    }

    public void setNumberType(NumberType numberType) {
        this.numberType.set(numberType);
    }

    public ObjectProperty<NumberType> numberTypeProperty() {
        return numberType;
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

    public void updateToken(Token t, int locationIndex) {
        letters.clear();
        selectedValues.clear();
        locations.clear();
        if (t == null) {
            return;
        }
        this.token = t;
        this.location = token.getLocations().get(locationIndex);
        ArabicWord tokenWord = token.getTokenWord();
        this.letters.addAll(tokenWord.getLetters());
        // initialization
        for (int i = 0; i < this.letters.size(); i++) {
            selectedValues.add(new SimpleBooleanProperty(false));
        }
        // sets values for current location
        if (!location.isTransient()) {
            for (int i = location.getStartIndex(); i < location.getEndIndex(); i++) {
                selectedValues.get(i).setValue(true);
            }
        }
        this.locations.addAll(token.getLocations());
        AbstractProperties p = location.getProperties();
        setTranslation(location.getTranslation());
        setPartOfSpeech(location.getPartOfSpeech());
        // upon calling setPartOfSpeech, the properties gets cleared, restore it by setting it back
        location.setProperties(p);
        setNamedTemplate(location.getFormTemplate());
        setNamedTag(location.getNamedTag());
        AbstractProperties properties = location.getProperties();
        setNumberType(properties.getNumber());
        setGenderType(properties.getGender());
        if (isNoun(properties)) {
            NounProperties np = (NounProperties) properties;
            setNounStatus(np.getStatus());
            setNounKind(np.getNounKind());
            setNounType(np.getNounType());
        } else if (isPronoun(properties)) {
            ProNounProperties pp = (ProNounProperties) properties;
            setNounStatus(pp.getStatus());
            setConversationType(pp.getConversationType());
            setProNounType(pp.getProNounType());
        } else if (isVerb(properties)) {
            VerbProperties vp = (VerbProperties) properties;
            setConversationType(vp.getConversationType());
            setVerbMode(vp.getMode());
            setVerbType(vp.getVerbType());
        }
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
        numberType.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (properties != null) {
                properties.setNumber(newValue);
            }
        });
        genderType.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (properties != null) {
                properties.setGender(newValue);
            }
        });
        nounStatus.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isNoun(properties) || isPronoun(properties)) {
                AbstractNounProperties anp = (AbstractNounProperties) properties;
                anp.setStatus(newValue);
            }
        });
        nounKind.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isNoun(properties)) {
                NounProperties np = (NounProperties) properties;
                np.setNounKind(newValue);
            }
        });
        nounType.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isNoun(properties)) {
                NounProperties np = (NounProperties) properties;
                np.setNounType(newValue);
            }
        });
        conversationType.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isPronoun(properties)) {
                ProNounProperties pnp = (ProNounProperties) properties;
                pnp.setConversationType(newValue);
            }
            if (isVerb(properties)) {
                VerbProperties vp = (VerbProperties) properties;
                vp.setConversationType(newValue);
            }
        });
        proNounType.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isPronoun(properties)) {
                ProNounProperties pnp = (ProNounProperties) properties;
                pnp.setProNounType(newValue);
            }
        });
        verbType.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isVerb(properties)) {
                VerbProperties vp = (VerbProperties) properties;
                vp.setVerbType(newValue);
            }
        });
        verbMode.addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isVerb(properties)) {
                VerbProperties vp = (VerbProperties) properties;
                vp.setMode(newValue);
            }
        });
    }
}
