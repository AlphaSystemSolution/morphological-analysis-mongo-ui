package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.arabic.ui.NounOfPlaceTimesPicker;
import com.alphasystem.arabic.ui.RootLettersPicker;
import com.alphasystem.arabic.ui.VerbalNounsPicker;
import com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.MorphologicalEntryView;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static java.lang.String.format;

/**
 * @author sali
 */
public class MorphologicalEntrySkin extends SkinBase<MorphologicalEntryView> {

    private final SkinView skinView;

    public MorphologicalEntrySkin(MorphologicalEntryView control) {
        super(control);
        skinView = new SkinView();
        getChildren().setAll(skinView);
    }

    public void updateVerbalNounsAndNounOfPlaceTimes() {
        updateVerbalNouns();
        updateNounOfPlaceTimes();
    }

    private void updateNounOfPlaceTimes() {
        MorphologicalEntryView control = getSkinnable();
        NamedTemplate template = control.getForm();
        skinView.nounOfPlaceTimesPicker.getValues().clear();
        ObservableSet<NounOfPlaceAndTime> nounOfPlaceAndTimesFromControl = control.getNounOfPlaceAndTimes();
        if (nounOfPlaceAndTimesFromControl != null && !nounOfPlaceAndTimesFromControl.isEmpty()) {
            skinView.nounOfPlaceTimesPicker.getValues().addAll(nounOfPlaceAndTimesFromControl);
        } else {
            skinView.nounOfPlaceTimesPicker.getValues().addAll(NounOfPlaceAndTime.getByTemplate(template));
        }
    }

    private void updateVerbalNouns() {
        MorphologicalEntryView control = getSkinnable();
        NamedTemplate template = control.getForm();
        skinView.verbalNounsPicker.getValues().clear();
        ObservableSet<VerbalNoun> verbalNounsFromControl = control.getVerbalNouns();
        if (verbalNounsFromControl != null && !verbalNounsFromControl.isEmpty()) {
            skinView.verbalNounsPicker.getValues().addAll(verbalNounsFromControl);
        } else {
            skinView.verbalNounsPicker.getValues().addAll(VerbalNoun.getByTemplate(template));
        }
    }

    private class SkinView extends BorderPane {

        @FXML
        private RootLettersPicker rootLettersPicker;

        @FXML
        private ComboBox<NamedTemplate> namedTemplateComboBox;

        @FXML
        private VerbalNounsPicker verbalNounsPicker;

        @FXML
        private NounOfPlaceTimesPicker nounOfPlaceTimesPicker;

        @FXML
        private TextArea translationField;

        @FXML
        private CheckBox removePassiveLine;

        @FXML
        private CheckBox skipRuleProcessing;

        private SkinView() {
            init();
        }

        private void init() {
            URL fxmlURL = getClass().getResource(format("/fxml/%s.fxml",
                    getSkinnable().getClass().getSimpleName()));
            try {
                loadFXML(this, fxmlURL, RESOURCE_BUNDLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
        void initialize() {
            final MorphologicalEntryView view = getSkinnable();
            rootLettersPicker.rootLettersProperty().bindBidirectional(view.rootLettersProperty());
            namedTemplateComboBox.valueProperty().bindBidirectional(view.formProperty());
            verbalNounsPicker.getValues().addAll(view.getVerbalNouns());
            verbalNounsPicker.getValues().addListener((ListChangeListener<? super VerbalNoun>) c -> {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        view.getVerbalNouns().removeAll(c.getRemoved());
                    }
                    if (c.wasAdded()) {
                        view.getVerbalNouns().addAll((Collection<? extends VerbalNoun>) c.getAddedSubList());
                    }
                }
            });
            nounOfPlaceTimesPicker.getValues().addAll(view.getNounOfPlaceAndTimes());
            nounOfPlaceTimesPicker.getValues().addListener((ListChangeListener<? super NounOfPlaceAndTime>) c -> {
                while (c.next()) {
                    if (c.wasRemoved()) {
                        view.getNounOfPlaceAndTimes().removeAll(c.getRemoved());
                    }
                    if (c.getAddedSize() > 0) {
                        view.getNounOfPlaceAndTimes().addAll((Collection<? extends NounOfPlaceAndTime>) c.getAddedSubList());
                    }
                }
            });
            translationField.textProperty().bindBidirectional(view.shortTranslationProperty());
            removePassiveLine.selectedProperty().bindBidirectional(view.removePassiveLineProperty());
            skipRuleProcessing.selectedProperty().bindBidirectional(view.skipRuleProcessingProperty());
        }
    }
}
