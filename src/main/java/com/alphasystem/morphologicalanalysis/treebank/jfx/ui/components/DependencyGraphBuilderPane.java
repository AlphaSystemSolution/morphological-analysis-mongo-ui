package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.components;

import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.GraphNode;
import com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.TerminalNode;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ResourceBundle;

import static java.lang.String.format;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.layout.BorderStrokeStyle.NONE;
import static javafx.scene.text.Font.font;
import static javafx.scene.text.FontPosture.REGULAR;
import static javafx.scene.text.FontWeight.BOLD;
import static javafx.scene.text.FontWeight.NORMAL;

/**
 * @author sali
 */
public class DependencyGraphBuilderPane extends Pane {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("resources");

    private GridPane editorPane;

    public DependencyGraphBuilderPane(List<GraphNode> nodes) {
        VBox vBox = new VBox(10);
        vBox.setAlignment(CENTER);
        vBox.setPadding(new Insets(5, 5, 5, 5));

        editorPane = new GridPane();
        editorPane.setAlignment(CENTER);
        editorPane.setHgap(10);
        editorPane.setVgap(10);
        editorPane.setPadding(new Insets(25, 25, 25, 25));

        GraphNode graphNode = new TerminalNode();
        if (!nodes.isEmpty()) {
            graphNode = nodes.get(0);
        }
        initEditorPane(graphNode);
        vBox.getChildren().addAll(initTreePane(), editorPane);

        getChildren().add(new TitledPane("Dependency Graph Builder", vBox));

        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    private void initEditorPane(GraphNode node) {
        ObservableList<Node> children = editorPane.getChildren();
        children.remove(0, children.size());

        addTextControls(node);

        BorderStroke borderStroke = new BorderStroke(Color.BLACK, null, null, null,
                BorderStrokeStyle.SOLID, NONE, NONE, NONE, null,
                BorderWidths.DEFAULT, new Insets(3, 3, 3, 3));
        editorPane.setBorder(new Border(borderStroke));
        editorPane.requestLayout();
    }

    private void addTextControls(GraphNode node) {
        Label label = new Label(getPaneTitle(node));
        label.setFont(font("Georgia", BOLD, REGULAR, 16));
        label.setUnderline(true);
        label.setTextFill(Color.BLUE);
        editorPane.add(label, 0, 0, 2, 1);

        label = new Label(RESOURCE_BUNDLE.getString("text.label"));
        editorPane.add(label, 0, 1);
        TextField textField = new TextField();
        textField.setFont(font("Arabic Typesetting", NORMAL, REGULAR, 20));
        label.setLabelFor(textField);
        editorPane.add(textField, 1, 1);


    }

    private Spinner<Double> getSpinner(double min, double max, double initialValue, double amountToStepBy) {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max,
                initialValue, amountToStepBy));
        return spinner;
    }

    private String getPaneTitle(GraphNode node) {
        return RESOURCE_BUNDLE.getString(format("%s_node.label", node.getNodeType().name()));
    }

    private Pane initTreePane() {

        return new Pane();
    }
}
