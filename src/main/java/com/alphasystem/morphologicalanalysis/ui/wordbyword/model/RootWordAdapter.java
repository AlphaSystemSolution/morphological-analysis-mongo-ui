package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.morphologicalanalysis.ui.common.AbstractDocumentAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RootWord;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public final class RootWordAdapter extends AbstractDocumentAdapter<RootWord> {

    private final ObjectProperty<ArabicLetterType> firstRadical = new SimpleObjectProperty<>(null, "firstRadical");
    private final ObjectProperty<ArabicLetterType> secondRadical = new SimpleObjectProperty<>(null, "secondRadical");
    private final ObjectProperty<ArabicLetterType> thirdRadical = new SimpleObjectProperty<>(null, "thirdRadical");
    private final ObjectProperty<ArabicLetterType> fourthRadical = new SimpleObjectProperty<>(null, "fourthRadical");

    public RootWordAdapter() {
        super();
        firstRadicalProperty().addListener((o, ov, nv) -> getSrc().setFirstRadical(nv));
        secondRadicalProperty().addListener((o, ov, nv) -> getSrc().setSecondRadical(nv));
        thirdRadicalProperty().addListener((o, ov, nv) -> getSrc().setThirdRadical(nv));
        fourthRadicalProperty().addListener((o, ov, nv) -> getSrc().setFourthRadical(nv));
    }

    @Override
    protected void initValues(RootWord value) {
        super.initValues(value);
        setFirstRadical((value == null) ? null : value.getFirstRadical());
        setSecondRadical((value == null) ? null : value.getSecondRadical());
        setThirdRadical((value == null) ? null : value.getThirdRadical());
        setFourthRadical((value == null) ? null : value.getFourthRadical());
    }

    public final ArabicLetterType getFirstRadical() {
        return firstRadical.get();
    }

    public final void setFirstRadical(ArabicLetterType firstRadical) {
        this.firstRadical.set(firstRadical);
    }

    public final ObjectProperty<ArabicLetterType> firstRadicalProperty() {
        return firstRadical;
    }

    public final ArabicLetterType getSecondRadical() {
        return secondRadical.get();
    }

    public final void setSecondRadical(ArabicLetterType secondRadical) {
        this.secondRadical.set(secondRadical);
    }

    public final ObjectProperty<ArabicLetterType> secondRadicalProperty() {
        return secondRadical;
    }

    public final ArabicLetterType getThirdRadical() {
        return thirdRadical.get();
    }

    public final void setThirdRadical(ArabicLetterType thirdRadical) {
        this.thirdRadical.set(thirdRadical);
    }

    public final ObjectProperty<ArabicLetterType> thirdRadicalProperty() {
        return thirdRadical;
    }

    public final ArabicLetterType getFourthRadical() {
        return fourthRadical.get();
    }

    public final void setFourthRadical(ArabicLetterType fourthRadical) {
        this.fourthRadical.set(fourthRadical);
    }

    public final ObjectProperty<ArabicLetterType> fourthRadicalProperty() {
        return fourthRadical;
    }
}
