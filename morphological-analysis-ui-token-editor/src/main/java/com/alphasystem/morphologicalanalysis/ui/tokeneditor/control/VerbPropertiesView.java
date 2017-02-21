package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control;

import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.VerbPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.ConversationType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerb;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbCategory;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbMode;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbPartOfSpeechType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.VerbType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sali
 */
@Component
public class VerbPropertiesView extends AbstractPropertiesView<VerbPartOfSpeechType, VerbProperties> {

    private final ObjectProperty<ConversationType> conversationType = new SimpleObjectProperty<>(this, "conversationType");
    private final ObjectProperty<VerbType> verbType = new SimpleObjectProperty<>(this, "verbType");
    private final ObjectProperty<VerbMode> verbMode = new SimpleObjectProperty<>(this, "verbMode");
    private final ObjectProperty<IncompleteVerbCategory> incompleteVerbCategory = new SimpleObjectProperty<>(this, "incompleteVerbCategory");
    private final ObjectProperty<IncompleteVerbType> incompleteVerbType = new SimpleObjectProperty<>(this, "incompleteVerbType");

    @SuppressWarnings({"unchecked"})
    public VerbPropertiesView() {
        super();
        conversationTypeProperty().addListener((o, ov, nv) -> {
            VerbProperties verbProperties = getLocationProperties();
            if (verbProperties != null) {
                verbProperties.setConversationType(nv);
            }
        });
        verbTypeProperty().addListener((o, ov, nv) -> {
            VerbProperties verbProperties = getLocationProperties();
            if (verbProperties != null) {
                verbProperties.setVerbType(nv);
            }
        });
        verbModeProperty().addListener((o, ov, nv) -> {
            VerbProperties verbProperties = getLocationProperties();
            if (verbProperties != null) {
                verbProperties.setMode(nv);
            }
        });
        incompleteVerbCategoryProperty().addListener((o, ov, nv) -> {
            VerbProperties verbProperties = getLocationProperties();
            if (verbProperties != null) {
                if (nv == null || IncompleteVerbCategory.DUMMY.equals(nv)) {
                    verbProperties.setIncompleteVerb(null);
                } else {
                    final IncompleteVerb incompleteVerb = createIncompleteVerb(nv);
                    verbProperties.setIncompleteVerb(incompleteVerb);
                    setIncompleteVerbType(incompleteVerb.getType());
                }
            }
        });
        incompleteVerbTypeProperty().addListener((o, ov, nv) -> {
            VerbProperties verbProperties = getLocationProperties();
            if (verbProperties != null) {
                IncompleteVerb incompleteVerb = verbProperties.getIncompleteVerb();
                if (incompleteVerb != null) {
                    incompleteVerb.setType(nv);
                }
            }
        });
    }

    @PostConstruct
    void postConstruct() {
        setSkin(createDefaultSkin());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new VerbPropertiesSkin(this);
    }

    @Override
    void setValues(VerbProperties nv) {
        super.setValues(nv);
        setConversationType((nv == null) ? null : nv.getConversationType());
        setVerbType((nv == null) ? null : nv.getVerbType());
        setVerbMode((nv == null) ? null : nv.getMode());
        IncompleteVerb incompleteVerb = (nv == null) ? null : nv.getIncompleteVerb();
        final IncompleteVerbCategory category = (incompleteVerb == null) ? IncompleteVerbCategory.DUMMY : incompleteVerb.getCategory();
        final IncompleteVerbType type = (incompleteVerb == null) ? null : incompleteVerb.getType();
        setIncompleteVerbCategory(category);
        setIncompleteVerbType(type);
    }

    @Override
    protected VerbPartOfSpeechType getDefaultPartOfSpeechType() {
        return VerbPartOfSpeechType.VERB;
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

    public final VerbMode getVerbMode() {
        return verbMode.get();
    }

    public final void setVerbMode(VerbMode verbMode) {
        this.verbMode.set(verbMode);
    }

    public final ObjectProperty<VerbMode> verbModeProperty() {
        return verbMode;
    }

    public final IncompleteVerbCategory getIncompleteVerbCategory() {
        return incompleteVerbCategory.get();
    }

    public final void setIncompleteVerbCategory(IncompleteVerbCategory incompleteVerbCategory) {
        this.incompleteVerbCategory.set(incompleteVerbCategory);
    }

    public final ObjectProperty<IncompleteVerbCategory> incompleteVerbCategoryProperty() {
        return incompleteVerbCategory;
    }

    public final IncompleteVerbType getIncompleteVerbType() {
        return incompleteVerbType.get();
    }

    public final void setIncompleteVerbType(IncompleteVerbType incompleteVerbType) {
        this.incompleteVerbType.set(incompleteVerbType);
    }

    public final ObjectProperty<IncompleteVerbType> incompleteVerbTypeProperty() {
        return incompleteVerbType;
    }

    @SuppressWarnings({"unchecked"})
    private IncompleteVerb createIncompleteVerb(IncompleteVerbCategory incompleteVerbCategory) {
        IncompleteVerb incompleteVerb = null;
        if (incompleteVerbCategory == null) {
            return null;
        }
        Class<? extends IncompleteVerb> categoryClassName = incompleteVerbCategory.getCategoryClassName();
        try {
            incompleteVerb = categoryClassName.newInstance();
            incompleteVerb.setCategory(incompleteVerbCategory);
            incompleteVerb.setType(incompleteVerbCategory.getMembers()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return incompleteVerb;
    }

}
