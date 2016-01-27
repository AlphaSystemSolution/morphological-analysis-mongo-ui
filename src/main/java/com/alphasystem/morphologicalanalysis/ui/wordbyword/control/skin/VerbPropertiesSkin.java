package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbCategory;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.GAP;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class VerbPropertiesSkin extends AbstractPropertiesSkin<VerbProperties, VerbPropertiesView> {

    public VerbPropertiesSkin(VerbPropertiesView control) {
        super(control);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected void initializeSkin() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(GAP));

        VerbPropertiesView view = getSkinnable();
        int row = 0;

        Label label = new Label(RESOURCE_BUNDLE.getString("nounStatus.label"));
        gridPane.add(label, 0, row);

        ComboBox comboBox = COMBO_BOX_FACTORY.getVerbTypeComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.verbTypeProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("conversationType.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getConversationTypeComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.conversationTypeProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("verbMode.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getVerbModeComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.verbModeProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("numberType.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getNumberTypeComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.numberTypeProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("genderType.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getGenderTypeComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.genderTypeProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("incompleteVerbCategory.label"));
        gridPane.add(label, 0, row);

        ComboBox<IncompleteVerbCategory> incompleteVerbCategoryComboBox = COMBO_BOX_FACTORY.getIncompleteVerbCategoryComboBox();
        label.setLabelFor(incompleteVerbCategoryComboBox);
        incompleteVerbCategoryComboBox.valueProperty().bindBidirectional(view.incompleteVerbCategoryProperty());
        gridPane.add(incompleteVerbCategoryComboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("incompleteVerbType.label"));
        gridPane.add(label, 0, row);

        ComboBox incompleteVerbTypeComboBox = COMBO_BOX_FACTORY.getIncompleteVerbTypeComboBox(null);
        label.setLabelFor(incompleteVerbTypeComboBox);
        incompleteVerbTypeComboBox.valueProperty().bindBidirectional(view.incompleteVerbTypeProperty());
        gridPane.add(incompleteVerbTypeComboBox, 1, row);
        incompleteVerbTypeComboBox.setDisable(true);

        incompleteVerbCategoryComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            boolean disable = (nv == null);
            incompleteVerbTypeComboBox.getItems().clear();
            incompleteVerbTypeComboBox.setDisable(disable);
            if (!disable) {
                incompleteVerbTypeComboBox.getItems().addAll(nv.getMembers());
                gridPane.requestLayout();
            }
        });

        getChildren().add(gridPane);
    }
}
