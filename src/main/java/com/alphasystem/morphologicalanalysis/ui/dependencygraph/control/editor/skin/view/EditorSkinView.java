package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.fx.ui.util.FontConstants;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.PropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.FontSizeStringConverter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import com.alphasystem.util.AppUtil;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static java.lang.String.format;

/**
 * @author sali
 */
public abstract class EditorSkinView<N extends GraphNode, A extends GraphNodeAdapter<N>, P extends PropertiesEditor<N, A>> extends BorderPane {

    protected final P control;

    @FXML protected Accordion accordion;
    @FXML protected TitledPane commonPropertiesPane;
    @FXML private TextField textField;
    @FXML private Spinner<Double> xSpinner;
    @FXML private Slider xSlider;
    @FXML private Spinner<Double> ySpinner;
    @FXML private Slider ySlider;
    @FXML private ComboBox<String> arabicFontFamily;
    @FXML private ComboBox<Integer> arabicFontSize;

    EditorSkinView(P control) {
        this.control = control;
        try {
            final URL fxmlURL = AppUtil.getPath(format("fxml.editor.%s.fxml", control.getClass().getSimpleName())).toUri().toURL();
            UiUtilities.loadFXML(this, fxmlURL, RESOURCE_BUNDLE);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void initialize() {
        accordion.setExpandedPane(accordion.getPanes().get(0));
        control.nodeProperty().addListener((observable, oldValue, newValue) -> initialize(newValue));
        initializeValues();
    }

    void initializeValues() {
        textField.setFont(FontConstants.ARABIC_FONT_24);
        textField.textProperty().bindBidirectional(control.textProperty());
        arabicFontFamily.getItems().addAll(Font.getFontNames());
        arabicFontFamily.getSelectionModel().selectFirst();
        arabicFontSize.getSelectionModel().selectFirst();
        arabicFontSize.setConverter(new FontSizeStringConverter());
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
        setupSpinnerSliderField(control.xProperty(), xSpinner, xSlider, true);
        setupSpinnerSliderField(control.yProperty(), ySpinner, ySlider, false);
    }

    @SuppressWarnings({"unchecked"})
    protected void initialize(A node) {
        if (node == null) {
            return;
        }
        final String labelKey = format("%s_node.label", node.getGraphNodeType().name());
        commonPropertiesPane.setText(RESOURCE_BUNDLE.getString(labelKey));
    }

    void setupSpinnerSliderField(final ObjectProperty<? extends PropertyAccessor<N, A>> property, final Spinner<Double> spinner,
                                 final Slider slider, boolean xAxis) {
        final DoubleSpinnerValueFactory valueFactory = new DoubleSpinnerValueFactory(-100, 100, 0, 0.5);
        if(xAxis){
            control.lowerXBoundProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    return;
                }
                final double min = ((Double) newValue) - 30;
                valueFactory.setMin(min);
                slider.setMin(min);
            });
            control.upperXBoundProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    return;
                }
                double max = ((Double) newValue) + 30;
                max = max > control.getCanvasWidth() ? control.getCanvasWidth() : max;
                valueFactory.setMax(max);
                slider.setMax(max);
            });
        } else {
            control.lowerYBoundProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    return;
                }
                final double min = ((Double) newValue) - 30;
                valueFactory.setMin(min);
                slider.setMin(min);
            });
            control.upperYBoundProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    return;
                }
                double max = ((Double) newValue) + 30;
                max = max > control.getCanvasHeight() ? control.getCanvasHeight() : max;
                valueFactory.setMax(max);
                slider.setMax(max);
            });
        }

        spinner.setValueFactory(valueFactory);
        property.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            double value = newValue.get();
            valueFactory.setValue(value);
            slider.setValue(value);
        });
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Double d = (Double) newValue;
            if (d % 1 != 0.0 && d % 0.5 != 0.0) {
                d = StrictMath.ceil(d);
            }
            slider.setValue(d);
            spinner.getValueFactory().setValue(d);
            property.get().set(d);
        });
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> slider.setValue(newValue));
    }

}
