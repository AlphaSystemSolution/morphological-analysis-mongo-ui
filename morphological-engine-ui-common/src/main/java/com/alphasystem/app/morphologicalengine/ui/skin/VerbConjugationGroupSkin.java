package com.alphasystem.app.morphologicalengine.ui.skin;

import com.alphasystem.app.morphologicalengine.conjugation.model.ConjugationTuple;
import com.alphasystem.app.morphologicalengine.conjugation.model.VerbConjugationGroup;
import com.alphasystem.app.morphologicalengine.ui.VerbConjugationGroupView;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SarfTermType;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * @author sali
 */
public class VerbConjugationGroupSkin extends ConjugationGroupSkin<VerbConjugationGroup, VerbConjugationGroupView> {

    @FXML protected ArabicLabelView row31;
    @FXML protected ArabicLabelView row32;
    @FXML protected ArabicLabelView row33;
    @FXML protected ArabicLabelView row41;
    @FXML protected ArabicLabelView row42;
    @FXML protected ArabicLabelView row43;
    @FXML protected ArabicLabelView row51;
    @FXML protected ArabicLabelView row52;

    public VerbConjugationGroupSkin(VerbConjugationGroupView control) {
        super(control);
    }

    @Override
    protected void setupRows(VerbConjugationGroup group) {
        final SarfTermType termType = (group == null) ? null : group.getTermType();
        final boolean imperativeOrForbidden = SarfTermType.IMPERATIVE.equals(termType) ||
                SarfTermType.FORBIDDING.equals(termType);
        if (imperativeOrForbidden) {
            final GridPane gridPane = (GridPane) getChildren().get(0);
            gridPane.getChildren().removeAll(row31, row32, row33, row41, row42, row43, row51, row52);
            setupRow(group.getMasculineSecondPerson(), row11, row12, row13);
            setupRow(group.getFeminineSecondPerson(), row21, row22, row23);
        } else {
            setupRow((group == null) ? null : group.getMasculineThirdPerson(), row11, row12, row13);
            setupRow((group == null) ? null : group.getFeminineThirdPerson(), row21, row22, row23);
            setupRow((group == null) ? null : group.getMasculineSecondPerson(), row31, row32, row33);
            setupRow((group == null) ? null : group.getFeminineSecondPerson(), row41, row42, row43);
            setupRow5((group == null) ? null : group.getFirstPerson());
        }

    }

    private void setupRow5(ConjugationTuple tuple) {
        row51.setLabel((tuple == null) ? null : tuple.getSingular());
        row52.setLabel((tuple == null) ? null : tuple.getPlural());
    }

    @Override
    protected void setDisableStroke(Paint disableStroke) {
        super.setDisableStroke(disableStroke);
        row31.setDisabledStroke(disableStroke);
        row32.setDisabledStroke(disableStroke);
        row33.setDisabledStroke(disableStroke);
    }

    @Override
    protected void setFont(Font font) {
        super.setFont(font);
        row31.setFont(font);
        row32.setFont(font);
        row33.setFont(font);
        row41.setFont(font);
        row42.setFont(font);
        row43.setFont(font);
        row51.setFont(font);
        row52.setFont(font);
    }
}
