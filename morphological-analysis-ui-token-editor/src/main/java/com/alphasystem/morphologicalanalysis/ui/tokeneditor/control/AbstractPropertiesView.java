package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeechType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

/**
 * @author sali
 */
public abstract class AbstractPropertiesView<P extends Enum<P> & PartOfSpeechType, T extends AbstractProperties<P>> extends Control {

    private final ObjectProperty<T> locationProperties = new SimpleObjectProperty<>(this, "locationProperties");
    private final ObjectProperty<P> partOfSpeechType = new SimpleObjectProperty<>(this, "partOfSpeechType");
    private final ObjectProperty<NumberType> numberType = new SimpleObjectProperty<>(this, "numberType");
    private final ObjectProperty<GenderType> genderType = new SimpleObjectProperty<>(this, "genderType");

    public AbstractPropertiesView() {
        locationPropertiesProperty().addListener((o, ov, nv) -> setValues(nv));
        partOfSpeechTypeProperty().addListener((observable, oldValue, newValue) -> {
            T properties = getLocationProperties();
            if (properties != null) {
                properties.setPartOfSpeech(newValue);
            }
        });
        numberTypeProperty().addListener((o, ov, nv) -> {
            T properties = getLocationProperties();
            if (properties != null) {
                properties.setNumber(nv);
            }
        });
        genderTypeProperty().addListener((o, ov, nv) -> {
            T properties = getLocationProperties();
            if (properties != null) {
                properties.setGender(nv);
            }
        });
    }

    void setValues(T nv) {
        setPartOfSpeechType((nv == null) ? getDefaultPartOfSpeechType() : nv.getPartOfSpeech());
        setNumberType((nv == null) ? null : nv.getNumber());
        setGenderType((nv == null) ? null : nv.getGender());
    }

    protected abstract P getDefaultPartOfSpeechType();

    @Override
    public String getUserAgentStylesheet() {
        return ApplicationHelper.STYLE_SHEET_PATH;
    }

    public final T getLocationProperties() {
        return locationProperties.get();
    }

    public final void setLocationProperties(T locationProperties) {
        this.locationProperties.set(locationProperties);
    }

    public final ObjectProperty<T> locationPropertiesProperty() {
        return locationProperties;
    }

    public final P getPartOfSpeechType() {
        return partOfSpeechType.get();
    }

    public final ObjectProperty<P> partOfSpeechTypeProperty() {
        return partOfSpeechType;
    }

    public final void setPartOfSpeechType(P partOfSpeechType) {
        this.partOfSpeechType.set(partOfSpeechType);
    }

    public final NumberType getNumberType() {
        return numberType.get();
    }

    public final void setNumberType(NumberType numberType) {
        this.numberType.set(numberType);
    }

    public final ObjectProperty<NumberType> numberTypeProperty() {
        return numberType;
    }

    public final GenderType getGenderType() {
        return genderType.get();
    }

    public final void setGenderType(GenderType genderType) {
        this.genderType.set(genderType);
    }

    public final ObjectProperty<GenderType> genderTypeProperty() {
        return genderType;
    }
}