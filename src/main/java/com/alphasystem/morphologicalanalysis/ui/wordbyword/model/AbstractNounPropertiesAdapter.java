package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NounStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public abstract class AbstractNounPropertiesAdapter<P extends AbstractNounProperties> extends AbstractPropertiesAdapter<P> {

    protected ObjectProperty<NounStatus> status = new SimpleObjectProperty<>(null, "status");

    public AbstractNounPropertiesAdapter() {
        super();
        statusProperty().addListener((o, ov, nv) -> getSrc().setStatus(nv));
    }

    @Override
    protected void initValues(P value) {
        super.initValues(value);
        setStatus((value == null) ? null : value.getStatus());
    }

    public final NounStatus getStatus() {
        return status.get();
    }

    public final void setStatus(NounStatus status) {
        this.status.set(status);
    }

    public final ObjectProperty<NounStatus> statusProperty() {
        return status;
    }
}
