package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.morphologicalengine.ui.control.model.TableModel;
import com.alphasystem.arabic.ui.VerbalNounPane;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

/**
 * @author sali
 */
public class VerbalNounTableCell extends ListTableCell<VerbalNoun> {

    public VerbalNounTableCell(TableColumn<TableModel, ObservableList<VerbalNoun>> column) {
        super(column, new VerbalNounPane());
    }
}
