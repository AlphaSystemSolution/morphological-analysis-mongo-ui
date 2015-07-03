package com.alphasystem.morphologicalanalysis.ui.dependencygraph.components;

import com.alphasystem.morphologicalanalysis.graph.model.Relationship;
import com.alphasystem.morphologicalanalysis.graph.repository.RelationshipRepository;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.Global;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RelationshipType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_SMALL_BOLD;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * @author sali
 */
public class RelationshipSelectionDialog extends Dialog<Relationship> {

    private final ObjectProperty<LinkSupport> dependentNode = new SimpleObjectProperty<>();
    private final ObjectProperty<LinkSupport> ownerNode = new SimpleObjectProperty<>();
    private final Label dependentLabel;
    private final Label ownerLabel;
    private final ComboBox<RelationshipType> comboBox;
    private final RelationshipRepository relationshipRepository = RepositoryTool.getInstance()
            .getRepositoryUtil().getRelationshipRepository();

    public RelationshipSelectionDialog() {
        setTitle(getLabel("title"));
        comboBox = ComboBoxFactory.getInstance().getRelationshipTypeComboBox();
        dependentLabel = new Label();
        dependentLabel.setFont(ARABIC_FONT_SMALL_BOLD);
        ownerLabel = new Label();
        ownerLabel.setFont(ARABIC_FONT_SMALL_BOLD);
        reset();
        initDialogPane();
        dependentNodeProperty().addListener((observable, oldValue, newValue) ->
                dependentLabel.setText(newValue.getText()));
        ownerNodeProperty().addListener((observable, oldValue, newValue) ->
                ownerLabel.setText(newValue.getText()));
    }

    private static String getLabel(String label) {
        return Global.getLabel(RelationshipSelectionDialog.class, label);
    }

    public final LinkSupport getDependentNode() {
        return dependentNode.get();
    }

    public final void setDependentNode(LinkSupport dependentNode) {
        this.dependentNode.set(dependentNode);
    }

    public final ObjectProperty<LinkSupport> dependentNodeProperty() {
        return dependentNode;
    }

    public final LinkSupport getOwnerNode() {
        return ownerNode.get();
    }

    public final void setOwnerNode(LinkSupport ownerNode) {
        this.ownerNode.set(ownerNode);
    }

    public final ObjectProperty<LinkSupport> ownerNodeProperty() {
        return ownerNode;
    }

    private void initDialogPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label(getLabel("dependent"));
        gridPane.add(label, 0, 0);
        gridPane.add(dependentLabel, 1, 0);

        label = new Label(getLabel("owner"));
        gridPane.add(label, 0, 1);
        gridPane.add(ownerLabel, 1, 1);

        label = new Label(getLabel("comboBox"));
        gridPane.add(label, 0, 2);
        gridPane.add(comboBox, 1, 2);

        setResultConverter(param -> {
            ButtonBar.ButtonData buttonData = param.getButtonData();
            Relationship result = null;
            Relationship relationship = null;
            if (!buttonData.isCancelButton()) {
                relationship = new Relationship();
                relationship.setDependent(getDependentNode().getRelated());
                relationship.setOwner(getOwnerNode().getRelated());
                relationship.setRelationship(comboBox.getSelectionModel().getSelectedItem());
                result = relationshipRepository.findByDisplayName(relationship.getDisplayName());
                if(result == null){
                    result = relationship;
                }
            }
            return result;
        });
        getDialogPane().getButtonTypes().addAll(OK, CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(OK);
        okButton.disableProperty().bind(comboBox.getSelectionModel().selectedIndexProperty().isEqualTo(0));
        getDialogPane().setContent(gridPane);
        getDialogPane().setPrefWidth(400);
    }


    public void reset() {
        comboBox.getSelectionModel().select(0);
    }

}