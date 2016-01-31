package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.IncompleteVerbCategory.DUMMY;
import static java.lang.String.format;

/**
 * @author sali
 */
public class VerbPropertiesSkin extends AbstractPropertiesSkin<VerbProperties, VerbPropertiesView> {

    public VerbPropertiesSkin(VerbPropertiesView control) {
        super(control);
    }

    @Override
    protected void initializeSkin() {
        getChildren().setAll(new SkinView());
    }

    private class SkinView extends BorderPane {

        @FXML
        private ComboBox<VerbType> verbTypeComboBox;

        @FXML
        private ComboBox<ConversationType> conversationTypeComboBox;

        @FXML
        private ComboBox<NumberType> numberTypeComboBox;

        @FXML
        private ComboBox<GenderType> genderTypeComboBox;

        @FXML
        private ComboBox<VerbMode> verbModeComboBox;

        @FXML
        private ComboBox<IncompleteVerbCategory> incompleteVerbCategoryComboBox;

        @FXML
        private ComboBox<IncompleteVerbType> incompleteVerbTypeComboBox;

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
            final VerbPropertiesView view = getSkinnable();
            verbTypeComboBox.valueProperty().bindBidirectional(view.verbTypeProperty());
            conversationTypeComboBox.valueProperty().bindBidirectional(view.conversationTypeProperty());
            numberTypeComboBox.valueProperty().bindBidirectional(view.numberTypeProperty());
            genderTypeComboBox.valueProperty().bindBidirectional(view.genderTypeProperty());
            verbModeComboBox.valueProperty().bindBidirectional(view.verbModeProperty());
            incompleteVerbCategoryComboBox.valueProperty().bindBidirectional(view.incompleteVerbCategoryProperty());
            incompleteVerbTypeComboBox.valueProperty().bindBidirectional(view.incompleteVerbTypeProperty());
            incompleteVerbTypeComboBox.setDisable(true);
            incompleteVerbCategoryComboBox.valueProperty().addListener((o, ov, nv) -> {
                boolean disable = (nv == null) || nv.equals(DUMMY);
                incompleteVerbTypeComboBox.getItems().clear();
                incompleteVerbTypeComboBox.setDisable(disable);
                if (!disable) {
                    incompleteVerbTypeComboBox.getItems().addAll(nv.getMembers());
                }
            });
        }
    }
}
