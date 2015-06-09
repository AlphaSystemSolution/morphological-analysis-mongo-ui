package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicSupportEnum;
import com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.scene.control.ComboBox;

import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumAdapter.populateValues;
import static com.alphasystem.morphologicalanalysis.ui.common.ArabicSupportEnumCellFactory.ListType.ARABIC_AND_ENGLISH;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.TREE_BANK_STYLE_SHEET;

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

    private static <T extends ArabicSupportEnum> ComboBox<ArabicSupportEnumAdapter<T>> createComboBox(T[] values) {
        ComboBox<ArabicSupportEnumAdapter<T>> comboBox = new ComboBox<>();
        comboBox.getStylesheets().add(TREE_BANK_STYLE_SHEET);
        comboBox.getItems().addAll(populateValues(values));
        return comboBox;
    }

    private static <T extends ArabicSupportEnum> ComboBox<T> createComboBox(T[] values, ListType type) {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(values);
        comboBox.setCellFactory(new ArabicSupportEnumCellFactory<>(type));
        return comboBox;
    }

    /**
     * @return
     */
    public ComboBox<ArabicSupportEnumAdapter<RelationshipType>> getGrammaticalRelationshipComboBox() {
        return createComboBox(RelationshipType.values());
    }

    public ComboBox<PartOfSpeech> getPartOfSpeechComboBox() {
        return createComboBox(PartOfSpeech.values(), ARABIC_AND_ENGLISH);
    }
}
