package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationListCell;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper.PREFERENCES;

/**
 * @author sali
 */
public class TokenPropertiesSkin extends SkinBase<TokenPropertiesView> {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(TokenPropertiesView.class.getSimpleName());

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public TokenPropertiesSkin(TokenPropertiesView control) {
        super(control);
        getChildren().setAll(new SkinView(control));
    }

    private class SkinView extends BorderPane {

        private final TokenPropertiesView control;
        private final GridPane gridPane = new GridPane();
        private final ComboBox<Location> locationComboBox = new ComboBox<>();
        private final HBox lettersBox = new HBox();
        private final ArabicLabelToggleGroup lettersGroup = new ArabicLabelToggleGroup();
        private final List<LabelSelectionChangeListener> listeners = new ArrayList<>();

        private SkinView(TokenPropertiesView control) {
            this.control = control;
            setCenter(gridPane);
            setPadding(new Insets(20));
            initializeSkin();
            refresh(this.control.getToken());
            this.control.tokenProperty().addListener((observable, oldValue, newValue) -> refresh(newValue));
        }

        private void initializeSkin() {
            final Font labelFont = Font.font(PREFERENCES.getEnglishFontName(), FontWeight.BOLD, FontPosture.REGULAR, 14);
            locationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                control.setSelectedLocation(newValue);
                final Token token = control.getToken();
                if (token != null) {
                    createLettersPane(token.tokenWord(), newValue);
                }
            });
            locationComboBox.setDisable(true);
            locationComboBox.setButtonCell(new LocationListCell());
            locationComboBox.setCellFactory(param -> new LocationListCell());

            gridPane.setPadding(new Insets(5));
            gridPane.setHgap(5);

            Label label = new Label(RESOURCE_BUNDLE.getString("location.label"));
            label.setFont(labelFont);
            label.setMnemonicParsing(true);
            gridPane.add(label, 0, 0);
            label.setLabelFor(locationComboBox);
            gridPane.add(locationComboBox, 0, 1);

            BorderPane lettersPane = new BorderPane();
            lettersPane.setBorder(ApplicationHelper.BORDER);

            lettersBox.setSpacing(10);
            lettersBox.setPadding(new Insets(10));
            lettersBox.setAlignment(Pos.CENTER);
            lettersBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            lettersPane.setCenter(lettersBox);

            lettersGroup.setWidth(64);
            lettersGroup.setHeight(64);
            lettersGroup.setFont(PREFERENCES.getArabicFont36());
            createLettersPane(null, null);

            gridPane.add(lettersPane, 1, 0, 1, 2);

            label = new Label(RESOURCE_BUNDLE.getString("translation.label"));
            label.setFont(labelFont);
            label.setMnemonicParsing(true);
            gridPane.add(label, 2, 0);

            TextArea translationArea = new TextArea();
            translationArea.setFont(PREFERENCES.getEnglishFont14());
            translationArea.setPrefRowCount(5);
            label.setLabelFor(translationArea);
            gridPane.add(translationArea, 2, 1);
        }

        private void refresh(Token token) {
            locationComboBox.getItems().remove(0, locationComboBox.getItems().size());
            locationComboBox.setDisable(true);
            if (token == null) {
                createLettersPane(null, null);
                return;
            }
            final List<Location> locations = token.getLocations();
            if (locations != null && !locations.isEmpty()) {
                locationComboBox.getItems().addAll(locations.toArray(new Location[locations.size()]));
                locationComboBox.getSelectionModel().selectFirst();
                control.setSelectedLocation(locations.get(0));
                locationComboBox.setDisable(false);
            }
            createLettersPane(token.tokenWord(), control.getSelectedLocation());
        }

        private void createLettersPane(ArabicWord arabicWord, Location location) {
            // "LabelSelectionChangeListener" is used to detect when a particular label is getting unselected,
            // by default all the labels selected, when a particular label is getting unselected a new "Location"
            // is getting created, problem arises when new "Token" is getting selected from the list, which is firing
            // "un-select" event for the previously selected "Token", which we don't want, remove the listeners from
            // the current set of labels before clearing.
            final ObservableList<ArabicLabelView> toggles = lettersGroup.getToggles();
            if (!listeners.isEmpty()) {
                for (int i = 0; i < toggles.size(); i++) {
                    toggles.get(i).selectedProperty().removeListener(listeners.get(i));
                }
                listeners.clear();
            }
            toggles.clear();
            lettersBox.getChildren().clear();
            if (arabicWord == null || location == null) {
                // both values are null, display empty values
                for (int i = 0; i < 5; i++) {
                    ArabicLabelView arabicLabelView = new ArabicLabelView();
                    arabicLabelView.setGroup(lettersGroup);
                    arabicLabelView.setDisable(true);
                    lettersBox.getChildren().add(arabicLabelView);
                }
            } else {
                final Integer startIndex = location.getStartIndex();
                final Integer endIndex = location.getEndIndex();
                for (int i = 0; i < arabicWord.getLength(); i++) {
                    ArabicLabelView arabicLabelView = new ArabicLabelView();
                    arabicLabelView.setGroup(lettersGroup);
                    arabicLabelView.setLabel(arabicWord.getLetter(i));
                    boolean selected = (i >= startIndex) && (i < endIndex);
                    arabicLabelView.setSelect(selected);
                    arabicLabelView.setDisable(!selected);
                    lettersBox.getChildren().add(arabicLabelView);
                    LabelSelectionModel model = new LabelSelectionModel().control(control).location(location).labelView(arabicLabelView)
                            .group(lettersGroup);
                    final LabelSelectionChangeListener listener = new LabelSelectionChangeListener(model);
                    // save the reference of listener
                    listeners.add(listener);
                    arabicLabelView.selectedProperty().addListener(listener);
                }
            }
        }
    }
}
