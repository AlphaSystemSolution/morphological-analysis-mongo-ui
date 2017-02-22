package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.application.ApplicationController;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

/**
 * @author sali
 */
public class LabelSelectionChangeListener implements ChangeListener<Boolean> {

    private final ApplicationController applicationController = ApplicationController.getInstance();
    private final LabelSelectionModel model;

    public LabelSelectionChangeListener(LabelSelectionModel model) {
        this.model = model;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            int index = 0;
            final ObservableList<ArabicLabelView> toggles = model.getGroup().getToggles();
            for (int j = 0; j < toggles.size(); j++) {
                if (toggles.get(j).getId().equals(model.getLabelView().getId())) {
                    index = j;
                    break;
                }
            } // end of "for"
            // TODO: validate
            // update current location, create new, add location to token, and finally re-initialize
            final Token token = model.getControl().getToken();
            final ArabicWord tokenWord = token.tokenWord();
            applicationController.updateLocation(model.getLocation(), index, tokenWord);
            final Location newLocation = applicationController.createNewLocation(model.getLocation(), index,
                    tokenWord, WordType.NOUN);
            token.addLocation(newLocation);
            model.getControl().setToken(null);
            model.getControl().setToken(token);
        }
    }
}
