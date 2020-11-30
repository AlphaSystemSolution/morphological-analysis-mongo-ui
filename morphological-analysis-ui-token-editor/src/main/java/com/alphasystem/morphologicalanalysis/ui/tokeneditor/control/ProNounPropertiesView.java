package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.ProNounPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ProNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ProNounPartOfSpeechType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ProNounType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class ProNounPropertiesView extends AbstractNounPropertiesView<ProNounPartOfSpeechType, ProNounProperties> {

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

    @PostConstruct
    void postConstruct(){
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ProNounPropertiesSkin(this);
    }

    @Override
    void setValues(ProNounProperties nv) {
        super.setValues(nv);
        setConversationType((nv == null) ? null : nv.getConversationType());
        setProNounType(nv == null ? null : nv.getProNounType());
    }

    @Override
    protected ProNounPartOfSpeechType getDefaultPartOfSpeechType() {
        return ProNounPartOfSpeechType.PRONOUN;
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

}
