package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.ProNounPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ProNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ProNounType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class ProNounPropertiesView extends AbstractNounPropertiesView<ProNounProperties> {

    private final ObjectProperty<ConversationType> conversationType = new SimpleObjectProperty<>(null, "conversationType");
    private final ObjectProperty<ProNounType> proNounType = new SimpleObjectProperty<>(null, "proNounType");

    public ProNounPropertiesView() {
        super();
        conversationTypeProperty().addListener((o, ov, nv) -> {
            ProNounProperties properties = getLocationProperties();
            if (properties != null) {
                properties.setConversationType(nv);
            }
        });
        proNounTypeProperty().addListener((o, ov, nv) -> {
            ProNounProperties properties = getLocationProperties();
            if (properties != null) {
                properties.setProNounType(nv);
            }
        });
    }

    @Override
    protected void setValues(ProNounProperties nv) {
        super.setValues(nv);
        setConversationType((nv == null) ? null : nv.getConversationType());
        setProNounType(nv == null ? null : nv.getProNounType());
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

    public final ProNounType geProNounType() {
        return proNounType.get();
    }

    public final ObjectProperty<ProNounType> proNounTypeProperty() {
        return proNounType;
    }

    public final void setProNounType(ProNounType proNounType) {
        this.proNounType.set(proNounType);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ProNounPropertiesSkin(this);
    }
}
