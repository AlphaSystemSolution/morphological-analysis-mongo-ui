package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.fx.ui.util.FontConstants;
import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.CommonPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.FontSizeStringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.util.AppUtil.getPath;

/**
 * @author sali
 */
public class CommonPropertiesEditorSkinView<N extends GraphNode, A extends GraphNodeAdapter<N>> extends SkinViewBase<N, A> {

    protected final CommonPropertiesEditor<N, A> control;

    @FXML private TextField textField;
    @FXML private Spinner<Double> xSpinner;
    @FXML private Slider xSlider;
    @FXML private Spinner<Double> ySpinner;
    @FXML private Slider ySlider;
    @FXML private ComboBox<String> arabicFontFamily;
    @FXML private ComboBox<Integer> arabicFontSize;

    public CommonPropertiesEditorSkinView(CommonPropertiesEditor<N, A> control) {
        this.control = control;
        try {
            loadFXML(this, getPath(String.format("fxml.editor.%s.fxml", control.getClass().getSimpleName())).toUri().toURL(), RESOURCE_BUNDLE);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        textField.setFont(FontConstants.ARABIC_FONT_24);
        textField.textProperty().bindBidirectional(control.textProperty());
        arabicFontFamily.getItems().addAll(Font.getFontNames());
        arabicFontFamily.getSelectionModel().selectFirst();
        arabicFontSize.getSelectionModel().selectFirst();
        arabicFontSize.setConverter(new FontSizeStringConverter());
        setupField(control.xProperty(), xSpinner, xSlider);
        setupField(control.yProperty(), ySpinner, ySlider);
        control.arabicFontProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            final String family = newValue.getFamily();
            arabicFontFamily.setValue(family);
            arabicFontFamily.getSelectionModel().select(family);
            final Integer size = new Double(newValue.getSize()).intValue();
            arabicFontSize.setValue(size);
            arabicFontSize.getSelectionModel().select(size);
        });
        arabicFontFamily.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            FontMetaInfo fontMetaInfo = fromFont(control.getArabicFont());
            fontMetaInfo = deriveFromFamily(fontMetaInfo, newValue);
            control.setArabicFont(fromFontMetaInfo(fontMetaInfo));
        });
        arabicFontSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            FontMetaInfo fontMetaInfo = fromFont(control.getArabicFont());
            fontMetaInfo = deriveFromSize(fontMetaInfo, newValue);
            control.setArabicFont(fromFontMetaInfo(fontMetaInfo));
        });
    }
}
