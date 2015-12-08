package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.common.LocationListCell;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

/**
 * @author sali
 */
public class TokenPropertiesSkin extends SkinBase<TokenPropertiesView> {

    private final ComboBox<Location> locationComboBox;
    private final LocationPropertiesView locationPropertiesView;
    private final TabPane tabPane;

    public TokenPropertiesSkin(TokenPropertiesView control) {
        super(control);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setBorder(BORDER);
        locationComboBox = new ComboBox<>();
        locationPropertiesView = new LocationPropertiesView();
        initializeSkin();
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
            }
        });
        gridPane.setBorder(BORDER);
        return gridPane;
    }

    private void updateLocations(Token token) {
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

        tabPane.getTabs().add(new Tab(RESOURCE_BUNDLE.getString("locationProperties.label"), locationPropertiesView));

        vBox.getChildren().addAll(createLocationsPane(), tabPane);
        getChildren().add(vBox);
    }
}
