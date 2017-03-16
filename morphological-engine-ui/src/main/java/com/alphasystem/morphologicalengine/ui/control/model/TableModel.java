package com.alphasystem.morphologicalengine.ui.control.model;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationData;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;

import static com.alphasystem.arabic.model.NamedTemplate.FORM_I_CATEGORY_A_GROUP_U_TEMPLATE;
import static java.lang.Boolean.FALSE;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
``public final class TableModel implements Comparable<TableModel> {

    private final BooleanProperty checked = new SimpleBooleanProperty(FALSE, "checked");
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>(null, "rootLetters");
    private final ObjectProperty<NamedTemplate> template = new SimpleObjectProperty<>(null, "template", FORM_I_CATEGORY_A_GROUP_U_TEMPLATE);
    private final StringProperty translation = new SimpleStringProperty(null, "translation");
    private final ObservableList<VerbalNoun> verbalNouns = observableArrayList();
    private final BooleanProperty removePassiveLine = new SimpleBooleanProperty(FALSE, "removePassiveLine");
    private final BooleanProperty skipRuleProcessing = new SimpleBooleanProperty(FALSE, "skipRuleProcessing");
    private final BooleanProperty viewConjugation = new SimpleBooleanProperty(null, "viewConjugation", FALSE);
    private final ConjugationData conjugationData;

    public TableModel() {
        this(new ConjugationData());
    }

    /**
     * Copy constructor.
     *
     * @param src source model
     */
    public TableModel(TableModel src) {
        this(copy(src));
    }

    public TableModel(ConjugationData data) {
        this.conjugationData = (data == null) ? new ConjugationData() : data;
        setRootLetters(this.conjugationData.getRootLetters());
        setTemplate(this.conjugationData.getTemplate());
        setTranslation(this.conjugationData.getTranslation());
        getVerbalNouns().addAll(this.conjugationData.getVerbalNouns());
        ConjugationConfiguration configuration = this.conjugationData.getConfiguration();
        setRemovePassiveLine(configuration.isRemovePassiveLine());
        setSkipRuleProcessing(configuration.isSkipRuleProcessing());

        // add listeners to update conjugation data

        rootLettersProperty().addListener((o, ov, nv) -> conjugationData.setRootLetters(nv));
        templateProperty().addListener((o, ov, nv) -> conjugationData.setTemplate(nv));
        translationProperty().addListener((o, ov, nv) -> conjugationData.setTranslation(nv));
        verbalNouns.addListener((ListChangeListener<VerbalNoun>) c -> {
            while (c.next()) {
                List<VerbalNoun> verbalNouns = conjugationData.getVerbalNouns();
                verbalNouns.clear();
                verbalNouns.addAll(c.getAddedSubList());
            }
        });
        removePassiveLineProperty().addListener((o, ov, nv) -> conjugationData.getConfiguration().setRemovePassiveLine(nv));
        skipRuleProcessingProperty().addListener((o, ov, nv) -> conjugationData.getConfiguration().setSkipRuleProcessing(nv));
    }

    private static ConjugationData copy(TableModel src) {
        ConjugationData conjugationData = new ConjugationData();
        conjugationData.setRootLetters(src.getRootLetters());
        return conjugationData;
    }

    public final ConjugationData getConjugationData() {
        return conjugationData;
    }

    public final boolean isChecked() {
        return checked.get();
    }

    public final void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public final BooleanProperty checkedProperty() {
        return checked;
    }

    public final RootLetters getRootLetters() {
        return rootLetters.get();
    }

    public final void setRootLetters(RootLetters rootLetters) {
        this.rootLetters.set(rootLetters);
    }

    private ObjectProperty<RootLetters> rootLettersProperty() {
        return rootLetters;
    }

    public final NamedTemplate getTemplate() {
        return template.get();
    }

    public final void setTemplate(NamedTemplate template) {
        this.template.set(template);
    }

    private ObjectProperty<NamedTemplate> templateProperty() {
        return template;
    }

    public final String getTranslation() {
        return translation.get();
    }

    public final void setTranslation(String translation) {
        this.translation.set(translation);
    }

    private StringProperty translationProperty() {
        return translation;
    }

    public final ObservableList<VerbalNoun> getVerbalNouns() {
        return verbalNouns;
    }

    public final boolean isRemovePassiveLine() {
        return removePassiveLine.get();
    }

    private void setRemovePassiveLine(boolean removePassiveLine) {
        this.removePassiveLine.set(removePassiveLine);
    }

    public final BooleanProperty removePassiveLineProperty() {
        return removePassiveLine;
    }

    public final boolean isSkipRuleProcessing() {
        return skipRuleProcessing.get();
    }

    private void setSkipRuleProcessing(boolean skipRuleProcessing) {
        this.skipRuleProcessing.set(skipRuleProcessing);
    }

    public final BooleanProperty skipRuleProcessingProperty() {
        return skipRuleProcessing;
    }

    public final BooleanProperty viewConjugationProperty() {
        return viewConjugation;
    }

    @Override
    public int compareTo(TableModel o) {
        return getConjugationData().compareTo(o.getConjugationData());
    }
}
