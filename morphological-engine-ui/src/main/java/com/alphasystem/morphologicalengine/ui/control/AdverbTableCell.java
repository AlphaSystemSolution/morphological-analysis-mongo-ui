package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.morphologicalengine.ui.control.model.TableModel;
import com.alphasystem.arabic.ui.AdverbPane;
import com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

/**
 * @author sali
 */
public class AdverbTableCell extends ListTableCell<NounOfPlaceAndTime> {

    public AdverbTableCell(TableColumn<TableModel, ObservableList<NounOfPlaceAndTime>> column) {
        super(column, new AdverbPane());
    }
}
