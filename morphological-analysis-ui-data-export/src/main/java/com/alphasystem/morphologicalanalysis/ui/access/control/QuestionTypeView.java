package com.alphasystem.morphologicalanalysis.ui.access.control;

import com.alphasystem.access.model.QuestionType;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.springframework.stereotype.Component;

/**
 * @author sali
 */
@Component
public class QuestionTypeView extends Control {

    private final ObjectProperty<QuestionType> questionType = new SimpleObjectProperty<>(this, "questionType", QuestionType.LIST);

    void postConstruct(){
        setSkin(createDefaultSkin());
    }

    public final QuestionType getQuestionType() {
        return questionType.get();
    }

    public final ObjectProperty<QuestionType> questionTypeProperty() {
        return questionType;
    }

    public final void setQuestionType(QuestionType questionType) {
        this.questionType.set(questionType);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new QuestionTypeSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return ApplicationHelper.STYLE_SHEET_PATH;
    }
}
