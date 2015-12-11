package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.ui.common.AbstractDocumentAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.*;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.*;
import static java.lang.Boolean.FALSE;

/**
 * @author sali
 */
public final class LocationAdapter extends AbstractDocumentAdapter<Location> {

    private final IntegerProperty chapterNumber = new SimpleIntegerProperty(0, "chapterNumber");
    private final IntegerProperty verseNumber = new SimpleIntegerProperty(0, "verseNumber");
    private final IntegerProperty tokenNumber = new SimpleIntegerProperty(0, "tokenNumber");
    private final IntegerProperty locationNumber = new SimpleIntegerProperty(0, "locationNumber");
    private final BooleanProperty hidden = new SimpleBooleanProperty(FALSE, "hidden");
    private final IntegerProperty startIndex = new SimpleIntegerProperty(0, "startIndex");
    private final IntegerProperty endIndex = new SimpleIntegerProperty(0, "endIndex");
    private final ObjectProperty<PartOfSpeech> partOfSpeech = new SimpleObjectProperty<>(null, "partOfSpeech");
    private final ObjectProperty<RootWordAdapter> rootWord = new SimpleObjectProperty<>(null, "rootWord");
    private final ObjectProperty<NamedTag> namedTag = new SimpleObjectProperty<>(null, "namedTag");
    private final ObjectProperty<NamedTemplate> formTemplate = new SimpleObjectProperty<>(null, "formTemplate");
    private final StringProperty translation = new SimpleStringProperty(null, "translation");
    private final ObjectProperty<AbstractPropertiesAdapter> properties = new SimpleObjectProperty<>(null, "properties");

    public LocationAdapter() {
        super();
        chapterNumberProperty().addListener((o, ov, nv) -> getSrc().setChapterNumber((Integer) nv));
        verseNumberProperty().addListener((o, ov, nv) -> getSrc().setVerseNumber((Integer) nv));
        tokenNumberProperty().addListener((o, ov, nv) -> getSrc().setTokenNumber((Integer) nv));
        locationNumberProperty().addListener((o, ov, nv) -> getSrc().setLocationNumber((Integer) nv));
        hiddenProperty().addListener((o, ov, nv) -> getSrc().setHidden(nv));
        startIndexProperty().addListener((o, ov, nv) -> getSrc().setStartIndex((Integer) nv));
        endIndexProperty().addListener((o, ov, nv) -> getSrc().setEndIndex((Integer) nv));
        partOfSpeechProperty().addListener((o, ov, nv) -> getSrc().setPartOfSpeech(nv));
        rootWordProperty().addListener((o, ov, nv) -> getSrc().setRootWord((nv == null) ? null : nv.getSrc()));
        namedTagProperty().addListener((o, ov, nv) -> getSrc().setNamedTag(nv));
        formTemplate.addListener((o, ov, nv) -> getSrc().setFormTemplate(nv));
        translationProperty().addListener((o, ov, nv) -> getSrc().setTranslation(nv));
        propertiesProperty().addListener((o, ov, nv) ->
                getSrc().setProperties((nv == null) ? null : (AbstractProperties) nv.getSrc()));

    }

    @Override
    protected void initValues(Location value) {
        super.initValues(value);
        setChapterNumber((value == null) ? 0 : value.getChapterNumber());
        setVerseNumber((value == null) ? 0 : value.getVerseNumber());
        setTokenNumber((value == null) ? 0 : value.getTokenNumber());
        setLocationNumber((value == null) ? 0 : value.getLocationNumber());
        setHidden((value == null) ? FALSE : value.isHidden());
        setStartIndex((value == null) ? 0 : value.getStartIndex());
        setEndIndex((value == null) ? 0 : value.getEndIndex());
        setPartOfSpeech((value == null) ? null : value.getPartOfSpeech());
        setNamedTag((value == null) ? null : value.getNamedTag());
        setFormTemplate((value == null) ? null : value.getFormTemplate());
        setTranslation((value == null) ? null : value.getTranslation());
        setRootWord(value);
        setProperties(value);
    }

    private void setProperties(Location value) {
        AbstractProperties props = (value == null) ? null : value.getProperties();
        AbstractPropertiesAdapter adapter = null;
        if (props != null) {
            if (isNoun(props)) {
                adapter = new NounPropertiesAdapter();
            } else if (isPronoun(props)) {
                adapter = new ProNounPropertiesAdapter();
            } else if (isVerb(props)) {
                adapter = new VerbPropertiesAdapter();
            } else {
                adapter = new ParticlePropertiesAdapter();
            }

            adapter.setSrc(props);
        }
        setProperties(adapter);
    }

    private void setRootWord(Location value) {
        RootWordAdapter rootWord = new RootWordAdapter();
        rootWord.setSrc((value == null) ? null : value.getRootWord());
        setRootWord(rootWord);
    }

    public final int getChapterNumber() {
        return chapterNumber.get();
    }

    public final void setChapterNumber(int chapterNumber) {
        this.chapterNumber.set(chapterNumber);
    }

    public final IntegerProperty chapterNumberProperty() {
        return chapterNumber;
    }

    public final int getVerseNumber() {
        return verseNumber.get();
    }

    public final void setVerseNumber(int verseNumber) {
        this.verseNumber.set(verseNumber);
    }

    public final IntegerProperty verseNumberProperty() {
        return verseNumber;
    }

    public final int getTokenNumber() {
        return tokenNumber.get();
    }

    public final void setTokenNumber(int tokenNumber) {
        this.tokenNumber.set(tokenNumber);
    }

    public final IntegerProperty tokenNumberProperty() {
        return tokenNumber;
    }

    public final int getLocationNumber() {
        return locationNumber.get();
    }

    public final void setLocationNumber(int locationNumber) {
        this.locationNumber.set(locationNumber);
    }

    public final IntegerProperty locationNumberProperty() {
        return locationNumber;
    }

    public final boolean getHidden() {
        return hidden.get();
    }

    public final void setHidden(boolean hidden) {
        this.hidden.set(hidden);
    }

    public final BooleanProperty hiddenProperty() {
        return hidden;
    }

    public final int getStartIndex() {
        return startIndex.get();
    }

    public final void setStartIndex(int startIndex) {
        this.startIndex.set(startIndex);
    }

    public final IntegerProperty startIndexProperty() {
        return startIndex;
    }

    public final int getEndIndex() {
        return endIndex.get();
    }

    public final void setEndIndex(int endIndex) {
        this.endIndex.set(endIndex);
    }

    public final IntegerProperty endIndexProperty() {
        return endIndex;
    }

    public final PartOfSpeech getPartOfSpeech() {
        return partOfSpeech.get();
    }

    public final void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech.set(partOfSpeech);
    }

    public final ObjectProperty<PartOfSpeech> partOfSpeechProperty() {
        return partOfSpeech;
    }

    public final RootWordAdapter getRootWord() {
        return rootWord.get();
    }

    public final void setRootWord(RootWordAdapter rootWord) {
        this.rootWord.set(rootWord);
    }

    public final ObjectProperty<RootWordAdapter> rootWordProperty() {
        return rootWord;
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

    public final NamedTemplate getFormTemplate() {
        return formTemplate.get();
    }

    public final void setFormTemplate(NamedTemplate formTemplate) {
        this.formTemplate.set(formTemplate);
    }

    public final ObjectProperty<NamedTemplate> formTemplateProperty() {
        return formTemplate;
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

    public final AbstractPropertiesAdapter getProperties() {
        return properties.get();
    }

    public final void setProperties(AbstractPropertiesAdapter properties) {
        this.properties.set(properties);
    }

    public final ObjectProperty<AbstractPropertiesAdapter> propertiesProperty() {
        return properties;
    }
}
