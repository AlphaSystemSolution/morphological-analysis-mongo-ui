package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicSupportEnum;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.scene.control.ComboBox;

import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.LABEL_AND_CODE;
import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.LABEL_ONLY;

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

    private static <T extends ArabicSupportEnum> ComboBox<T> createComboBox(boolean addEmptyValue,
                                                                            ListType type, T... values) {
        ComboBox<T> comboBox = new ComboBox<>();
        if(addEmptyValue){
            comboBox.getItems().add(null);
        }
        comboBox.getItems().addAll(values);
        comboBox.setCellFactory(new ArabicSupportEnumCellFactory<>(type));
        comboBox.setButtonCell(new ArabicSupportEnumListCell<>(type));
        return comboBox;
    }

    private static <T extends ArabicSupportEnum> ComboBox<T> createComboBox(ListType type, T... values) {
        return createComboBox(false, type, values);
    }

    private static <T extends ArabicSupportEnum> ComboBox<T> createComboBox(T... values) {
        return createComboBox(LABEL_AND_CODE, values);
    }

    public ComboBox<RelationshipType> getRelationshipTypeComboBox(){
        return createComboBox(LABEL_ONLY, RelationshipType.values());
    }

    public ComboBox<PartOfSpeech> getPartOfSpeechComboBox() {
        return createComboBox(PartOfSpeech.values());
    }

    public ComboBox<NamedTemplate> getNamedTemplateComboBox(){
        return createComboBox(true, LABEL_AND_CODE, NamedTemplate.values());
    }

    public ComboBox<NamedTag> getNamedTagComboBox(){
        return createComboBox(true, LABEL_AND_CODE, NamedTag.values());
    }
}
