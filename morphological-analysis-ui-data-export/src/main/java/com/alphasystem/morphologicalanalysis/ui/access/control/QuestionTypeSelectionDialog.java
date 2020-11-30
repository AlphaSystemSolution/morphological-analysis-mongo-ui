package com.alphasystem.morphologicalanalysis.ui.access.control;

import com.alphasystem.access.model.QuestionType;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationContextProvider;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;

/**
 * @author sali
 */
public class QuestionTypeSelectionDialog extends Dialog<QuestionType> {

    private QuestionTypeView view;

    public QuestionTypeSelectionDialog(){
        setTitle("Select Question Type");
        initModality(Modality.APPLICATION_MODAL);

        view = ApplicationContextProvider.getBean(QuestionTypeView.class);
        view.setMaxWidth(300);
        view.setMinWidth(300);
        view.setPrefWidth(300);

        getDialogPane().setContent(view);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);
        setResultConverter(param -> param.getButtonData().isDefaultButton() ? view.getQuestionType() : null);
    }

    public QuestionTypeView getView() {
        return view;
    }
}
