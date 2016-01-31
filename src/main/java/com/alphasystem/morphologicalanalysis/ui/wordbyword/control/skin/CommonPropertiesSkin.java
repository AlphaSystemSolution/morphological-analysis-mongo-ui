package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static java.lang.String.format;

/**
 * @author sali
 */
public class CommonPropertiesSkin extends SkinBase<CommonPropertiesView> {

    public CommonPropertiesSkin(CommonPropertiesView view) {
        super(view);
        getChildren().setAll(new SkinView());
    }

    private class SkinView extends BorderPane {

        @FXML
        private TextArea translationField;

        @FXML
        private ComboBox<PartOfSpeech> partOfSpeechComboBox;

        @FXML
        private ComboBox<NamedTag> namedTagComboBox;

        private SkinView() {
            init();
        }

        private void init() {
            URL fxmlURL = getClass().getResource(format("/fxml/%s.fxml",
                    getSkinnable().getClass().getSimpleName()));
            try {
                loadFXML(this, fxmlURL, RESOURCE_BUNDLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        void initialize() {
            final CommonPropertiesView view = getSkinnable();
            translationField.textProperty().bindBidirectional(view.translationProperty());
            partOfSpeechComboBox.valueProperty().bindBidirectional(view.partOfSpeechProperty());
            namedTagComboBox.valueProperty().bindBidirectional(view.namedTagProperty());
        }
    }
}
