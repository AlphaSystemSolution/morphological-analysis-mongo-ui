package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ParticlePartOfSpeechType;
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
    }
    
    @Override
    protected ParticlePartOfSpeechType getDefaultPartOfSpeechType() {
        return ParticlePartOfSpeechType.GENITIVE_PARTICLE;
    }

}
