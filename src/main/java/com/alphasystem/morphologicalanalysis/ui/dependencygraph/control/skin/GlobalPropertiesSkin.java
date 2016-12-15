package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.skin;

import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.GlobalPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.FontSizeStringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.util.AppUtil.getPath;

/**
 * @author sali
 */
public class GlobalPropertiesSkin extends SkinBase<GlobalPropertiesView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public GlobalPropertiesSkin(GlobalPropertiesView control) {
        super(control);
        getChildren().setAll(new SkinView());
    }

    private class SkinView extends BorderPane {

        @FXML
        private Accordion accordion;

        @FXML
        private Spinner<Double> widthSpinner;

        @FXML
        private Spinner<Double> heightSpinner;

        @FXML
        private CheckBox showGridLines;

        @FXML
        private CheckBox showOutline;

        @FXML
        private CheckBox debugMode;

        @FXML
        private Spinner<Double> alignTerminalYAxisSpinner;

        @FXML
        private Spinner<Double> alignTranslationsYAxisSpinner;

        @FXML
        private Spinner<Double> alignPosYAxisSpinner;

        @FXML
        private Spinner<Double> alignPOSControlYAxisSpinner;

        @FXML
        private Spinner<Double> groupTranslateXAxisSpinner;

        @FXML
        private Slider groupTranslateXAxisSlider;

        @FXML
        private Spinner<Double> groupTranslateYAxisSpinner;

        @FXML
        private ComboBox<String> terminalFontFamily;

        @FXML
        private ComboBox<Integer> terminalFontSize;

        @FXML
        private ComboBox<String> translationFontFamily;

        @FXML
        private ComboBox<Integer> translationFontSize;

        @FXML
        private ComboBox<String> partOfSpeechFontFamily;

        @FXML
        private ComboBox<Integer> partOfSpeechFontSize;

        @FXML
        private ColorPicker colorPicker;

        private SkinView() {
            init();
        }

        private void init() {
            try {
                loadFXML(this, getPath("fxml.GlobalProperties.fxml").toUri().toURL(), RESOURCE_BUNDLE);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        void initialize() {
            accordion.setExpandedPane(accordion.getPanes().get(0));
            final GlobalPropertiesView control = getSkinnable();
            colorPicker.setValue((Color) control.getBackgroundColor());
            colorPicker.setOnAction(event1 -> {
                control.setBackgroundColor(colorPicker.getValue());
            });

            setupCanvasWidth(control);
            setupCanvasHeight(control);
            setupShowGridLines(control);
            setupShowOutline(control);
            setupDebugMode(control);
            setupBackgroundColor(control);
            setupAlignTerminalYAxis(control);
            setupAlignTranslationsYAxis(control);
            setupAlignPosYAxis(control);
            setupAlignPOSControlYAxis(control);
            setupGroupTranslateXAxis(control);
            setupGroupTranslateYAxis(control);
            setupTerminalFont(control);
            setupTranslationFont(control);
            setupPartOfSpeechFont(control);
        }

        private void setupCanvasWidth(GlobalPropertiesView control) {
            widthSpinner.setValueFactory(new DoubleSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, control.getCanvasWidth(), 20.0));
            control.canvasWidthProperty().addListener((observable, oldValue, newValue) -> {
                final Double value = (Double) newValue;
                widthSpinner.getValueFactory().setValue(value);
                groupTranslateXAxisSlider.setMax(value);
            });
            widthSpinner.valueProperty().addListener((observable, oldValue, newValue) -> control.setCanvasWidth(newValue));
        }

        private void setupCanvasHeight(GlobalPropertiesView control) {
            heightSpinner.setValueFactory(new DoubleSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, control.getCanvasHeight(), 20.0));
            control.canvasHeightProperty().addListener((observable, oldValue, newValue) -> heightSpinner.getValueFactory().setValue((Double) newValue));
            heightSpinner.valueProperty().addListener((observable, oldValue, newValue) -> control.setCanvasHeight(newValue));
        }

        private void setupShowGridLines(GlobalPropertiesView control) {
            showGridLines.selectedProperty().setValue(control.isShowGridLines());
            showGridLines.selectedProperty().bindBidirectional(control.showGridLinesProperty());
        }

        private void setupShowOutline(GlobalPropertiesView control) {
            showOutline.selectedProperty().setValue(control.isShowOutline());
            showOutline.selectedProperty().bindBidirectional(control.showOutlineProperty());
        }

        private void setupDebugMode(GlobalPropertiesView control) {
            debugMode.selectedProperty().setValue(control.isDebugMode());
            debugMode.selectedProperty().bindBidirectional(control.debugModeProperty());
        }

        private void setupBackgroundColor(GlobalPropertiesView control) {
            colorPicker.setValue((Color) control.getBackgroundColor());
        }

        private void setupAlignTerminalYAxis(GlobalPropertiesView control) {
            alignTerminalYAxisSpinner.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
            control.alignTerminalYAxisProperty().addListener((observable, oldValue, newValue) ->
                    alignTerminalYAxisSpinner.getValueFactory().setValue((Double) newValue));
            alignTerminalYAxisSpinner.valueProperty().addListener((observable, oldValue, newValue) -> control.setAlignTerminalYAxis(newValue));
        }

        void setupAlignTranslationsYAxis(GlobalPropertiesView control) {
            alignTranslationsYAxisSpinner.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
            control.alignTranslationYAxisProperty().addListener((observable, oldValue, newValue) ->
                    alignTranslationsYAxisSpinner.getValueFactory().setValue((Double) newValue));
            alignTranslationsYAxisSpinner.valueProperty().addListener((observable, oldValue, newValue) -> control.setAlignTranslationYAxis(newValue));
        }

        private void setupAlignPosYAxis(GlobalPropertiesView control) {
            alignPosYAxisSpinner.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
            control.alignPosYAxisProperty().addListener((observable, oldValue, newValue) ->
                    alignPosYAxisSpinner.getValueFactory().setValue((Double) newValue));
            alignPosYAxisSpinner.valueProperty().addListener((observable, oldValue, newValue) -> control.setAlignPosYAxis(newValue));
        }

        private void setupAlignPOSControlYAxis(GlobalPropertiesView control) {
            alignPOSControlYAxisSpinner.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
            control.alignPOSControlYAxisProperty().addListener((observable, oldValue, newValue) ->
                    alignPOSControlYAxisSpinner.getValueFactory().setValue((Double) newValue));
            alignPOSControlYAxisSpinner.valueProperty().addListener((observable, oldValue, newValue) -> control.setAlignPOSControlYAxis(newValue));
        }

        private void setupGroupTranslateXAxis(GlobalPropertiesView control) {
            groupTranslateXAxisSpinner.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_WIDTH, 0, 0.5));
            groupTranslateXAxisSlider.setMin(0);
            control.groupTranslateXAxisProperty().addListener((observable, oldValue, newValue) -> {
                final Double value = (Double) newValue;
                groupTranslateXAxisSpinner.getValueFactory().setValue(value);
                groupTranslateXAxisSlider.setValue(value);
            });
            groupTranslateXAxisSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                control.setGroupTranslateXAxis(newValue);
                groupTranslateXAxisSlider.setValue(newValue);
            });
            groupTranslateXAxisSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                Double d = (Double) newValue;
                if (d % 1 != 0.0 && d % 0.5 != 0.0) {
                    d = StrictMath.ceil(d);
                }
                groupTranslateXAxisSlider.setValue(d);
                groupTranslateXAxisSpinner.getValueFactory().setValue(d);
            });
        }

        private void setupGroupTranslateYAxis(GlobalPropertiesView control) {
            groupTranslateYAxisSpinner.setValueFactory(new DoubleSpinnerValueFactory(-100.0, control.getCanvasHeight(), 0, 0.5));
            control.groupTranslateYAxisProperty().addListener((observable, oldValue, newValue) ->
                    groupTranslateYAxisSpinner.getValueFactory().setValue((Double) newValue));
            groupTranslateYAxisSpinner.valueProperty().addListener((observable, oldValue, newValue) -> control.setGroupTranslateYAxis(newValue));
        }

        private void setupTranslationFont(GlobalPropertiesView control) {
            translationFontFamily.getItems().addAll(Font.getFontNames());
            translationFontFamily.getSelectionModel().selectFirst();
            translationFontSize.getSelectionModel().selectFirst();
            translationFontSize.setConverter(new FontSizeStringConverter());
            control.translationFontProperty().addListener((observable, oldValue, newValue) -> {
                translationFontFamily.getSelectionModel().select(newValue.getFamily());
                translationFontSize.getSelectionModel().select(new Integer(new Double(newValue.getSize()).intValue()));
            });
            translationFontFamily.valueProperty().addListener((observable, oldValue, newValue) ->
                    control.setTranslationFont(deriveFromFamily(control.getTranslationFont(), newValue)));
            translationFontSize.valueProperty().addListener((observable, oldValue, newValue) ->
                    control.setTranslationFont(deriveFromSize(control.getTranslationFont(), newValue)));
        }

        private void setupTerminalFont(GlobalPropertiesView control) {
            terminalFontFamily.getItems().addAll(Font.getFontNames());
            terminalFontFamily.getSelectionModel().selectFirst();
            terminalFontSize.getSelectionModel().selectFirst();
            terminalFontSize.setConverter(new FontSizeStringConverter());
            control.terminalFontProperty().addListener((observable, oldValue, newValue) -> {
                terminalFontFamily.getSelectionModel().select(newValue.getFamily());
                terminalFontSize.getSelectionModel().select(new Integer(new Double(newValue.getSize()).intValue()));
            });
            terminalFontFamily.valueProperty().addListener((observable, oldValue, newValue) ->
                    control.setTerminalFont(deriveFromFamily(control.getTerminalFont(), newValue)));
            terminalFontSize.valueProperty().addListener((observable, oldValue, newValue) ->
                    control.setTerminalFont(deriveFromSize(control.getTerminalFont(), newValue)));
        }

        private void setupPartOfSpeechFont(GlobalPropertiesView control) {
            partOfSpeechFontFamily.getItems().addAll(Font.getFontNames());
            partOfSpeechFontFamily.getSelectionModel().selectFirst();
            partOfSpeechFontSize.getSelectionModel().selectFirst();
            partOfSpeechFontSize.setConverter(new FontSizeStringConverter());
            control.posFontProperty().addListener((observable, oldValue, newValue) -> {
                partOfSpeechFontFamily.getSelectionModel().select(newValue.getFamily());
                partOfSpeechFontSize.getSelectionModel().select(new Integer(new Double(newValue.getSize()).intValue()));
            });
            partOfSpeechFontFamily.valueProperty().addListener((observable, oldValue, newValue) ->
                    control.setPosFont(deriveFromFamily(control.getPosFont(), newValue)));
            partOfSpeechFontSize.valueProperty().addListener((observable, oldValue, newValue) ->
                    control.setPosFont(deriveFromSize(control.getPosFont(), newValue)));
        }

    }

    private static FontMetaInfo deriveFromFamily(FontMetaInfo src, String family) {
        return new FontMetaInfo(family, src.getWeight(), src.getPosture(), src.getSize());
    }

    private static FontMetaInfo deriveFromSize(FontMetaInfo src, double size) {
        return new FontMetaInfo(src.getFamily(), src.getWeight(), src.getPosture(), size);
    }

}
