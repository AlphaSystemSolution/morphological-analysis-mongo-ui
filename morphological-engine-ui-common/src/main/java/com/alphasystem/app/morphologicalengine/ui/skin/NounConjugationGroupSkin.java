package com.alphasystem.app.morphologicalengine.ui.skin;

import com.alphasystem.app.morphologicalengine.conjugation.model.NounConjugationGroup;
import com.alphasystem.app.morphologicalengine.ui.NounConjugationGroupView;
import com.alphasystem.arabic.ui.ArabicLabelView;
import javafx.fxml.FXML;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * @author sali
 */
public class NounConjugationGroupSkin extends ConjugationGroupSkin<NounConjugationGroup, NounConjugationGroupView> {

    @FXML protected ArabicLabelView row31;
    @FXML protected ArabicLabelView row32;
    @FXML protected ArabicLabelView row33;

    public NounConjugationGroupSkin(NounConjugationGroupView control) {
        super(control);
    }

    @Override
    protected void setupRows(NounConjugationGroup group) {
        setupRow((group == null) ? null : group.getNominative(), row11, row12, row13);
        setupRow((group == null) ? null : group.getAccusative(), row21, row22, row23);
        setupRow((group == null) ? null : group.getGenitive(), row31, row32, row33);
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
    }
}
