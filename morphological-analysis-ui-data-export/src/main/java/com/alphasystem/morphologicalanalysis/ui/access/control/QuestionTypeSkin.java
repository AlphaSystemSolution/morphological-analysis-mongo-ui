package com.alphasystem.morphologicalanalysis.ui.access.control;

import com.alphasystem.morphologicalanalysis.ui.access.control.controller.QuestionTypeController;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
class QuestionTypeSkin extends SkinBase<QuestionTypeView> {

    QuestionTypeSkin(QuestionTypeView control) {
        super(control);
        getChildren().setAll(ApplicationContextProvider.getBean(QuestionTypeController.class));
    }
}
