package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeechType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
abstract class AbstractNounPropertiesView<P extends Enum<P> & PartOfSpeechType, T extends
        AbstractNounProperties<P>> extends AbstractPropertiesView<P, T> {

    private final ObjectProperty<NounStatus> nounStatus = new SimpleObjectProperty<>(this, "nounStatus");

    AbstractNounPropertiesView() {
        super();
        nounStatusProperty().addListener((o, ov, nv) -> {
            T properties = getLocationProperties();
            if (properties != null) {
                properties.setStatus(nv);
            }
        });
    }

    @Override
    void setValues(T nv) {
        super.setValues(nv);
        setNounStatus((nv == null) ? null : nv.getStatus());
    }

    public final NounStatus getNounStatus() {
        return nounStatus.get();
    }

    public final void setNounStatus(NounStatus nounStatus) {
        this.nounStatus.set(nounStatus);
    }

    public final ObjectProperty<NounStatus> nounStatusProperty() {
        return nounStatus;
    }
}
