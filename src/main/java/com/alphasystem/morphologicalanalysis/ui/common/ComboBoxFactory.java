package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.scene.control.ComboBox;

import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
import static com.alphasystem.arabic.ui.ListType.LABEL_AND_CODE;
import static com.alphasystem.arabic.ui.ListType.LABEL_ONLY;

/**
 * @author sali
 */
public class ComboBoxFactory {

    /**
     * Do not let any one instantiate this class.
     */
    private ComboBoxFactory() {
    }

    public static ComboBox<RelationshipType> getRelationshipTypeComboBox() {
        return createComboBox(LABEL_ONLY, RelationshipType.values());
    }

    public static ComboBox<PartOfSpeech> getPartOfSpeechComboBox() {
        return createComboBox(PartOfSpeech.values());
    }

    public static ComboBox<NamedTemplate> getNamedTemplateComboBox() {
        return createComboBox(true, LABEL_AND_CODE, NamedTemplate.values());
    }

    public static ComboBox<NamedTag> getNamedTagComboBox() {
        return createComboBox(true, LABEL_AND_CODE, NamedTag.values());
    }

    public static ComboBox<NounStatus> getNounStatusComboBox() {
        return createComboBox(NounStatus.values());
    }

    public static ComboBox<AlternateStatus> getAlternateStatusComboBox() {
        return createComboBox(true, AlternateStatus.values());
    }

    public static ComboBox<NumberType> getNumberTypeComboBox() {
        return createComboBox(NumberType.values());
    }

    public static ComboBox<GenderType> getGenderTypeComboBox() {
        return createComboBox(GenderType.values());
    }

    public static ComboBox<NounType> getNounTypeComboBox() {
        return createComboBox(NounType.values());
    }

    public static ComboBox<NounKind> getNounKindComboBox() {
        return createComboBox(true, LABEL_AND_CODE, NounKind.values());
    }

    public static ComboBox<ProNounType> getProNounTypeComboBox() {
        return createComboBox(ProNounType.values());
    }

    public static ComboBox<ConversationType> getConversationTypeComboBox() {
        return createComboBox(ConversationType.values());
    }

    public static ComboBox<VerbType> getVerbTypeComboBox() {
        return createComboBox(VerbType.values());
    }

    public static ComboBox<VerbMode> getVerbModeComboBox() {
        return createComboBox(true, LABEL_AND_CODE, VerbMode.values());
    }

    public static ComboBox<IncompleteVerbCategory> getIncompleteVerbCategoryComboBox() {
        return createComboBox(true, LABEL_ONLY, IncompleteVerbCategory.values());
    }

    public static <T extends Enum<T> & IncompleteVerbType> ComboBox<T> getIncompleteVerbTypeComboBox(T[] values) {
        return createComboBox(true, LABEL_ONLY, values);
    }

}
