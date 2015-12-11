package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.ProNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ProNounType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public class ProNounPropertiesAdapter extends AbstractNounPropertiesAdapter<ProNounProperties> {

    private ObjectProperty<ConversationType> conversationType = new SimpleObjectProperty<>(null, "conversationType");
    private ObjectProperty<ProNounType> proNounType = new SimpleObjectProperty<>(null, "proNounType");

    public ProNounPropertiesAdapter() {
        super();
        conversationTypeProperty().addListener((o, ov, nv) -> getSrc().setConversationType(nv));
        proNounTypeProperty().addListener((o, ov, nv) -> getSrc().setProNounType(nv));
    }

    @Override
    protected void initValues(ProNounProperties value) {
        super.initValues(value);
        setConversationType((value == null) ? null : value.getConversationType());
        setProNounType((value == null) ? null : value.getProNounType());
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

    public final ProNounType getProNounType() {
        return proNounType.get();
    }

    public final void setProNounType(ProNounType proNounType) {
        this.proNounType.set(proNounType);
    }

    public final ObjectProperty<ProNounType> proNounTypeProperty() {
        return proNounType;
    }
}
