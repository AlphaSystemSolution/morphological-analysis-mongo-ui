package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.wordbyword.model.support.AlternateStatus;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.scene.control.ComboBox;

import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
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

    public static ComboBox<AlternateStatus> getAlternateStatusComboBox() {
        return createComboBox(true, AlternateStatus.values());
    }

}
