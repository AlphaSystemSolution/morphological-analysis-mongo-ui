package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.VerbPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbCategory.DUMMY;

/**
 * @author sali
 */
public class VerbPropertiesView extends AbstractPropertiesView<VerbProperties> {

    private final ObjectProperty<ConversationType> conversationType = new SimpleObjectProperty<>(null, "conversationType");
    private final ObjectProperty<VerbType> verbType = new SimpleObjectProperty<>(null, "verbType");
    private final ObjectProperty<VerbMode> verbMode = new SimpleObjectProperty<>(null, "verbMode");
    private final ObjectProperty<IncompleteVerbCategory> incompleteVerbCategory = new SimpleObjectProperty<>(null, "incompleteVerbCategory");
    private final ObjectProperty<IncompleteVerbType> incompleteVerbType = new SimpleObjectProperty<>(null, "incompleteVerbType");

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
            if ((verbProperties != null) && ((nv != null) && !nv.equals(DUMMY))) {
                verbProperties.setIncompleteVerb(createIncompleteVerb(nv));
            }
        });
        incompleteVerbTypeProperty().addListener((o, ov, nv) -> {
            VerbProperties verbProperties = getLocationProperties();
            if (verbProperties != null) {
                IncompleteVerb incompleteVerb = verbProperties.getIncompleteVerb();
                incompleteVerb.setType(nv);
            }
        });
    }

    @Override
    protected void setValues(VerbProperties nv) {
        super.setValues(nv);
        setConversationType((nv == null) ? null : nv.getConversationType());
        setVerbType((nv == null) ? null : nv.getVerbType());
        setVerbMode((nv == null) ? null : nv.getMode());
        IncompleteVerb incompleteVerb = (nv == null) ? null : nv.getIncompleteVerb();
        if (incompleteVerb != null) {
            setIncompleteVerbCategory(incompleteVerb.getCategory());
            setIncompleteVerbType(incompleteVerb.getType());
        }
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

    @Override
    protected Skin<?> createDefaultSkin() {
        return new VerbPropertiesSkin(this);
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
