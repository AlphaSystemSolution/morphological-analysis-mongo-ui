package com.alphasystem.morphologicalanalysis.ui.access.control.controller;

import com.alphasystem.access.model.QuestionType;
import com.alphasystem.morphologicalanalysis.ui.access.control.QuestionTypeView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author sali
 */
@Component
public class QuestionTypeController extends BorderPane {

    @Autowired private QuestionTypeView control;

    @FXML private ComboBox<QuestionType> questionTypeComboBox;

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize(){
        questionTypeComboBox.getItems().addAll(QuestionType.values());
        questionTypeComboBox.valueProperty().bindBidirectional(control.questionTypeProperty());
    }
}
