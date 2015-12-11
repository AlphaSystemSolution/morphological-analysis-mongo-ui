package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

import com.alphasystem.morphologicalanalysis.ui.common.AbstractDocumentAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public abstract class AbstractPropertiesAdapter<P extends AbstractProperties> extends AbstractDocumentAdapter<P> {

    protected final ObjectProperty<NumberType> number = new SimpleObjectProperty<>(null, "number");
    protected final ObjectProperty<GenderType> gender = new SimpleObjectProperty<>(null, "gender");

    public AbstractPropertiesAdapter() {
        super();
        numberProperty().addListener((o, ov, nv) -> getSrc().setNumber(nv));
        genderProperty().addListener((o, ov, nv) -> getSrc().setGender(nv));
    }

    public final NumberType getNumber() {
        return number.get();
    }

    public final void setNumber(NumberType number) {
        this.number.set(number);
    }

    public final ObjectProperty<NumberType> numberProperty() {
        return number;
    }

    public final GenderType getGender() {
        return gender.get();
    }

    public final void setGender(GenderType gender) {
        this.gender.set(gender);
    }

    public final ObjectProperty<GenderType> genderProperty() {
        return gender;
    }
}
