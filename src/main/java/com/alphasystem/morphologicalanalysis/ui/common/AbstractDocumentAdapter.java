package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.persistence.mongo.model.AbstractDocument;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author sali
 */
public class AbstractDocumentAdapter<D extends AbstractDocument> {

    protected final ObjectProperty<D> src = new SimpleObjectProperty<>(null, "src");
    protected final StringProperty id = new SimpleStringProperty("id");
    protected final StringProperty displayName = new SimpleStringProperty("displayName");

    public AbstractDocumentAdapter() {
        srcProperty().addListener((o, ov, nv) -> initValues(nv));
        idProperty().addListener((o, ov, nv) -> getSrc().setId(nv));
        displayNameProperty().addListener((o, ov, nv) -> getSrc().setId(nv));
    }

    protected void initValues(D value) {
        setId((value == null) ? null : value.getId());
        setDisplayName((value == null) ? null : value.getDisplayName());
    }

    public final D getSrc() {
        return src.get();
    }

    public final void setSrc(D src) {
        this.src.set(src);
    }

    public final ObjectProperty<D> srcProperty() {
        return src;
    }

    public final String getId() {
        return id.get();
    }

    public final void setId(String id) {
        this.id.set(id);
    }

    public final StringProperty idProperty() {
        return id;
    }

    public final String getDisplayName() {
        return displayName.get();
    }

    public final void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    public final StringProperty displayNameProperty() {
        return displayName;
    }
}
