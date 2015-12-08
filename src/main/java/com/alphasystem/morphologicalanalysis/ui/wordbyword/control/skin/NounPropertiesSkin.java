package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.NounPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.GAP;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class NounPropertiesSkin extends AbstractPropertiesSkin<NounProperties, NounPropertiesView> {

    public NounPropertiesSkin(NounPropertiesView view) {
        super(view);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected void initializeSkin() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(GAP));

        NounPropertiesView view = getSkinnable();
        int row = 0;

        Label label = new Label(RESOURCE_BUNDLE.getString("nounStatus.label"));
        gridPane.add(label, 0, row);

        ComboBox comboBox = COMBO_BOX_FACTORY.getNounStatusComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.nounStatusProperty());
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
        label = new Label(RESOURCE_BUNDLE.getString("nounType.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getNounTypeComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.nounTypeProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("nounKind.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getNounKindComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.nounKindProperty());
        gridPane.add(comboBox, 1, row);

        getChildren().add(gridPane);
    }
}
