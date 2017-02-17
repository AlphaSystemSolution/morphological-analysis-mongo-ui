package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.ParticlePropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ParticlePartOfSpeechType;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class ParticlePropertiesView extends AbstractPropertiesView<ParticlePartOfSpeechType, ParticleProperties> {

    public ParticlePropertiesView() {
        super();
    }

    @PostConstruct
    void postConstruct(){
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ParticlePropertiesSkin(this);
    }

    @Override
    protected ParticlePartOfSpeechType getDefaultPartOfSpeechType() {
        return ParticlePartOfSpeechType.GENITIVE_PARTICLE;
    }

}
