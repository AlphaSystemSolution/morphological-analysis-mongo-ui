package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.GenderType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NumberType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

import static com.alphasystem.util.AppUtil.getResource;

/**
 * @author sali
 */
public class AbstractPropertiesView<T extends AbstractProperties> extends Control {

    protected final ObjectProperty<T> locationProperties = new SimpleObjectProperty<>(null, "locationProperties");
    protected final ObjectProperty<NumberType> numberType = new SimpleObjectProperty<>(null, "numberType");
    protected final ObjectProperty<GenderType> genderType = new SimpleObjectProperty<>(null, "genderType");

    public AbstractPropertiesView() {
        locationPropertiesProperty().addListener((o, ov, nv) -> setValues(nv));
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

    protected void setValues(T nv) {
        setNumberType((nv == null) ? null : nv.getNumber());
        setGenderType((nv == null) ? null : nv.getGender());
    }

    @Override
    public String getUserAgentStylesheet() {
        return getResource("styles/application.css").toExternalForm();
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
