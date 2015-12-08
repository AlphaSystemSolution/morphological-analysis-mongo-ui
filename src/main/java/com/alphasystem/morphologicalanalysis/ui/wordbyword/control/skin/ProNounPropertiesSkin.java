package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ProNounProperties;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.GAP;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class ProNounPropertiesSkin extends AbstractPropertiesSkin<ProNounProperties, ProNounPropertiesView> {

    public ProNounPropertiesSkin(ProNounPropertiesView view) {
        super(view);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected void initializeSkin() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(GAP));

        ProNounPropertiesView view = getSkinnable();
        int row = 0;

        Label label = new Label(RESOURCE_BUNDLE.getString("proNounType.label"));
        gridPane.add(label, 0, row);

        ComboBox comboBox = COMBO_BOX_FACTORY.getProNounTypeComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.proNounTypeProperty());
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
        label = new Label(RESOURCE_BUNDLE.getString("conversationType.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getConversationTypeComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.conversationTypeProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("nounStatus.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getNounStatusComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.nounStatusProperty());
        gridPane.add(comboBox, 1, row);

        getChildren().add(gridPane);
    }
}
