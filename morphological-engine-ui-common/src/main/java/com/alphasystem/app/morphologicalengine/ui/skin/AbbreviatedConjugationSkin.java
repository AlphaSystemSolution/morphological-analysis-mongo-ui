package com.alphasystem.app.morphologicalengine.ui.skin;

import com.alphasystem.app.morphologicalengine.ui.AbbreviatedConjugationView;
import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.arabic.model.ArabicLetters;
import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.morphologicalengine.model.AbbreviatedConjugation;
import com.alphasystem.morphologicalengine.model.AbbreviatedRecord;
import com.alphasystem.morphologicalengine.model.ConjugationHeader;
import com.alphasystem.util.GenericPreferences;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.alphasystem.arabic.model.ArabicLetterType.*;
import static com.alphasystem.arabic.model.ArabicLetters.WORD_SPACE;
import static com.alphasystem.arabic.model.ArabicWord.*;
import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.scene.paint.Color.DODGERBLUE;
import static javafx.scene.paint.Color.TRANSPARENT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author sali
 */
public class AbbreviatedConjugationSkin extends SkinBase<AbbreviatedConjugationView> {

    private static final int NUM_OF_COLUMNS_IN_DETAIL_MODE = 6;
    private static final int WIDTH_IN_DETAIL_MODE = 128;
    private static final int SPACING = 12;
    private static final int NUM_OF_COLUMNS = 4;
    private static final int TOTAL_WIDTH = (WIDTH_IN_DETAIL_MODE * NUM_OF_COLUMNS_IN_DETAIL_MODE) + SPACING;
    private static final int WIDTH = TOTAL_WIDTH / NUM_OF_COLUMNS;
    private static final int HEIGHT = 64;
    private static final ArabicWord PARTICIPLE_PREFIX = getWord(FA, HA, WAW);
    private static final ArabicWord IMPERATIVE_PREFIX = getWord(ALIF, LAM, ALIF_HAMZA_ABOVE, MEEM, RA, SPACE, MEEM, NOON, HA);
    private static final ArabicWord FORBIDDING_PREFIX = getWord(WAW, NOON, HA, YA, SPACE, AIN, NOON, HA);
    private static final ArabicWord ADVERB_PREFIX = getWord(WAW, ALIF, LAM, DTHA, RA, FA, SPACE, MEEM, NOON, HA);

    private final MorphologicalEnginePreferences preferences;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public AbbreviatedConjugationSkin(AbbreviatedConjugationView control) {
        super(control);
        this.preferences = GenericPreferences.getInstance(MorphologicalEnginePreferences.class);
        getChildren().setAll(new SkinView(control));
    }

    private class SkinView extends BorderPane {

        private final AbbreviatedConjugationView control;
        private final GridPane pane = new GridPane();

        private SkinView(AbbreviatedConjugationView control) {
            this.control = control;
            pane.setAlignment(Pos.BASELINE_CENTER);
            pane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            initialize();
            setCenter(pane);
        }

        private void initialize() {
            control.conjugationHeaderProperty().addListener((observable, oldValue, newValue) ->
                    setup(newValue, control.getAbbreviatedConjugation()));
            control.abbreviatedConjugationProperty().addListener((observable, oldValue, newValue) ->
                    setup(control.getConjugationHeader(), newValue));
            setup(control.getConjugationHeader(), control.getAbbreviatedConjugation());
        }

        private void setup(ConjugationHeader conjugationHeader, AbbreviatedConjugation abbreviatedConjugation) {
            pane.getChildren().remove(0, pane.getChildren().size());
            int row = 0;

            if (conjugationHeader != null) {
                pane.add(createTitle(conjugationHeader), 0, row, NUM_OF_COLUMNS_IN_DETAIL_MODE, 2);
                row += 2;
                pane.add(createConjugationTypeDetails(conjugationHeader), 0, row, 2, 2);
                pane.add(createTranslation(conjugationHeader), 3, row, 2, 2);
                row += 2;
            }

            if (abbreviatedConjugation != null) {
                addActiveOrPassiveLine(abbreviatedConjugation.getPastTense(), abbreviatedConjugation.getPresentTense(),
                        abbreviatedConjugation.getVerbalNouns(), abbreviatedConjugation.getActiveParticipleMasculine(), row);
                row++;
                if (abbreviatedConjugation.hasPassiveLine()) {
                    addActiveOrPassiveLine(abbreviatedConjugation.getPastPassiveTense(), abbreviatedConjugation.getPresentPassiveTense(),
                            abbreviatedConjugation.getVerbalNouns(), abbreviatedConjugation.getPassiveParticipleMasculine(), row);
                    row++;
                }
                addImperativeAndForbiddenLine(abbreviatedConjugation.getImperative(), abbreviatedConjugation.getForbidding(), row);
                row++;
                final AbbreviatedRecord[] adverbs = abbreviatedConjugation.getAdverbs();
                if(!ArrayUtils.isEmpty(adverbs)){
                    addAdverbLine(adverbs, row);
                }
            }
        }

        private ArabicLabelView createTitle(ConjugationHeader conjugationHeader) {
            ArabicLabelView labelView = new ArabicLabelView();
            labelView.setLabel(fromUnicode(conjugationHeader.getTitle()));
            labelView.setDisabledStroke(TRANSPARENT);
            labelView.setFont(preferences.getArabicHeadingFont());
            labelView.setDisable(true);
            labelView.setStroke(DODGERBLUE);
            labelView.setHeight(HEIGHT);
            labelView.setWidth(TOTAL_WIDTH);
            return labelView;
        }

        private ArabicLabelView createTranslation(ConjugationHeader conjugationHeader) {
            ArabicLabelView labelView = new ArabicLabelView();
            labelView.setFont(preferences.getEnglishFont());
            labelView.setWidth(WIDTH * 2);
            labelView.setHeight(HEIGHT * 2);
            labelView.setDisable(true);
            String translation = conjugationHeader.getTranslation();
            if (isNotBlank(translation)) {
                labelView.setText(translation);
            }
            return labelView;
        }

        private ArabicLabelView createConjugationTypeDetails(ConjugationHeader conjugationHeader) {
            ArabicLabelView labelView = new ArabicLabelView();
            labelView.setFont(preferences.getArabicFont());
            ArabicWord label = concatenate(WORD_SPACE, fromUnicode(conjugationHeader.getTypeLabel1()),
                    getWord(NEW_LINE), WORD_SPACE, fromUnicode(conjugationHeader.getTypeLabel2()),
                    getWord(NEW_LINE), WORD_SPACE, fromUnicode(conjugationHeader.getTypeLabel3()));
            labelView.setLabel(label);
            labelView.setAlignment(CENTER_LEFT);
            labelView.setWidth(WIDTH * 2);
            labelView.setHeight(HEIGHT * 2);
            labelView.setDisable(true);
            return labelView;
        }

        private void addActiveOrPassiveLine(AbbreviatedRecord pastTense, AbbreviatedRecord presentTense,
                                            AbbreviatedRecord[] verbalNouns, AbbreviatedRecord participleMasculine, int row) {
            pane.add(createLabel(toArabicWord(pastTense)), 0, row);
            pane.add(createLabel(toArabicWord(presentTense)), 1, row);
            pane.add(createLabel(toArabicWord(verbalNouns, null)), 3, row);
            pane.add(createLabel(toArabicWord(participleMasculine, PARTICIPLE_PREFIX)), 4, row);
        }

        private void addImperativeAndForbiddenLine(AbbreviatedRecord imperative, AbbreviatedRecord forbidding, int row) {
            pane.add(createLabel(toArabicWord(imperative, IMPERATIVE_PREFIX), WIDTH * 2), 0, row, 2, 1);
            pane.add(createLabel(toArabicWord(forbidding, FORBIDDING_PREFIX), WIDTH * 2), 3, row, 2, 1);
        }

        private void addAdverbLine(AbbreviatedRecord[] adverbs, int row) {
            pane.add(createLabel(toArabicWord(adverbs, ADVERB_PREFIX), TOTAL_WIDTH), 0, row, NUM_OF_COLUMNS_IN_DETAIL_MODE, 1);
        }

        private ArabicWord toArabicWord(AbbreviatedRecord record, ArabicWord prefix) {
            if (record == null) {
                return ArabicLetters.WORD_SPACE;
            }
            ArabicWord arabicWord = ArabicWord.fromUnicode(record.getLabel());
            if (prefix != null) {
                arabicWord = ArabicWord.concatenateWithSpace(prefix, arabicWord);
            }
            return arabicWord;
        }

        private ArabicWord toArabicWord(AbbreviatedRecord record) {
            return toArabicWord(record, null);
        }

        private ArabicWord toArabicWord(AbbreviatedRecord[] values, ArabicWord prefix) {
            if (ArrayUtils.isEmpty(values)) {
                return ArabicLetters.WORD_SPACE;
            }
            List<ArabicWord> arabicWords = new ArrayList<>();
            Arrays.stream(values).forEach(record -> arabicWords.add(toArabicWord(record)));
            ArabicWord arabicWord = ArabicWord.concatenateWithAnd(arabicWords.toArray(new ArabicWord[0]));
            if (prefix != null) {
                arabicWord = ArabicWord.concatenateWithSpace(prefix, arabicWord);
            }
            return arabicWord;
        }

        private ArabicLabelView createLabel(ArabicSupport word) {
            return createLabel(word, WIDTH);
        }

        private ArabicLabelView createLabel(ArabicSupport word, int width) {
            ArabicLabelView label = new ArabicLabelView();
            label.setWidth(width);
            label.setHeight(HEIGHT);
            label.setDisable(true);
            label.setFont(preferences.getArabicFont());
            label.setLabel(word);
            return label;
        }

    }
}
