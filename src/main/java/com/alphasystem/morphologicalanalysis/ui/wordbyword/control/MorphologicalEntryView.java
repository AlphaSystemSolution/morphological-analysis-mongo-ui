package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.MorphologicalEntrySkin;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class MorphologicalEntryView extends Control {

    private final ObjectProperty<MorphologicalEntry> morphologicalEntry = new SimpleObjectProperty<>(null, "morphologicalEntry");
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>(null, "rootLetters");
    private final ObjectProperty<NamedTemplate> form = new SimpleObjectProperty<>(null, "form");
    private final StringProperty translation = new SimpleStringProperty(null, "translation");
    private final ObservableList<VerbalNoun> verbalNouns = observableArrayList();
    private final ObservableList<NounOfPlaceAndTime> nounOfPlaceAndTimes = observableArrayList();
    private final BooleanProperty removePassiveLine = new SimpleBooleanProperty(false, "removePassiveLine");
    private final BooleanProperty skipRuleProcessing = new SimpleBooleanProperty(false, "skipRuleProcessing");

    public MorphologicalEntryView() {
        morphologicalEntryProperty().addListener((o, ov, nv) -> setValues(nv));
        rootLettersProperty().addListener((o, ov, nv) -> getMorphologicalEntry().setRootLetters(nv));
        formProperty().addListener((o, ov, nv) -> getMorphologicalEntry().setForm(nv));
        translationProperty().addListener((o, ov, nv) -> getMorphologicalEntry().setTranslation(nv));
        removePassiveLineProperty().addListener((o, ov, nv) -> {
            ConjugationConfiguration configuration = getMorphologicalEntry().getConfiguration();
            if (configuration == null) {
                configuration = new ConjugationConfiguration();
            }
            configuration.setRemovePassiveLine(nv);
            getMorphologicalEntry().setConfiguration(configuration);
        });
        skipRuleProcessingProperty().addListener((o, ov, nv) -> {
            ConjugationConfiguration configuration = getMorphologicalEntry().getConfiguration();
            if (configuration == null) {
                configuration = new ConjugationConfiguration();
            }
            configuration.setSkipRuleProcessing(nv);
            getMorphologicalEntry().setConfiguration(configuration);
        });
    }

    private void setValues(MorphologicalEntry morphologicalEntry) {
        if (morphologicalEntry != null) {
            setRootLetters(morphologicalEntry.getRootLetters());
            setForm(morphologicalEntry.getForm());
            setTranslation(morphologicalEntry.getTranslation());
            getVerbalNouns().addAll(morphologicalEntry.getVerbalNouns());
            getNounOfPlaceAndTimes().addAll(morphologicalEntry.getNounOfPlaceAndTimes());
            ConjugationConfiguration configuration = morphologicalEntry.getConfiguration();
            if (configuration != null) {
                setRemovePassiveLine(configuration.isRemovePassiveLine());
                setSkipRuleProcessing(configuration.isSkipRuleProcessing());
            }
        }
    }

    public final MorphologicalEntry getMorphologicalEntry() {
        return morphologicalEntry.get();
    }

    public final void setMorphologicalEntry(MorphologicalEntry morphologicalEntry) {
        this.morphologicalEntry.set(morphologicalEntry);
    }

    public final ObjectProperty<MorphologicalEntry> morphologicalEntryProperty() {
        return morphologicalEntry;
    }

    public final RootLetters getRootLetters() {
        return rootLetters.get();
    }

    public final void setRootLetters(RootLetters rootLetters) {
        this.rootLetters.set(rootLetters);
    }

    public final ObjectProperty<RootLetters> rootLettersProperty() {
        return rootLetters;
    }

    public final NamedTemplate getForm() {
        return form.get();
    }

    public final void setForm(NamedTemplate form) {
        this.form.set(form);
    }

    public final ObjectProperty<NamedTemplate> formProperty() {
        return form;
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

    public final ObservableList<VerbalNoun> getVerbalNouns() {
        return verbalNouns;
    }

    public final ObservableList<NounOfPlaceAndTime> getNounOfPlaceAndTimes() {
        return nounOfPlaceAndTimes;
    }

    public final boolean getRemovePassiveLine() {
        return removePassiveLine.get();
    }

    public final void setRemovePassiveLine(boolean removePassiveLine) {
        this.removePassiveLine.set(removePassiveLine);
    }

    public final BooleanProperty removePassiveLineProperty() {
        return removePassiveLine;
    }

    public final boolean getSkipRuleProcessing() {
        return skipRuleProcessing.get();
    }

    public final void setSkipRuleProcessing(boolean skipRuleProcessing) {
        this.skipRuleProcessing.set(skipRuleProcessing);
    }

    public final BooleanProperty skipRuleProcessingProperty() {
        return skipRuleProcessing;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MorphologicalEntrySkin(this);
    }
}
