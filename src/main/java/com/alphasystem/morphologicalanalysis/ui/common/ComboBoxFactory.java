package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.arabic.model.ArabicSupportEnum;
import com.alphasystem.morphologicalanalysis.model.support.GrammaticalRelationship;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.Global.TREE_BANK_STYLE_SHEET;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class ComboBoxFactory {

    private static ComboBoxFactory instance;
    private ComboBox<ArabicSupportEnumAdapter<GrammaticalRelationship>> grammaticalRelationshipComboBox;

    /**
     * Do not let any one instantiate this class.
     */
    private ComboBoxFactory() {
        grammaticalRelationshipComboBox = createComboBox(GrammaticalRelationship.values());
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
        ObservableList<ArabicSupportEnumAdapter<T>> items = observableArrayList();
        for (T value : values) {
            items.add(new ArabicSupportEnumAdapter<>(value));
        }
        return comboBox;
    }

    /**
     * @return
     */
    public ComboBox<ArabicSupportEnumAdapter<GrammaticalRelationship>> getGrammaticalRelationshipComboBox() {
        return grammaticalRelationshipComboBox;
    }
}
