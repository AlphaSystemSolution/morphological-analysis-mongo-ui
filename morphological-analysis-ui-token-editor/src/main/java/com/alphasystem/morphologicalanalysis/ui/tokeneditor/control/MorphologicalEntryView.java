package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.MorphologicalEntrySkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

import static com.alphasystem.util.AppUtil.getResource;
import static javafx.collections.FXCollections.observableSet;

/**
 * @author sali
 */
@Component
public class MorphologicalEntryView extends Control {

    private final ObjectProperty<MorphologicalEntry> morphologicalEntry = new SimpleObjectProperty<>(null, "morphologicalEntry");
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>(null, "rootLetters");
    private final ObjectProperty<NamedTemplate> form = new SimpleObjectProperty<>(null, "form");
    private final StringProperty shortTranslation = new SimpleStringProperty(null, "shortTranslation");
    private final ObservableSet<VerbalNoun> verbalNouns = observableSet();
    private final BooleanProperty removePassiveLine = new SimpleBooleanProperty(false, "removePassiveLine");
    private final BooleanProperty skipRuleProcessing = new SimpleBooleanProperty(false, "skipRuleProcessing");

    @PostConstruct
    void postConstruct() {
        setSkin(createDefaultSkin());
        morphologicalEntryProperty().addListener((o, ov, nv) -> setValues(nv));
        rootLettersProperty().addListener((o, ov, nv) -> updateMorphologicalEntry(nv));
        formProperty().addListener((o, ov, nv) -> updateMorphologicalEntry(nv));
        shortTranslationProperty().addListener((o, ov, nv) -> updateMorphologicalEntry(nv));
        removePassiveLineProperty().addListener((o, ov, nv) -> updateMorphologicalEntry(nv, getSkipRuleProcessing()));
        skipRuleProcessingProperty().addListener((o, ov, nv) -> updateMorphologicalEntry(getRemovePassiveLine(), nv));
        getVerbalNouns().addListener((SetChangeListener<? super VerbalNoun>) this::updateMorphologicalEntry);
    }

    private void setValues(MorphologicalEntry morphologicalEntry) {
        if (morphologicalEntry != null) {
            RootLetters rootLetters = morphologicalEntry.getRootLetters();
            setRootLetters(rootLetters);

            Set<VerbalNoun> verbalNouns = morphologicalEntry.getVerbalNouns();
            this.verbalNouns.clear();
            this.verbalNouns.addAll(verbalNouns);

            setForm(morphologicalEntry.getForm());

            // update verbal nouns and noun of place and time
            ((MorphologicalEntrySkin) getSkin()).updateVerbalNouns();

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

    private void updateMorphologicalEntry(RootLetters rootLetters) {
        final MorphologicalEntry morphologicalEntry = getMorphologicalEntry();
        if (morphologicalEntry != null) {
            morphologicalEntry.setRootLetters(rootLetters);
        }
    }

    private void updateMorphologicalEntry(NamedTemplate namedTemplate) {
        final MorphologicalEntry morphologicalEntry = getMorphologicalEntry();
        if (morphologicalEntry != null) {
            morphologicalEntry.setForm(namedTemplate);
        }
    }

    private void updateMorphologicalEntry(String translation) {
        final MorphologicalEntry morphologicalEntry = getMorphologicalEntry();
        if (morphologicalEntry != null) {
            morphologicalEntry.setShortTranslation(translation);
        }
    }

    private void updateMorphologicalEntry(Boolean removePassiveLIne, Boolean skipRuleProcessing) {
        final MorphologicalEntry morphologicalEntry = getMorphologicalEntry();
        if (morphologicalEntry != null) {
            ConjugationConfiguration configuration = morphologicalEntry.getConfiguration();
            if (configuration == null) {
                configuration = new ConjugationConfiguration();
            }
            configuration.setRemovePassiveLine(removePassiveLIne);
            configuration.setSkipRuleProcessing(skipRuleProcessing);
            morphologicalEntry.setConfiguration(configuration);
        }
    }

    private void updateMorphologicalEntry(SetChangeListener.Change<? extends VerbalNoun> c) {
        final MorphologicalEntry morphologicalEntry = getMorphologicalEntry();
        if (morphologicalEntry != null) {
            if (c.wasRemoved()) {
                VerbalNoun elementRemoved = c.getElementRemoved();
                morphologicalEntry.getVerbalNouns().remove(elementRemoved);
            }
            if (c.wasAdded()) {
                VerbalNoun elementAdded = c.getElementAdded();
                morphologicalEntry.getVerbalNouns().add(elementAdded);
            }
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
