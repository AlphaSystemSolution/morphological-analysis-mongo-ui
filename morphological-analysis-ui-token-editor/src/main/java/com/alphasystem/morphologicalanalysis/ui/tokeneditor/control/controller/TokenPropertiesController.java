package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationListCell;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.LabelSelectionChangeListener;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.skin.LabelSelectionModel;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper.PREFERENCES;

/**
 * @author sali
 */
@Component
public class TokenPropertiesController extends BorderPane {

    @Autowired
    private TokenPropertiesView control;
    @FXML
    private ComboBox<Location> locationComboBox;
    @FXML
    private TextArea translationArea;
    @FXML
    private HBox lettersBox;
    private final ArabicLabelToggleGroup lettersGroup = new ArabicLabelToggleGroup();
    private final List<LabelSelectionChangeListener> listeners = new ArrayList<>();

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        locationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            control.setSelectedLocation(newValue);
            final Token token = control.getToken();
            if (token != null) {
                createLettersPane(token.tokenWord(), newValue);
            }
        });
        locationComboBox.setButtonCell(new LocationListCell());
        locationComboBox.setCellFactory(param -> new LocationListCell());

        translationArea.textProperty().bindBidirectional(control.translationTextProperty());
        translationArea.setFont(PREFERENCES.getEnglishFont14());

        refresh(control.getToken());
        control.tokenProperty().addListener((observable, oldValue, newValue) -> refresh(newValue));
    }

    private void refresh(Token token) {
        locationComboBox.getItems().remove(0, locationComboBox.getItems().size());
        locationComboBox.setDisable(true);
        if (token == null) {
            translationArea.setText(null);
            createLettersPane(null, null);
            return;
        }
        final List<Location> locations = token.getLocations();
        if (locations != null && !locations.isEmpty()) {
            locationComboBox.getItems().addAll(locations.toArray(new Location[0]));
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
