package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.CanvasMetaData;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import org.controlsfx.dialog.FontSelectorDialog;

import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static java.lang.String.format;
import static javafx.geometry.Pos.CENTER;

/**
 * @author sali
 */
public class PropertiesPane extends BorderPane {

    private Spinner<Integer> widthField;
    private Spinner<Integer> heightField;
    private Spinner<Double> tokenWidth;
    private Spinner<Double> tokenHeight;
    private Spinner<Double> gapBetweenTokens;
    private CheckBox showOutlineOnlyCheckBox;
    private CheckBox showGridLinesCheckBox;
    private CheckBox debugModeCheckBox;
    private FontSelectorDialog tokenFontSelectorDialog;
    private FontSelectorDialog partOfSpeechFontSelectorDialog;
    private FontSelectorDialog translationFontSelectorDialog;
    private Spinner<Double> alignTokensYField;
    private Spinner<Double> alignTranslationsY;
    private Spinner<Double> alignPOSsYField;
    private Spinner<Double> alignPOSControlYField;

    private CanvasMetaData metaData;

    public PropertiesPane(CanvasMetaData metaData) {

        this.metaData = metaData;
        tokenFontSelectorDialog = new FontSelectorDialog(metaData.getTokenFont());
        partOfSpeechFontSelectorDialog = new FontSelectorDialog(metaData.getPartOfSpeechFont());
        translationFontSelectorDialog = new FontSelectorDialog(metaData.getTranslationFont());

        setCenter(initMetaProperties());

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    private GridPane initMetaProperties() {
        int width = metaData.getWidth();
        int height = metaData.getHeight();
        double _tokenWidth = metaData.getTokenWidth();
        double _tokenHeight = metaData.getTokenHeight();
        double gapBetweenTokens = metaData.getGapBetweenTokens();

        GridPane grid = new GridPane();
        grid.setAlignment(CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        int rowIndex = 0;
        // sets up "width" field
        Label label = new Label(RESOURCE_BUNDLE.getString("totalWidth.label"));
        grid.add(label, 0, rowIndex);

        widthField = new Spinner<>();
        widthField.setValueFactory(new IntegerSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH,
                width, 20));
        metaData.widthProperty().bind(widthField.valueProperty());
        grid.add(widthField, 1, rowIndex);

        rowIndex++;
        // sets up "height" field
        label = new Label(RESOURCE_BUNDLE.getString("totalHeight.label"));
        grid.add(label, 0, rowIndex);

        heightField = new Spinner<>();
        heightField.setValueFactory(new IntegerSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT, height, 20));
        metaData.heightProperty().bind(heightField.valueProperty());
        grid.add(heightField, 1, rowIndex);

        rowIndex++;
        // sets up "tokenWidth" field
        label = new Label(RESOURCE_BUNDLE.getString("tokenWidth.label"));
        grid.add(label, 0, rowIndex);

        tokenWidth = new Spinner<>();
        tokenWidth.setValueFactory(new DoubleSpinnerValueFactory(60, RECTANGLE_WIDTH, _tokenWidth, 10));
        metaData.tokenWidthProperty().bind(tokenWidth.valueProperty());
        grid.add(tokenWidth, 1, rowIndex);

        rowIndex++;
        // sets up "tokenHeight" field
        label = new Label(RESOURCE_BUNDLE.getString("tokenHeight.label"));
        grid.add(label, 0, rowIndex);

        tokenHeight = new Spinner<>();
        tokenHeight.setValueFactory(new DoubleSpinnerValueFactory(60, RECTANGLE_HEIGHT, _tokenHeight, 10));
        metaData.tokenHeightProperty().bind(tokenHeight.valueProperty());
        grid.add(tokenHeight, 1, rowIndex);

        rowIndex++;
        // sets up "gapBetweenTokens" field
        label = new Label(RESOURCE_BUNDLE.getString("gapBetweenTokens.label"));
        grid.add(label, 0, rowIndex);

        this.gapBetweenTokens = new Spinner<>();
        this.gapBetweenTokens.setValueFactory(new DoubleSpinnerValueFactory(10, GAP_BETWEEN_TOKENS,
                gapBetweenTokens, 10));
        metaData.gapBetweenTokensProperty().bind(this.gapBetweenTokens.valueProperty());
        grid.add(this.gapBetweenTokens, 1, rowIndex);

        rowIndex++;
        // sets up "show grid lines" field
        label = new Label(RESOURCE_BUNDLE.getString("showGridLines.label"));
        grid.add(label, 0, rowIndex);

        showGridLinesCheckBox = new CheckBox();
        showGridLinesCheckBox.setSelected(metaData.isShowGridLines());
        metaData.showGridLinesProperty().bind(showGridLinesCheckBox.selectedProperty());
        showGridLinesCheckBox.setOnAction(event -> {
            boolean selected = showGridLinesCheckBox.isSelected();
            showOutlineOnlyCheckBox.setSelected(selected);
        });
        grid.add(showGridLinesCheckBox, 1, rowIndex);

        rowIndex++;
        // sets up "show out lines" field
        label = new Label(RESOURCE_BUNDLE.getString("showOutlineOnly.label"));
        grid.add(label, 0, rowIndex);

        showOutlineOnlyCheckBox = new CheckBox();
        showOutlineOnlyCheckBox.setSelected(metaData.isShowOutLines());
        metaData.showOutLinesProperty().bind(showOutlineOnlyCheckBox.selectedProperty());
        grid.add(showOutlineOnlyCheckBox, 1, rowIndex);

        showOutlineOnlyCheckBox.disableProperty().bind(showGridLinesCheckBox.selectedProperty());

        rowIndex++;
        // sets up "debug mode" field
        label = new Label(RESOURCE_BUNDLE.getString("debugMode.label"));
        grid.add(label, 0, rowIndex);

        debugModeCheckBox = new CheckBox();
        debugModeCheckBox.setSelected(metaData.isDebugMode());
        metaData.debugModeProperty().bind(debugModeCheckBox.selectedProperty());
        grid.add(debugModeCheckBox, 1, rowIndex);

        rowIndex++;
        // sets up Token Font Selector
        label = new Label(RESOURCE_BUNDLE.getString("tokenFont.label"));
        grid.add(label, 0, rowIndex);

        Button tokenButton = new Button(selectedFontString(metaData.getTokenFont()));
        tokenButton.setOnAction(event -> {
            final Optional<Font> result = tokenFontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                metaData.setTokenFont(font);
                tokenButton.setText(selectedFontString(font));
            });
        });
        grid.add(tokenButton, 1, rowIndex);

        rowIndex++;
        // sets part of speech font selector
        label = new Label(RESOURCE_BUNDLE.getString("partOfSpeechFont.label"));
        grid.add(label, 0, rowIndex);

        Button posButton = new Button(selectedFontString(metaData.getPartOfSpeechFont()));
        posButton.setOnAction(event -> {
            final Optional<Font> result = partOfSpeechFontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                metaData.setPartOfSpeechFont(font);
                posButton.setText(selectedFontString(font));
            });
        });
        grid.add(posButton, 1, rowIndex);

        rowIndex++;
        // sets translation font selector
        label = new Label(RESOURCE_BUNDLE.getString("translationFont.label"));
        grid.add(label, 0, rowIndex);

        Button translationButton = new Button(selectedFontString(metaData.getTranslationFont()));
        translationButton.setOnAction(event -> {
            final Optional<Font> result = translationFontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                metaData.setTranslationFont(font);
                translationButton.setText(selectedFontString(font));
            });
        });
        grid.add(translationButton, 1, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignTokensY.label"));
        grid.add(label, 0, rowIndex);

        alignTokensYField = new Spinner<>();
        alignTokensYField.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
        grid.add(alignTokensYField, 1, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignTranslationsY.label"));
        grid.add(label, 0, rowIndex);

        alignTranslationsY = new Spinner<>();
        alignTranslationsY.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
        grid.add(alignTranslationsY, 1, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignPOSY.label"));
        grid.add(label, 0, rowIndex);

        alignPOSsYField = new Spinner<>();
        alignPOSsYField.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
        grid.add(alignPOSsYField, 1, rowIndex);

        rowIndex++;
        label = new Label(RESOURCE_BUNDLE.getString("alignPOSYControl.label"));
        grid.add(label, 0, rowIndex);

        alignPOSControlYField = new Spinner<>();
        alignPOSControlYField.setValueFactory(new DoubleSpinnerValueFactory(0, MAX_HEIGHT, 0, 0.5));
        grid.add(alignPOSControlYField, 1, rowIndex);

        return grid;
    }

    private String selectedFontString(Font font){
        return format("%s : %s", font.getFamily(), font.getSize());
    }

    public Spinner<Integer> getHeightField() {
        return heightField;
    }

    public Spinner<Integer> getWidthField() {
        return widthField;
    }

    public Spinner<Double> getAlignTokensYField() {
        return alignTokensYField;
    }

    public Spinner<Double> getAlignTranslationsY() {
        return alignTranslationsY;
    }

    public Spinner<Double> getAlignPOSsYField() {
        return alignPOSsYField;
    }

    public Spinner<Double> getAlignPOSControlYField() {
        return alignPOSControlYField;
    }
}
