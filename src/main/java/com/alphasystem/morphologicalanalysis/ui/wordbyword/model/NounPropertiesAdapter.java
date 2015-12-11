package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounKind;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public final class NounPropertiesAdapter extends AbstractNounPropertiesAdapter<NounProperties> {

    private ObjectProperty<NounKind> nounKind = new SimpleObjectProperty<>(null, "nounKind");
    private ObjectProperty<NounType> nounType = new SimpleObjectProperty<>(null, "nounType");

    public NounPropertiesAdapter() {
        super();
        nounKindProperty().addListener((o, ov, nv) -> getSrc().setNounKind(nv));
        nounTypeProperty().addListener((o, ov, nv) -> getSrc().setNounType(nv));
    }

    @Override
    protected void initValues(NounProperties value) {
        super.initValues(value);
        setNounKind((value == null) ? null : value.getNounKind());
        setNounType((value == null) ? null : value.getNounType());
    }

    public final NounKind getNounKind() {
        return nounKind.get();
    }

    public final void setNounKind(NounKind nounKind) {
        this.nounKind.set(nounKind);
    }

    public final ObjectProperty<NounKind> nounKindProperty() {
        return nounKind;
    }

    public final NounType getNounType() {
        return nounType.get();
    }

    public final void setNounType(NounType nounType) {
        this.nounType.set(nounType);
    }

    public final ObjectProperty<NounType> nounTypeProperty() {
        return nounType;
    }
}
