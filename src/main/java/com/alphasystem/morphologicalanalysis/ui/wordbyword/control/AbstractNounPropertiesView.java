package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public class AbstractNounPropertiesView<T extends AbstractNounProperties> extends AbstractPropertiesView<T> {

    protected final ObjectProperty<NounStatus> nounStatus = new SimpleObjectProperty<>(null, "nounStatus");

    public AbstractNounPropertiesView() {
        super();
        nounStatusProperty().addListener((o, ov, nv) -> {
            T properties = getLocationProperties();
            if (properties != null) {
                properties.setStatus(nv);
            }
        });
    }

    @Override
    protected void setValues(T nv) {
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
