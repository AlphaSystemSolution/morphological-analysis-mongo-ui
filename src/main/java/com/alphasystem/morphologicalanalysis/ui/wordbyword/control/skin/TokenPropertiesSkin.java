package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.arabic.ui.Browser;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.common.LocationListCell;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.alphasystem.arabic.ui.util.FontConstants.ARABIC_FONT_36;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView.SelectionStatus.*;
import static com.alphasystem.util.AppUtil.isGivenType;
import static java.lang.String.format;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

/**
 * @author sali
 */
public class TokenPropertiesSkin extends SkinBase<TokenPropertiesView> {

    private final ComboBox<Location> locationComboBox;
    private final ArabicLabelToggleGroup group;
    private final LocationPropertiesView locationPropertiesView;
    private final BorderPane lettersPane;
    private final TabPane tabPane;
    private final Browser browser;
    private Tab browseDictionaryTab;

    public TokenPropertiesSkin(TokenPropertiesView control) {
        super(control);

        lettersPane = new BorderPane();
        lettersPane.setBorder(BORDER);

        browser = new Browser();

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setBorder(BORDER);
        browseDictionaryTab = new Tab("Browse Dictionary", browser);
        browseDictionaryTab.setDisable(true);

        locationComboBox = new ComboBox<>();
        locationPropertiesView = new LocationPropertiesView();

        group = new ArabicLabelToggleGroup();
        group.setWidth(64);
        group.setHeight(64);
        group.setFont(ARABIC_FONT_36);

        locationPropertiesView.updatedPropertyProperty().addListener((o, ov, nv) -> {
            if (nv == null) {
                return;
            }
            if (isGivenType(RootLetters.class, nv)) {
                RootLetters rootLetters = (RootLetters) nv;
                loadDictionary(rootLetters);
            }
            if (isGivenType(NamedTemplate.class, nv)) {
                NamedTemplate form = (NamedTemplate) nv;
                // TODO: do something here
            }
        });

        initializeSkin();
    }

    private void createLettersPane() {
        lettersPane.setCenter(null);

        HBox hBox = new HBox();
        hBox.setSpacing(GAP);
        hBox.setPadding(new Insets(GAP));
        hBox.setAlignment(CENTER);
        hBox.setNodeOrientation(RIGHT_TO_LEFT);

        TokenPropertiesView view = getSkinnable();
        final ObservableList<TokenPropertiesView.LocationLabel> labels = view.getLabels();
        labels.forEach(locationLabel -> {
            ArabicLabelView labelView = new ArabicLabelView();
            labelView.setLabel(locationLabel);
            labelView.setGroup(group);
            // final int index = locationLabel.getIndex();
            labelView.selectedProperty().addListener((o, ov, nv) -> {
                TokenPropertiesView.SelectionStatus status = nv ? SELECTED : AVAILABLE;
                // replace element at "index" this will fire change event in the "labels" list
                // labels.set(index, new TokenPropertiesView.LocationLabel(status, locationLabel.getLetter(), index));
                locationLabel.setStatus(status);
            });
            labelView.setSelect(locationLabel.getStatus().equals(SELECTED));
            labelView.setDisable(locationLabel.getStatus().equals(NOT_AVAILABLE));
            hBox.getChildren().add(labelView);
        });

        lettersPane.setCenter(hBox);
    }

    private GridPane createLocationsPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(GAP));
        gridPane.setAlignment(CENTER);

        Label label = new Label(RESOURCE_BUNDLE.getString("locations.label"));
        gridPane.add(label, 0, 0);

        locationComboBox.setDisable(true);
        locationComboBox.setButtonCell(new LocationListCell());
        locationComboBox.setCellFactory(param -> new LocationListCell());
        gridPane.add(locationComboBox, 1, 0);

        TokenPropertiesView view = getSkinnable();
        view.tokenProperty().addListener((o, ov, nv) -> updateLocations(nv));
        locationComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                locationPropertiesView.setLocation(nv);
                tabPane.getSelectionModel().select(0);
                MorphologicalEntry morphologicalEntry = nv.getMorphologicalEntry();
                if (morphologicalEntry != null) {
                    loadDictionary(morphologicalEntry.getRootLetters());
                }
                view.changeLocation(ov, nv);
            }
            createLettersPane();
        });
        gridPane.setBorder(BORDER);
        return gridPane;
    }

    private void loadDictionary(RootLetters rootLetters) {
        String searchString = null;
        boolean disableDictionaryTab = (rootLetters == null || rootLetters.isEmpty());
        if (!disableDictionaryTab) {
            // not sure whether display is initialized or not
            rootLetters.initDisplayName();
            searchString = rootLetters.getDisplayName();
        }
        disableDictionaryTab = searchString == null;
        browseDictionaryTab.setDisable(disableDictionaryTab);
        if (!disableDictionaryTab) {
            String url = format("%s%s", MAWRID_READER_URL, searchString);
            browser.loadUrl(url);
        }
    }

    private void updateLocations(Token token) {
        tabPane.getSelectionModel().select(0);
        locationComboBox.getItems().clear();
        locationComboBox.setDisable(true);
        if (token == null) {
            return;
        }
        List<Location> locations = token.getLocations();
        if (locations == null || locations.isEmpty()) {
            return;
        }
        locationComboBox.setDisable(false);
        locationComboBox.getItems().addAll(locations.toArray(new Location[locations.size()]));
        locationComboBox.getSelectionModel().select(0);
    }

    private void initializeSkin() {
        VBox vBox = new VBox();
        vBox.setSpacing(GAP);

        tabPane.getTabs().addAll(new Tab(RESOURCE_BUNDLE.getString("locationProperties.label"), locationPropertiesView),
                browseDictionaryTab);

        createLettersPane();

        VBox subBox = new VBox();
        subBox.setSpacing(GAP);
        subBox.getChildren().addAll(lettersPane, createLocationsPane());

        TitledPane titledPane = new TitledPane("Token Letters and Locations", subBox);

        vBox.getChildren().addAll(titledPane, tabPane);
        getChildren().add(vBox);
        updateLocations(getSkinnable().getToken());
    }
}
