package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.fx.ui.util.FontConstants;
import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.TranslationPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.FontSizeStringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.PropertiesEditor.setupField;
import static com.alphasystem.util.AppUtil.getPath;

/**
 * @author sali
 */
public class TranslationPropertiesEditorSkinView<N extends TerminalNode, A extends GraphNodeAdapter<N>> extends GridPane {

    private final TranslationPropertiesEditor<N, A> control;

    @FXML private TextField textField;
    @FXML private Spinner<Double> xSpinner;
    @FXML private Slider xSlider;
    @FXML private Spinner<Double> ySpinner;
    @FXML private Slider ySlider;
    @FXML private ComboBox<String> translationFontFamily;
    @FXML private ComboBox<Integer> translationFontSize;

    public TranslationPropertiesEditorSkinView(TranslationPropertiesEditor<N, A> control) {
        this.control = control;
        try {
            loadFXML(this, getPath("fxml.editor.TranslationPropertiesEditor.fxml").toUri().toURL(), RESOURCE_BUNDLE);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        textField.setFont(FontConstants.ENGLISH_FONT_12);
        textField.textProperty().bindBidirectional(control.textProperty());
        translationFontFamily.getItems().addAll(Font.getFontNames());
        translationFontFamily.getSelectionModel().selectFirst();
        translationFontSize.getSelectionModel().selectFirst();
        translationFontSize.setConverter(new FontSizeStringConverter());
        setupField(control.xProperty(), xSpinner, xSlider);
        setupField(control.yProperty(), ySpinner, ySlider);
        control.translationFontProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            final String family = newValue.getFamily();
            translationFontFamily.setValue(family);
            translationFontFamily.getSelectionModel().select(family);
            final Integer size = new Double(newValue.getSize()).intValue();
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
    }
}
