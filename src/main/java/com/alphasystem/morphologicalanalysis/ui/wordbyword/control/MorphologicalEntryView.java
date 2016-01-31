package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.MorphologicalEntrySkin;
import javafx.beans.property.*;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.Set;

import static com.alphasystem.util.AppUtil.getResource;
import static javafx.collections.FXCollections.observableSet;

/**
 * @author sali
 */
public class MorphologicalEntryView extends Control {

    private final ObjectProperty<MorphologicalEntry> morphologicalEntry = new SimpleObjectProperty<>(null, "morphologicalEntry");
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>(null, "rootLetters");
    private final ObjectProperty<NamedTemplate> form = new SimpleObjectProperty<>(null, "form");
    private final StringProperty shortTranslation = new SimpleStringProperty(null, "shortTranslation");
    private final ObservableSet<VerbalNoun> verbalNouns = observableSet();
    private final ObservableSet<NounOfPlaceAndTime> nounOfPlaceAndTimes = observableSet();
    private final BooleanProperty removePassiveLine = new SimpleBooleanProperty(false, "removePassiveLine");
    private final BooleanProperty skipRuleProcessing = new SimpleBooleanProperty(false, "skipRuleProcessing");

    public MorphologicalEntryView() {
        morphologicalEntryProperty().addListener((o, ov, nv) -> setValues(nv));
        rootLettersProperty().addListener((o, ov, nv) -> getMorphologicalEntry().setRootLetters(nv));
        formProperty().addListener((o, ov, nv) -> getMorphologicalEntry().setForm(nv));
        shortTranslationProperty().addListener((o, ov, nv) -> getMorphologicalEntry().setShortTranslation(nv));
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
        getVerbalNouns().addListener((SetChangeListener<? super VerbalNoun>) c -> {
            if (c.wasRemoved()) {
                VerbalNoun elementRemoved = (VerbalNoun) c.getElementRemoved();
                getMorphologicalEntry().getVerbalNouns().remove(elementRemoved);
            }
            if (c.wasAdded()) {
                VerbalNoun elementAdded = (VerbalNoun) c.getElementAdded();
                getMorphologicalEntry().getVerbalNouns().add(elementAdded);
            }
        });
        getNounOfPlaceAndTimes().addListener((SetChangeListener<? super NounOfPlaceAndTime>) c -> {
            if (c.wasRemoved()) {
                NounOfPlaceAndTime elementRemoved = (NounOfPlaceAndTime) c.getElementRemoved();
                getMorphologicalEntry().getNounOfPlaceAndTimes().remove(elementRemoved);
            }
            if (c.wasAdded()) {
                NounOfPlaceAndTime elementAdded = (NounOfPlaceAndTime) c.getElementAdded();
                getMorphologicalEntry().getNounOfPlaceAndTimes().add(elementAdded);
            }
        });
    }

    private void setValues(MorphologicalEntry morphologicalEntry) {
        if (morphologicalEntry != null) {
            RootLetters rootLetters = morphologicalEntry.getRootLetters();
            setRootLetters(rootLetters);

            Set<VerbalNoun> verbalNouns = morphologicalEntry.getVerbalNouns();
            this.verbalNouns.clear();
            this.verbalNouns.addAll(verbalNouns);

            Set<NounOfPlaceAndTime> nounOfPlaceAndTimes = morphologicalEntry.getNounOfPlaceAndTimes();
            this.nounOfPlaceAndTimes.clear();
            this.nounOfPlaceAndTimes.addAll(nounOfPlaceAndTimes);

            setForm(morphologicalEntry.getForm());

            // update verbal nouns and noun of place and time
            ((MorphologicalEntrySkin) getSkin()).updateVerbalNounsAndNounOfPlaceTimes();

            setShortTranslation(morphologicalEntry.getShortTranslation());
            ConjugationConfiguration configuration = morphologicalEntry.getConfiguration();
            boolean removePassiveLine = false;
            boolean skipRuleProcessing = false;
            if (configuration != null) {
                removePassiveLine = configuration.isRemovePassiveLine();
                skipRuleProcessing = configuration.isSkipRuleProcessing();
            }
            setRemovePassiveLine(removePassiveLine);
            setSkipRuleProcessing(skipRuleProcessing);
        }
    }

    @Override
    public String getUserAgentStylesheet() {
        return getResource("styles/application.css").toExternalForm();
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

    public final String getShortTranslation() {
        return shortTranslation.get();
    }

    public final void setShortTranslation(String shortTranslation) {
        this.shortTranslation.set(shortTranslation);
    }

    public final StringProperty shortTranslationProperty() {
        return shortTranslation;
    }

    public final ObservableSet<VerbalNoun> getVerbalNouns() {
        return verbalNouns;
    }

    public final ObservableSet<NounOfPlaceAndTime> getNounOfPlaceAndTimes() {
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
        MorphologicalEntrySkin skin = new MorphologicalEntrySkin(this);
        setSkin(skin);
        return skin;
    }
}
