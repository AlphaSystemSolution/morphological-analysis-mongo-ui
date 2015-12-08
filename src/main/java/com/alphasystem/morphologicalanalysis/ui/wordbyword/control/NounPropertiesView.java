package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.NounPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounKind;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class NounPropertiesView extends AbstractNounPropertiesView<NounProperties> {

    private final ObjectProperty<NounKind> nounKind = new SimpleObjectProperty<>(null, "nounKind");
    private final ObjectProperty<NounType> nounType = new SimpleObjectProperty<>(null, "nounType");

    public NounPropertiesView() {
        super();
        nounKindProperty().addListener((o, ov, nv) -> {
            NounProperties properties = getLocationProperties();
            if (properties != null) {
                properties.setNounKind(nv);
            }
        });
        nounTypeProperty().addListener((o, ov, nv) -> {
            NounProperties properties = getLocationProperties();
            if (properties != null) {
                properties.setNounType(nv);
            }
        });
    }

    @Override
    protected void setValues(NounProperties nv) {
        super.setValues(nv);
        setNounKind((nv == null) ? null : nv.getNounKind());
        setNounType(nv == null ? null : nv.getNounType());
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

    @Override
    protected Skin<?> createDefaultSkin() {
        return new NounPropertiesSkin(this);
    }
}
