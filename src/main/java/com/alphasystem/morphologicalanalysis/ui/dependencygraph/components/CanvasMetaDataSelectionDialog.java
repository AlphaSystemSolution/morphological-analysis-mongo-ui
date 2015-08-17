package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.CanvasMetaData;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import org.controlsfx.dialog.FontSelectorDialog;

import java.util.Optional;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static java.lang.String.format;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class CanvasMetaDataSelectionDialog extends Dialog<CanvasMetaData> {

    private final Spinner<Integer> widthField;
    private final Spinner<Integer> heightField;
    private final Spinner<Double> tokenWidth;
    private final Spinner<Double> tokenHeight;
    private final Spinner<Double> gapBetweenTokens;
    private FontSelectorDialog tokenFontSelectorDialog;
    private FontSelectorDialog partOfSpeechFontSelectorDialog;
    private FontSelectorDialog translationFontSelectorDialog;
    private CanvasMetaData canvasMetaData;

    public CanvasMetaDataSelectionDialog(){
        setTitle("Select Canvas Meta Properties");

        widthField = new Spinner<>();
        heightField = new Spinner<>();
        tokenWidth = new Spinner<>();
        tokenHeight = new Spinner<>();
        gapBetweenTokens = new Spinner<>();
        tokenFontSelectorDialog = new FontSelectorDialog(ARABIC_FONT_BIG);
        partOfSpeechFontSelectorDialog = new FontSelectorDialog(ARABIC_FONT_SMALL);
        translationFontSelectorDialog = new FontSelectorDialog(ENGLISH_FONT);

        canvasMetaData = new CanvasMetaData();
        getDialogPane().setContent(createMainPanel());
        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        refreshDialog();

        setResultConverter(param -> {
            if(param.getButtonData().isCancelButton()){
                canvasMetaData = null;
            }
            return canvasMetaData;
        });
    }

    private GridPane createMainPanel(){
        GridPane grid = new GridPane();
        grid.setAlignment(CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        int rowIndex = 0;
        // sets up "width" field
        Label label = new Label(RESOURCE_BUNDLE.getString("totalWidth.label"));
        grid.add(label, 0, rowIndex);

        widthField.setValueFactory(new IntegerSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, canvasMetaData.getWidth(),
                AMOUNT_STEP_BY));
        canvasMetaData.widthProperty().bind(widthField.valueProperty());
        grid.add(widthField, 1, rowIndex);

        rowIndex++;
        // sets up "height" field
        label = new Label(RESOURCE_BUNDLE.getString("totalHeight.label"));
        grid.add(label, 0, rowIndex);

        heightField.setValueFactory(new IntegerSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT, canvasMetaData.getHeight(),
                AMOUNT_STEP_BY));
        canvasMetaData.heightProperty().bind(heightField.valueProperty());
        grid.add(heightField, 1, rowIndex);

        rowIndex++;
        // sets up "tokenWidth" field
        label = new Label(RESOURCE_BUNDLE.getString("tokenWidth.label"));
        grid.add(label, 0, rowIndex);

        tokenWidth.setValueFactory(new DoubleSpinnerValueFactory(MIN_RECTANGLE_WIDTH, RECTANGLE_WIDTH,
                canvasMetaData.getTokenWidth(), 10));
        canvasMetaData.tokenWidthProperty().bind(tokenWidth.valueProperty());
        grid.add(tokenWidth, 1, rowIndex);

        rowIndex++;
        // sets up "tokenHeight" field
        label = new Label(RESOURCE_BUNDLE.getString("tokenHeight.label"));
        grid.add(label, 0, rowIndex);

        tokenHeight.setValueFactory(new DoubleSpinnerValueFactory(MIN_RECTANGLE_HEIGHT, RECTANGLE_HEIGHT,
                canvasMetaData.getTokenHeight(), 10));
        canvasMetaData.tokenHeightProperty().bind(tokenHeight.valueProperty());
        grid.add(tokenHeight, 1, rowIndex);

        rowIndex++;
        // sets up "gapBetweenTokens" field
        label = new Label(RESOURCE_BUNDLE.getString("gapBetweenTokens.label"));
        grid.add(label, 0, rowIndex);

        gapBetweenTokens.setValueFactory(new DoubleSpinnerValueFactory(MIN_GAP_BETWEEN_TOKENS, GAP_BETWEEN_TOKENS,
                canvasMetaData.getGapBetweenTokens(), 10));
        canvasMetaData.gapBetweenTokensProperty().bind(this.gapBetweenTokens.valueProperty());
        grid.add(this.gapBetweenTokens, 1, rowIndex);

        rowIndex++;
        // sets up Token Font Selector
        label = new Label(RESOURCE_BUNDLE.getString("tokenFont.label"));
        grid.add(label, 0, rowIndex);

        Button tokenButton = new Button(selectedFontString(ARABIC_FONT_BIG));
        tokenButton.setOnAction(event -> {
            final Optional<Font> result = tokenFontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                canvasMetaData.setTokenFont(font);
                tokenButton.setText(selectedFontString(font));
            });
        });
        grid.add(tokenButton, 1, rowIndex);

        rowIndex++;
        // sets part of speech font selector
        label = new Label(RESOURCE_BUNDLE.getString("partOfSpeechFont.label"));
        grid.add(label, 0, rowIndex);

        Button posButton = new Button(selectedFontString(ARABIC_FONT_SMALL));
        posButton.setOnAction(event -> {
            final Optional<Font> result = partOfSpeechFontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                canvasMetaData.setPartOfSpeechFont(font);
                posButton.setText(selectedFontString(font));
            });
        });
        grid.add(posButton, 1, rowIndex);

        rowIndex++;
        // sets translation font selector
        label = new Label(RESOURCE_BUNDLE.getString("translationFont.label"));
        grid.add(label, 0, rowIndex);

        Button translationButton = new Button(selectedFontString(ENGLISH_FONT));
        translationButton.setOnAction(event -> {
            final Optional<Font> result = translationFontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                canvasMetaData.setTranslationFont(font);
                translationButton.setText(selectedFontString(font));
            });
        });
        grid.add(translationButton, 1, rowIndex);

        return grid;
    }

    private String selectedFontString(Font font){
        return format("%s : %s", font.getFamily(), font.getSize());
    }

    public void refreshDialog(){
        widthField.getValueFactory().setValue(canvasMetaData.getWidth());
        heightField.getValueFactory().setValue(canvasMetaData.getHeight());
        tokenWidth.getValueFactory().setValue(canvasMetaData.getTokenWidth());
        tokenHeight.getValueFactory().setValue(canvasMetaData.getTokenHeight());
        gapBetweenTokens.getValueFactory().setValue(canvasMetaData.getGapBetweenTokens());
        tokenFontSelectorDialog = new FontSelectorDialog(ARABIC_FONT_BIG);
        partOfSpeechFontSelectorDialog = new FontSelectorDialog(ARABIC_FONT_SMALL);
        translationFontSelectorDialog = new FontSelectorDialog(ENGLISH_FONT);
    }
}
