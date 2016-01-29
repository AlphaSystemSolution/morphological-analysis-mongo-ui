package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.arabic.ui.NounOfPlaceTimesPicker;
import com.alphasystem.arabic.ui.RootLettersPicker;
import com.alphasystem.arabic.ui.VerbalNounsPicker;
import com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.MorphologicalEntryView;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Collection;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.GAP;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;

/**
 * @author sali
 */
public class MorphologicalEntrySkin extends SkinBase<MorphologicalEntryView> {

    private final VerbalNounsPicker verbalNounsPicker = new VerbalNounsPicker();
    private final NounOfPlaceTimesPicker nounOfPlaceTimesPicker = new NounOfPlaceTimesPicker();

    public MorphologicalEntrySkin(MorphologicalEntryView control) {
        super(control);
        initializeSkin();
    }

    @SuppressWarnings({"unchecked"})
    private void initializeSkin() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(GAP));

        MorphologicalEntryView control = getSkinnable();

        int row = 0;
        Label label = new Label(RESOURCE_BUNDLE.getString("rootLetters.label"));
        gridPane.add(label, 0, row);

        RootLettersPicker rootLettersPicker = new RootLettersPicker();
        label.setLabelFor(rootLettersPicker);
        rootLettersPicker.rootLettersProperty().bindBidirectional(control.rootLettersProperty());
        gridPane.add(rootLettersPicker, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("form.label"));
        gridPane.add(label, 0, row);

        ComboBox<NamedTemplate> namedTemplateComboBox = ComboBoxFactory.getNamedTemplateComboBox();
        label.setLabelFor(namedTemplateComboBox);
        namedTemplateComboBox.valueProperty().bindBidirectional(control.formProperty());
        gridPane.add(namedTemplateComboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("verbalNoun.label"));
        gridPane.add(label, 0, row);

        verbalNounsPicker.getValues().addAll(control.getVerbalNouns());
        verbalNounsPicker.getValues().addListener((ListChangeListener<? super VerbalNoun>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    control.getVerbalNouns().removeAll(c.getRemoved());
                }
                if (c.wasAdded()) {
                    control.getVerbalNouns().addAll((Collection<? extends VerbalNoun>) c.getAddedSubList());
                }
            }
        });
        label.setLabelFor(verbalNounsPicker);
        gridPane.add(verbalNounsPicker, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("nounOfPlaceAndTime.label"));
        gridPane.add(label, 0, row);

        nounOfPlaceTimesPicker.getValues().addAll(control.getNounOfPlaceAndTimes());
        nounOfPlaceTimesPicker.getValues().addListener((ListChangeListener<? super NounOfPlaceAndTime>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    control.getNounOfPlaceAndTimes().removeAll(c.getRemoved());
                }
                if (c.getAddedSize() > 0) {
                    control.getNounOfPlaceAndTimes().addAll((Collection<? extends NounOfPlaceAndTime>) c.getAddedSubList());
                }
            }
        });
        label.setLabelFor(nounOfPlaceTimesPicker);
        gridPane.add(nounOfPlaceTimesPicker, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("translation.label"));
        gridPane.add(label, 0, row);

        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(5);
        textArea.setPrefColumnCount(25);
        label.setLabelFor(textArea);
        textArea.textProperty().bindBidirectional(control.shortTranslationProperty());
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setContent(textArea);
        gridPane.add(scrollPane, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("removePassiveLine.label"));
        gridPane.add(label, 0, row);

        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().bindBidirectional(control.removePassiveLineProperty());
        label.setLabelFor(checkBox);
        gridPane.add(checkBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("skipRuleProcessing.label"));
        gridPane.add(label, 0, row);

        checkBox = new CheckBox();
        checkBox.selectedProperty().bindBidirectional(control.skipRuleProcessingProperty());
        label.setLabelFor(checkBox);
        gridPane.add(checkBox, 1, row);

        getChildren().add(gridPane);
    }

    public void updateVerbalNounsAndNounOfPlaceTimes() {
        updateVerbalNouns();
        updateNounOfPlaceTimes();
    }

    private void updateNounOfPlaceTimes() {
        MorphologicalEntryView control = getSkinnable();
        NamedTemplate template = control.getForm();
        nounOfPlaceTimesPicker.getValues().clear();
        ObservableSet<NounOfPlaceAndTime> nounOfPlaceAndTimesFromControl = control.getNounOfPlaceAndTimes();
        if (nounOfPlaceAndTimesFromControl != null && !nounOfPlaceAndTimesFromControl.isEmpty()) {
            nounOfPlaceTimesPicker.getValues().addAll(nounOfPlaceAndTimesFromControl);
        } else {
            nounOfPlaceTimesPicker.getValues().addAll(NounOfPlaceAndTime.getByTemplate(template));
        }
    }

    private void updateVerbalNouns() {
        MorphologicalEntryView control = getSkinnable();
        NamedTemplate template = control.getForm();
        verbalNounsPicker.getValues().clear();
        ObservableSet<VerbalNoun> verbalNounsFromControl = control.getVerbalNouns();
        if (verbalNounsFromControl != null && !verbalNounsFromControl.isEmpty()) {
            verbalNounsPicker.getValues().addAll(verbalNounsFromControl);
        } else {
            verbalNounsPicker.getValues().addAll(VerbalNoun.getByTemplate(template));
        }
    }
}
