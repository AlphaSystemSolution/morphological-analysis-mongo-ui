package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.ui.RootLettersPicker;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RootWord;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.GAP;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;

/**
 * @author sali
 */
public class CommonPropertiesSkin extends SkinBase<CommonPropertiesView> {

    private static final ComboBoxFactory COMBO_BOX_FACTORY = ComboBoxFactory.getInstance();

    public CommonPropertiesSkin(CommonPropertiesView view) {
        super(view);
        initializeSkin();
    }

    @SuppressWarnings({"unchecked"})
    private void initializeSkin() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(GAP));

        CommonPropertiesView view = getSkinnable();
        int row = 0;
        Label label = new Label(RESOURCE_BUNDLE.getString("translation.label"));
        gridPane.add(label, 0, row);

        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(5);
        textArea.setPrefColumnCount(15);
        label.setLabelFor(textArea);
        textArea.textProperty().bindBidirectional(view.translationProperty());
        gridPane.add(textArea, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("partOfSpeech.label"));
        gridPane.add(label, 0, row);

        ComboBox comboBox = COMBO_BOX_FACTORY.getPartOfSpeechComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.partOfSpeechProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("namedTag.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getNamedTagComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.namedTagProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("form.label"));
        gridPane.add(label, 0, row);

        comboBox = COMBO_BOX_FACTORY.getNamedTemplateComboBox();
        label.setLabelFor(comboBox);
        comboBox.valueProperty().bindBidirectional(view.namedTemplateProperty());
        gridPane.add(comboBox, 1, row);

        row++;
        label = new Label(RESOURCE_BUNDLE.getString("rootWords.label"));
        gridPane.add(label, 0, row);

        RootLettersPicker rootLettersPicker = new RootLettersPicker();
        RootWord rootWord = view.getRootWord();
        if (rootWord != null) {
            rootLettersPicker.setRootLetters(rootWord.getFirstRadical(), rootWord.getSecondRadical(),
                    rootWord.getThirdRadical(), rootWord.getFourthRadical());
        }
        label.setLabelFor(rootLettersPicker);
        view.rootWordProperty().addListener((o, ov, nv) -> {
            ArabicLetterType firstRadical = (nv == null) ? null : nv.getFirstRadical();
            ArabicLetterType secondRadical = (nv == null) ? null : nv.getSecondRadical();
            ArabicLetterType thirdRadical = (nv == null) ? null : nv.getThirdRadical();
            ArabicLetterType fourthRadical = (nv == null) ? null : nv.getFourthRadical();
            rootLettersPicker.setRootLetters(firstRadical, secondRadical, thirdRadical, fourthRadical);
        });
        gridPane.add(rootLettersPicker, 1, row);

        getChildren().add(gridPane);
    }
}
