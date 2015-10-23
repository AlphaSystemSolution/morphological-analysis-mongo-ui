package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicSupportEnum;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.scene.control.ComboBox;

import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.LABEL_AND_CODE;
import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.LABEL_ONLY;
import static java.lang.Double.MAX_VALUE;

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

    @SafeVarargs
    private static <T extends ArabicSupportEnum> ComboBox<T> createComboBox(boolean addEmptyValue,
                                                                            ListType type, T... values) {
        ComboBox<T> comboBox = new ComboBox<>();
        if (addEmptyValue) {
            comboBox.getItems().add(null);
        }
        comboBox.getItems().addAll(values);
        comboBox.setCellFactory(new ArabicSupportEnumCellFactory<>(type));
        comboBox.setButtonCell(new ArabicSupportEnumListCell<>(type));
        comboBox.getSelectionModel().select(0);
        comboBox.setMaxWidth(MAX_VALUE);
        comboBox.setMaxHeight(MAX_VALUE);
        return comboBox;
    }

    @SafeVarargs
    private static <T extends ArabicSupportEnum> ComboBox<T> createComboBox(boolean addEmptyValue, T... values) {
        return createComboBox(addEmptyValue, LABEL_AND_CODE, values);
    }

    @SafeVarargs
    private static <T extends ArabicSupportEnum> ComboBox<T> createComboBox(ListType type, T... values) {
        return createComboBox(false, type, values);
    }

    @SafeVarargs
    private static <T extends ArabicSupportEnum> ComboBox<T> createComboBox(T... values) {
        return createComboBox(LABEL_AND_CODE, values);
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

    public ComboBox<AlternateStatus> getAlternateStatusComboBox(){
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
}
