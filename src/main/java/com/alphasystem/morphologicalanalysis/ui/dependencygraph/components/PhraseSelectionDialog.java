package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.Fragment;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.Global;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.TerminalNode;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_BIG;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class PhraseSelectionDialog extends Dialog<Fragment> {

    private final ComboBox<RelationshipType> comboBox;
    private final ObservableList<TerminalNode> nodes;
    private final Label phraseLabel;

    public PhraseSelectionDialog() {
        setTitle(getLabel("title"));

        nodes = observableArrayList();
        comboBox = ComboBoxFactory.getInstance().getRelationshipTypeComboBox();
        phraseLabel = new Label();
        phraseLabel.setFont(ARABIC_FONT_BIG);

        nodes.addListener((ListChangeListener<TerminalNode>) c -> {
            phraseLabel.setText("");
            c.next();
            if (c.wasAdded()) {
                ObservableList<? extends TerminalNode> list = c.getList();
                StringBuilder builder = new StringBuilder();
                list.forEach(tn -> builder.append(tn.getText()).append(" "));
                phraseLabel.setText(builder.toString());
            }
        });

        initDialogPane();
        setResultConverter(param -> {
            ButtonBar.ButtonData buttonData = param.getButtonData();
            Fragment result = null;
            if (!buttonData.isCancelButton()) {
                result = new Fragment();
                result.setRelationshipType(comboBox.getValue());
                List<Token> tokens = new ArrayList<>();
                nodes.forEach(terminalNode -> tokens.add(terminalNode.getToken()));
                result.setTokens(tokens);
            }
            return result;
        });
    }

    private static String getLabel(String label) {
        return Global.getLabel(PhraseSelectionDialog.class, label);
    }

    private void initDialogPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(phraseLabel);
        gridPane.add(borderPane, 0, 0, 1, 2);

        Label label = new Label(getLabel("comboBox"));
        gridPane.add(label, 0, 2);
        gridPane.add(comboBox, 1, 2);

        getDialogPane().getButtonTypes().addAll(OK, CANCEL);

        Button okButton = (Button) getDialogPane().lookupButton(OK);
        okButton.disableProperty().bind(comboBox.getSelectionModel().selectedIndexProperty().isEqualTo(0));

        getDialogPane().setContent(gridPane);
        getDialogPane().setPrefWidth(400);
    }

    public void setNodes(List<TerminalNode> srcNodes) {
        nodes.remove(0, nodes.size());
        nodes.addAll(srcNodes);
    }

    public void reset() {
        nodes.remove(0, nodes.size());
    }
}
