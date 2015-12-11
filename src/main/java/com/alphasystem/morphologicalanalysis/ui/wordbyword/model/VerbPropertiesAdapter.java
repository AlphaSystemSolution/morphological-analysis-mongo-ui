package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerb;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbMode;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public final class VerbPropertiesAdapter extends AbstractPropertiesAdapter<VerbProperties> {

    private ObjectProperty<ConversationType> conversationType = new SimpleObjectProperty<>(null, "conversationType");
    private ObjectProperty<VerbType> verbType = new SimpleObjectProperty<>(null, "verbType");
    private ObjectProperty<VerbMode> mode = new SimpleObjectProperty<>(null, "mode");
    private ObjectProperty<IncompleteVerb> incompleteVerb = new SimpleObjectProperty<>(null, "incompleteVerb");

    public VerbPropertiesAdapter() {
        super();
        conversationTypeProperty().addListener((o, ov, nv) -> getSrc().setConversationType(nv));
        verbTypeProperty().addListener((o, ov, nv) -> getSrc().setVerbType(nv));
        modeProperty().addListener((o, ov, nv) -> getSrc().setMode(nv));
        incompleteVerbProperty().addListener((o, ov, nv) -> getSrc().setIncompleteVerb(nv));
    }

    @Override
    protected void initValues(VerbProperties value) {
        super.initValues(value);
        setConversationType((value == null) ? null : value.getConversationType());
        setVerbType((value == null) ? null : value.getVerbType());
        setMode((value == null) ? null : value.getMode());
        setIncompleteVerb((value == null) ? null : value.getIncompleteVerb());
    }

    public final ConversationType getConversationType() {
        return conversationType.get();
    }

    public final void setConversationType(ConversationType conversationType) {
        this.conversationType.set(conversationType);
    }

    public final ObjectProperty<ConversationType> conversationTypeProperty() {
        return conversationType;
    }

    public final VerbType getVerbType() {
        return verbType.get();
    }

    public final void setVerbType(VerbType verbType) {
        this.verbType.set(verbType);
    }

    public final ObjectProperty<VerbType> verbTypeProperty() {
        return verbType;
    }

    public final VerbMode getMode() {
        return mode.get();
    }

    public final void setMode(VerbMode mode) {
        this.mode.set(mode);
    }

    public final ObjectProperty<VerbMode> modeProperty() {
        return mode;
    }

    public final IncompleteVerb getIncompleteVerb() {
        return incompleteVerb.get();
    }

    public final void setIncompleteVerb(IncompleteVerb incompleteVerb) {
        this.incompleteVerb.set(incompleteVerb);
    }

    public final ObjectProperty<IncompleteVerb> incompleteVerbProperty() {
        return incompleteVerb;
    }
}
