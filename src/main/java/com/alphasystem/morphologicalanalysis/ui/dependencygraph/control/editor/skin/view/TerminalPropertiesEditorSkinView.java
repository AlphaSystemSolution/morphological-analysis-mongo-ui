package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.fx.ui.util.FontSizeStringConverter;
import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.TerminalPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNodeAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;

/**
 * @author sali
 */
public class TerminalPropertiesEditorSkinView extends LineSupportPropertiesEditorSkinView<TerminalNode, TerminalNodeAdapter, TerminalPropertiesEditor> {

    @FXML protected Accordion accordion;

    @FXML private TextField translationTextField;
    @FXML private Spinner<Double> translationXSpinner;
    @FXML private Slider translationXSlider;
    @FXML private Spinner<Double> translationYSpinner;
    @FXML private Slider translationYSlider;
    @FXML private ComboBox<String> translationFontFamily;
    @FXML private ComboBox<Long> translationFontSize;
    @FXML private Spinner<Double> groupTranslateXSpinner;
    @FXML private Slider groupTranslateXSlider;
    @FXML private Spinner<Double> groupTranslateYSpinner;
    @FXML private Slider groupTranslateYSlider;

    public TerminalPropertiesEditorSkinView(TerminalPropertiesEditor control) {
        super(control);
    }

    @Override
    void initialize(TerminalNodeAdapter node) {
        super.initialize(node);
        translationXSlider.setValue(node.getTranslationX());
        translationYSlider.setValue(node.getTranslationY());
        groupTranslateXSlider.setValue(node.getTranslateX());
        groupTranslateYSlider.setValue(node.getTranslationY());
        setFont(translationFontFamily,translationFontSize,node.getTranslationFont());
    }

    @Override
    void initializeValues() {
        super.initializeValues();
        translationTextField.setFont(preferences.getEnglishFont12());
        translationTextField.textProperty().bindBidirectional(control.translationTextProperty());
        translationFontFamily.getItems().addAll(Font.getFontNames());
        translationFontFamily.getSelectionModel().selectFirst();
        translationFontSize.getSelectionModel().selectFirst();
        translationFontSize.setConverter(new FontSizeStringConverter());
        control.translationFontProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            final String family = newValue.getFamily();
            translationFontFamily.setValue(family);
            translationFontFamily.getSelectionModel().select(family);
            final Long size = new Double(newValue.getSize()).longValue();
            translationFontSize.setValue(size);
            translationFontSize.getSelectionModel().select(size);
        });
        translationFontFamily.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            FontMetaInfo fontMetaInfo = fromFont(control.getTranslationFont());
            fontMetaInfo = deriveFromFamily(fontMetaInfo, newValue);
            control.setTranslationFont(fromFontMetaInfo(fontMetaInfo));
        });
        translationFontSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            FontMetaInfo fontMetaInfo = fromFont(control.getTranslationFont());
            fontMetaInfo = deriveFromSize(fontMetaInfo, newValue);
            control.setTranslationFont(fromFontMetaInfo(fontMetaInfo));
        });
        setupSpinnerSliderField(control.translationXProperty(), translationXSpinner, translationXSlider, true);
        setupSpinnerSliderField(control.translationYProperty(), translationYSpinner, translationYSlider, false);
        setupSpinnerSliderField(control.xProperty(), groupTranslateXSpinner, groupTranslateXSlider, true);
        setupSpinnerSliderField(control.yProperty(), groupTranslateYSpinner, groupTranslateYSlider, false);
    }
}
