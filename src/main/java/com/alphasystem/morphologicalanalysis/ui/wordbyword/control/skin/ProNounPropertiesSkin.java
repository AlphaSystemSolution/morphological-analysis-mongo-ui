package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ProNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static java.lang.String.format;

/**
 * @author sali
 */
public class ProNounPropertiesSkin extends AbstractPropertiesSkin<ProNounProperties, ProNounPropertiesView> {

    public ProNounPropertiesSkin(ProNounPropertiesView view) {
        super(view);
    }

    @Override
    protected void initializeSkin() {
        getChildren().setAll(new SkinView());
    }

    private class SkinView extends BorderPane {

        @FXML
        private ComboBox<ProNounType> proNounTypeComboBox;

        @FXML
        private ComboBox<NounStatus> nounStatusComboBox;

        @FXML
        private ComboBox<NumberType> numberTypeComboBox;

        @FXML
        private ComboBox<GenderType> genderTypeComboBox;

        @FXML
        private ComboBox<ConversationType> conversationTypeComboBox;

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
            final ProNounPropertiesView view = getSkinnable();
            proNounTypeComboBox.valueProperty().bindBidirectional(view.proNounTypeProperty());
            nounStatusComboBox.valueProperty().bindBidirectional(view.nounStatusProperty());
            numberTypeComboBox.valueProperty().bindBidirectional(view.numberTypeProperty());
            genderTypeComboBox.valueProperty().bindBidirectional(view.genderTypeProperty());
            conversationTypeComboBox.valueProperty().bindBidirectional(view.conversationTypeProperty());
        }
    }
}
