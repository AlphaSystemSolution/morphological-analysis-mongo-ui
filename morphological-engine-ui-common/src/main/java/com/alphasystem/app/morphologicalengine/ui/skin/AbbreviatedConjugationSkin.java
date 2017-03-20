package com.alphasystem.app.morphologicalengine.ui.skin;

import com.alphasystem.app.morphologicalengine.ui.AbbreviatedConjugationView;
import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.morphologicalengine.model.AbbreviatedConjugation;
import com.alphasystem.morphologicalengine.model.ConjugationHeader;
import com.alphasystem.morphologicalengine.model.abbrvconj.ActiveLine;
import com.alphasystem.morphologicalengine.model.abbrvconj.AdverbLine;
import com.alphasystem.morphologicalengine.model.abbrvconj.ImperativeAndForbiddingLine;
import com.alphasystem.morphologicalengine.model.abbrvconj.PassiveLine;
import com.alphasystem.util.GenericPreferences;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import static com.alphasystem.arabic.model.ArabicLetterType.NEW_LINE;
import static com.alphasystem.arabic.model.ArabicLetters.WORD_SPACE;
import static com.alphasystem.arabic.model.ArabicWord.concatenate;
import static com.alphasystem.arabic.model.ArabicWord.fromUnicode;
import static com.alphasystem.arabic.model.ArabicWord.getWord;
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
                final ActiveLine activeLine = abbreviatedConjugation.getActiveLine();
                if (activeLine != null) {
                    addActiveLine(activeLine, row);
                    row++;
                }
                final PassiveLine passiveLine = abbreviatedConjugation.getPassiveLine();
                if (passiveLine != null) {
                    addPassiveLine(passiveLine, row);
                    row++;
                }
                final ImperativeAndForbiddingLine imperativeAndForbiddingLine = abbreviatedConjugation.getImperativeAndForbiddingLine();
                if (imperativeAndForbiddingLine != null) {
                    addImperativeAndForbiddenLine(imperativeAndForbiddingLine, row);
                    row++;
                }
                final AdverbLine adverbLine = abbreviatedConjugation.getAdverbLine();
                if (adverbLine != null) {
                    addAdverbLine(adverbLine, row);
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

        private void addActiveLine(ActiveLine activeLine, int row) {
            pane.add(createLabel(fromUnicode(activeLine.getPastTense())), 0, row);
            pane.add(createLabel(fromUnicode(activeLine.getPresentTense())), 1, row);
            pane.add(createLabel(fromUnicode(activeLine.getVerbalNoun())), 3, row);
            pane.add(createLabel(fromUnicode(activeLine.getActiveParticipleValue())), 4, row);
        }

        private void addPassiveLine(PassiveLine passiveLine, int row) {
            pane.add(createLabel(fromUnicode(passiveLine.getPastPassiveTense())), 0, row);
            pane.add(createLabel(fromUnicode(passiveLine.getPresentPassiveTense())), 1, row);
            pane.add(createLabel(fromUnicode(passiveLine.getVerbalNoun())), 3, row);
            pane.add(createLabel(fromUnicode(passiveLine.getPassiveParticipleValue())), 4, row);
        }

        private void addImperativeAndForbiddenLine(ImperativeAndForbiddingLine imperativeAndForbiddingLine, int row) {
            pane.add(createLabel(fromUnicode(imperativeAndForbiddingLine.getImperativeWithPrefix()), WIDTH * 2), 0, row, 2, 1);
            pane.add(createLabel(fromUnicode(imperativeAndForbiddingLine.getForbiddingWithPrefix()), WIDTH * 2), 3, row, 2, 1);
        }

        private void addAdverbLine(AdverbLine adverbLine, int row) {
            pane.add(createLabel(fromUnicode(adverbLine.getAdverb()), TOTAL_WIDTH), 0, row, NUM_OF_COLUMNS_IN_DETAIL_MODE, 1);
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
