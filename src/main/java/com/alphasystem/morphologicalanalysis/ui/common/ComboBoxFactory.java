package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.scene.control.ComboBox;

import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
import static com.alphasystem.arabic.ui.ListType.LABEL_AND_CODE;
import static com.alphasystem.arabic.ui.ListType.LABEL_ONLY;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * @author sali
 */
public class ComboBoxFactory {

    private static ComboBoxFactory instance;

    /**
     * Do not let any one instantiate this class.
     */
    private ComboBoxFactory() {
    }

    public synchronized static ComboBoxFactory getInstance() {
        if (instance == null) {
            instance = new ComboBoxFactory();
        }
        return instance;
    }

    public ComboBox<RelationshipType> getRelationshipTypeComboBox() {
        return createComboBox(LABEL_ONLY, RelationshipType.values());
    }

    public ComboBox<PartOfSpeech> getPartOfSpeechComboBox() {
        return createComboBox(PartOfSpeech.values());
    }

    public ComboBox<NamedTemplate> getNamedTemplateComboBox() {
        return createComboBox(true, LABEL_AND_CODE, NamedTemplate.values());
    }

    public ComboBox<NamedTag> getNamedTagComboBox() {
        return createComboBox(true, LABEL_AND_CODE, NamedTag.values());
    }

    public ComboBox<NounStatus> getNounStatusComboBox() {
        return createComboBox(NounStatus.values());
    }

    public ComboBox<AlternateStatus> getAlternateStatusComboBox() {
        return createComboBox(true, AlternateStatus.values());
    }

    public ComboBox<NumberType> getNumberTypeComboBox() {
        return createComboBox(NumberType.values());
    }

    public ComboBox<GenderType> getGenderTypeComboBox() {
        return createComboBox(GenderType.values());
    }

    public ComboBox<NounType> getNounTypeComboBox() {
        return createComboBox(NounType.values());
    }

    public ComboBox<NounKind> getNounKindComboBox() {
        return createComboBox(true, LABEL_AND_CODE, NounKind.values());
    }

    public ComboBox<ProNounType> getProNounTypeComboBox() {
        return createComboBox(ProNounType.values());
    }

    public ComboBox<ConversationType> getConversationTypeComboBox() {
        return createComboBox(ConversationType.values());
    }

    public ComboBox<VerbType> getVerbTypeComboBox() {
        return createComboBox(VerbType.values());
    }

    public ComboBox<VerbMode> getVerbModeComboBox() {
        return createComboBox(true, LABEL_AND_CODE, VerbMode.values());
    }

    public ComboBox<IncompleteVerbCategory> getIncompleteVerbCategoryComboBox() {
        return createComboBox(true, LABEL_ONLY, IncompleteVerbCategory.values());
    }

    public ComboBox<IncompleteVerbType> getIncompleteVerbTypeComboBox(IncompleteVerbType[] values) {
        boolean addEmptyValue = isEmpty(values);
        values = addEmptyValue ? new IncompleteVerbType[0] : values;
        return createComboBox(addEmptyValue, LABEL_ONLY, values);
    }

}
