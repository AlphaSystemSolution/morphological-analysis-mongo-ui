package com.alphasystem.morphologicalanalysis.ui.common;

import com.alphasystem.morphologicalanalysis.graph.model.FontMetaInfo;
import com.alphasystem.morphologicalanalysis.graph.model.GraphMetaInfo;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphMetaInfoAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
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
public class GraphMetaInfoSelectionDialog extends Dialog<GraphMetaInfoAdapter> {

    private static final int PREF_COLUMN_COUNT = 20;

    private final Spinner<Double> widthField;
    private final Spinner<Double> heightField;
    private final Spinner<Double> tokenWidthField;
    private final Spinner<Double> tokenHeightField;
    private final Spinner<Double> gapBetweenTokensField;
    private final TextField terminalFontField;
    private final TextField posFontField;
    private final TextField translationFontField;
    private final ObjectProperty<GraphMetaInfoAdapter> graphMetaInfo;
    private FontSelectorDialog fontSelectorDialog;

    public GraphMetaInfoSelectionDialog() {
        setTitle("Select Canvas Meta Properties");

        widthField = new Spinner<>();
        heightField = new Spinner<>();
        tokenWidthField = new Spinner<>();
        tokenHeightField = new Spinner<>();
        gapBetweenTokensField = new Spinner<>();

        terminalFontField = new TextField();
        terminalFontField.setEditable(false);
        terminalFontField.setPrefColumnCount(PREF_COLUMN_COUNT);

        posFontField = new TextField();
        posFontField.setEditable(false);
        posFontField.setPrefColumnCount(PREF_COLUMN_COUNT);

        translationFontField = new TextField();
        translationFontField.setEditable(false);
        translationFontField.setPrefColumnCount(PREF_COLUMN_COUNT);

        graphMetaInfo = new SimpleObjectProperty<>(new GraphMetaInfoAdapter(new GraphMetaInfo()));

        getDialogPane().setContent(createMainPanel());
        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        refreshDialog();
        setGraphMetaInfo(null);
        fontSelectorDialog = new FontSelectorDialog(fromFontMetaInfo(getGraphMetaInfo().getTerminalFont()));

        setResultConverter(param -> {
            if (param.getButtonData().isCancelButton()) {
                return null;
            }
            return getGraphMetaInfo();
        });
    }

    private static String getFontDisplayValue(FontMetaInfo fmi) {
        return format("%s, %s", fmi.getFamily(), fmi.getSize());
    }

    public final GraphMetaInfoAdapter getGraphMetaInfo() {
        return graphMetaInfo.get();
    }

    public final void setGraphMetaInfo(GraphMetaInfoAdapter graphMetaInfo) {
        graphMetaInfo = graphMetaInfo == null ? new GraphMetaInfoAdapter(new GraphMetaInfo()) : graphMetaInfo;
        this.graphMetaInfo.set(graphMetaInfo);
    }

    private GridPane createMainPanel() {
        GridPane grid = new GridPane();
        grid.setAlignment(CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        int rowIndex = 0;
        // sets up "width" field
        Label label = new Label(RESOURCE_BUNDLE.getString("totalWidth.label"));
        grid.add(label, 0, rowIndex);

        widthField.setValueFactory(new DoubleSpinnerValueFactory(MIN_WIDTH, MAX_WIDTH, graphMetaInfo.get().getWidth(),
                AMOUNT_STEP_BY));
        widthField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.get().setWidth(newValue);
        });
        grid.add(widthField, 1, rowIndex);

        rowIndex++;
        // sets up "height" field
        label = new Label(RESOURCE_BUNDLE.getString("totalHeight.label"));
        grid.add(label, 0, rowIndex);

        heightField.setValueFactory(new DoubleSpinnerValueFactory(MIN_HEIGHT, MAX_HEIGHT, graphMetaInfo.get().getHeight(),
                AMOUNT_STEP_BY));
        heightField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.get().setHeight(newValue);
        });
        grid.add(heightField, 1, rowIndex);

        rowIndex++;
        // sets up "tokenWidthField" field
        label = new Label(RESOURCE_BUNDLE.getString("tokenWidth.label"));
        grid.add(label, 0, rowIndex);

        tokenWidthField.setValueFactory(new DoubleSpinnerValueFactory(MIN_RECTANGLE_WIDTH, RECTANGLE_WIDTH,
                graphMetaInfo.get().getTokenWidth(), 10));
        tokenWidthField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.get().setTokenWidth(newValue);
        });
        grid.add(tokenWidthField, 1, rowIndex);

        rowIndex++;
        // sets up "tokenHeightField" field
        label = new Label(RESOURCE_BUNDLE.getString("tokenHeight.label"));
        grid.add(label, 0, rowIndex);

        tokenHeightField.setValueFactory(new DoubleSpinnerValueFactory(MIN_RECTANGLE_HEIGHT, RECTANGLE_HEIGHT,
                graphMetaInfo.get().getTokenHeight(), 10));
        tokenHeightField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.get().setTokenHeight(newValue);
        });
        grid.add(tokenHeightField, 1, rowIndex);

        rowIndex++;
        // sets up "gapBetweenTokensField" field
        label = new Label(RESOURCE_BUNDLE.getString("gapBetweenTokens.label"));
        grid.add(label, 0, rowIndex);

        gapBetweenTokensField.setValueFactory(new DoubleSpinnerValueFactory(MIN_GAP_BETWEEN_TOKENS, GAP_BETWEEN_TOKENS,
                graphMetaInfo.get().getGapBetweenTokens(), 10));
        this.gapBetweenTokensField.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphMetaInfo.get().setGapBetweenTokens(newValue);
        });
        grid.add(this.gapBetweenTokensField, 1, rowIndex);

        Button button;
        rowIndex++;
        // sets up "terminalFontField" field
        label = new Label(RESOURCE_BUNDLE.getString("terminalFont.label"));
        grid.add(label, 0, rowIndex);
        grid.add(terminalFontField, 1, rowIndex);
        button = new Button(" ... ");
        button.setOnAction(event -> {
            fontSelectorDialog = new FontSelectorDialog(fromFontMetaInfo(graphMetaInfo.get().getTerminalFont()));
            Optional<Font> result = fontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                FontMetaInfo fmi = fromFont(font);
                graphMetaInfo.get().setTerminalFont(fmi);
                terminalFontField.setText(getFontDisplayValue(fmi));
            });
        });
        grid.add(button, 2, rowIndex);

        rowIndex++;
        // sets up "posFontField" field
        label = new Label(RESOURCE_BUNDLE.getString("partOfSpeechFont.label"));
        grid.add(label, 0, rowIndex);
        grid.add(posFontField, 1, rowIndex);
        button = new Button(" ... ");
        button.setOnAction(event -> {
            fontSelectorDialog = new FontSelectorDialog(fromFontMetaInfo(graphMetaInfo.get().getPosFont()));
            Optional<Font> result = fontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                FontMetaInfo fmi = fromFont(font);
                graphMetaInfo.get().setPosFont(fmi);
                posFontField.setText(getFontDisplayValue(fmi));
            });
        });
        grid.add(button, 2, rowIndex);

        rowIndex++;
        // sets up "translationFontField" field
        label = new Label(RESOURCE_BUNDLE.getString("translationFont.label"));
        grid.add(label, 0, rowIndex);
        grid.add(translationFontField, 1, rowIndex);
        button = new Button(" ... ");
        button.setOnAction(event -> {
            fontSelectorDialog = new FontSelectorDialog(fromFontMetaInfo(graphMetaInfo.get().getTranslationFont()));
            Optional<Font> result = fontSelectorDialog.showAndWait();
            result.ifPresent(font -> {
                FontMetaInfo fmi = fromFont(font);
                graphMetaInfo.get().setTranslationFont(fmi);
                translationFontField.setText(getFontDisplayValue(fmi));
            });
        });
        grid.add(button, 2, rowIndex);

        return grid;
    }

    private void refreshDialog() {
        graphMetaInfo.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                widthField.getValueFactory().setValue(newValue.getWidth());
                heightField.getValueFactory().setValue(newValue.getHeight());
                tokenWidthField.getValueFactory().setValue(newValue.getTokenWidth());
                tokenHeightField.getValueFactory().setValue(newValue.getTokenHeight());
                gapBetweenTokensField.getValueFactory().setValue(newValue.getGapBetweenTokens());
                terminalFontField.setText(getFontDisplayValue(newValue.getTerminalFont()));
                posFontField.setText(getFontDisplayValue(newValue.getPosFont()));
                translationFontField.setText(getFontDisplayValue(newValue.getTranslationFont()));
            }
        });
    }
}
