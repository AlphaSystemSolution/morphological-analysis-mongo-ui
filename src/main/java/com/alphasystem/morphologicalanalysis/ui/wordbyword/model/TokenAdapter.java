package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

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
    private final ObjectProperty<RootWord> rootWord = new SimpleObjectProperty<>();
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
    private final ObjectProperty<IncompleteVerbCategory> incompleteVerbCategory = new SimpleObjectProperty<>();
    private final ObjectProperty<IncompleteVerbType> incompleteVerbType = new SimpleObjectProperty<>();
    private Token token;
    private Location location;

    public TokenAdapter() {
        initListeners();
    }

    public void setVerbMode(VerbMode verbMode) {
        this.verbMode.set(verbMode);
    }

    public IncompleteVerbType getIncompleteVerbType() {
        return incompleteVerbType.get();
    }

    public void setIncompleteVerbType(IncompleteVerbType incompleteVerbType) {
        this.incompleteVerbType.set(incompleteVerbType);
    }

    public IncompleteVerbCategory getIncompleteVerbCategory() {
        return incompleteVerbCategory.get();
    }

    public void setIncompleteVerbCategory(IncompleteVerbCategory incompleteVerbCategory) {
        this.incompleteVerbCategory.set(incompleteVerbCategory);
    }

    public ObjectProperty<IncompleteVerbCategory> incompleteVerbCategoryProperty() {
        return incompleteVerbCategory;
    }

    public ObjectProperty<IncompleteVerbType> incompleteVerbTypeProperty() {
        return incompleteVerbType;
    }

    public final ObjectProperty<VerbMode> verbModeProperty() {
        return verbMode;
    }

    public void setVerbType(VerbType verbType) {
        this.verbType.set(verbType);
    }

    public ObjectProperty<VerbType> verbTypeProperty() {
        return verbType;
    }

    public void setProNounType(ProNounType proNounType) {
        this.proNounType.set(proNounType);
    }

    public ObjectProperty<ProNounType> proNounTypeProperty() {
        return proNounType;
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType.set(conversationType);
    }

    public ObjectProperty<ConversationType> conversationTypeProperty() {
        return conversationType;
    }

    public void setNounType(NounType nounType) {
        this.nounType.set(nounType);
    }

    public ObjectProperty<NounType> nounTypeProperty() {
        return nounType;
    }

    public void setNounKind(NounKind nounKind) {
        this.nounKind.set(nounKind);
    }

    public ObjectProperty<NounKind> nounKindProperty() {
        return nounKind;
    }

    public void setNounStatus(NounStatus nounStatus) {
        this.nounStatus.set(nounStatus);
    }

    public ObjectProperty<NounStatus> nounStatusProperty() {
        return nounStatus;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType.set(genderType);
    }

    public ObjectProperty<GenderType> genderTypeProperty() {
        return genderType;
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

    public final void setNamedTemplate(NamedTemplate namedTemplate) {
        this.namedTemplate.set(namedTemplate);
    }

    public final ObjectProperty<NamedTemplate> namedTemplateProperty() {
        return namedTemplate;
    }

    public final void setNamedTag(NamedTag namedTag) {
        this.namedTag.set(namedTag);
    }

    public final ObjectProperty<NamedTag> namedTagProperty() {
        return namedTag;
    }

    public final ObjectProperty<RootWord> rootWordProperty() {
        return rootWord;
    }

    public final void setRootWord(RootWord rootWord) {
        this.rootWord.set(rootWord);
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
        setRootWord(location.getRootWord());
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
            IncompleteVerb incompleteVerb = vp.getIncompleteVerb();
            if (incompleteVerb != null) {
                setIncompleteVerbCategory(incompleteVerb.getCategory());
                setIncompleteVerbType(incompleteVerb.getType());
            }
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
        rootWordProperty().addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                RootWord rw = newValue == null ? new RootWord() : newValue;
                location.setRootWord(rw);
            }
        });
        translationProperty().addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                String translation = isBlank(newValue) ? null : newValue;
                location.setTranslation(translation);
            }
        });
        partOfSpeechProperty().addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                location.setPartOfSpeech(newValue);
            }
        });
        namedTemplateProperty().addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                location.setFormTemplate(newValue);
            }
        });
        namedTagProperty().addListener((observable, oldValue, newValue) -> {
            if (location != null) {
                location.setNamedTag(newValue);
            }
        });
        numberTypeProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (properties != null) {
                properties.setNumber(newValue);
            }
        });
        genderTypeProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (properties != null) {
                properties.setGender(newValue);
            }
        });
        nounStatusProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isNoun(properties) || isPronoun(properties)) {
                AbstractNounProperties anp = (AbstractNounProperties) properties;
                anp.setStatus(newValue);
            }
        });
        nounKindProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isNoun(properties)) {
                NounProperties np = (NounProperties) properties;
                np.setNounKind(newValue);
            }
        });
        nounTypeProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isNoun(properties)) {
                NounProperties np = (NounProperties) properties;
                np.setNounType(newValue);
            }
        });
        conversationTypeProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isPronoun(properties)) {
                ProNounProperties pnp = (ProNounProperties) properties;
                pnp.setConversationType(newValue);
            } else if (isVerb(properties)) {
                VerbProperties vp = (VerbProperties) properties;
                vp.setConversationType(newValue);
            }
        });
        proNounTypeProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isPronoun(properties)) {
                ProNounProperties pnp = (ProNounProperties) properties;
                pnp.setProNounType(newValue);
            }
        });
        verbTypeProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isVerb(properties)) {
                VerbProperties vp = (VerbProperties) properties;
                vp.setVerbType(newValue);
            }
        });
        verbModeProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isVerb(properties)) {
                VerbProperties vp = (VerbProperties) properties;
                vp.setMode(newValue);
            }
        });
        incompleteVerbCategoryProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isVerb(properties)) {
                VerbProperties vp = (VerbProperties) properties;
                IncompleteVerb incompleteVerb = createIncompleteVerb(newValue);
                vp.setIncompleteVerb(incompleteVerb);
            }
        });
        incompleteVerbTypeProperty().addListener((observable, oldValue, newValue) -> {
            AbstractProperties properties = (location == null) ? null : location.getProperties();
            if (isVerb(properties)) {
                VerbProperties vp = (VerbProperties) properties;
                IncompleteVerb incompleteVerb = vp.getIncompleteVerb();
                if (incompleteVerb == null) {
                    incompleteVerb = createIncompleteVerb(getIncompleteVerbCategory());
                    vp.setIncompleteVerb(incompleteVerb);
                }
                incompleteVerb.setType(newValue);
            }
        });
    }

    @SuppressWarnings({"unchecked"})
    private IncompleteVerb createIncompleteVerb(IncompleteVerbCategory incompleteVerbCategory) {
        IncompleteVerb incompleteVerb = null;
        if (incompleteVerbCategory == null) {
            return null;
        }
        Class<? extends IncompleteVerb> categoryClassName = incompleteVerbCategory.getCategoryClassName();
        try {
            incompleteVerb = categoryClassName.newInstance();
            incompleteVerb.setCategory(incompleteVerbCategory);
            incompleteVerb.setType(incompleteVerbCategory.getMembers()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return incompleteVerb;
    }
}
