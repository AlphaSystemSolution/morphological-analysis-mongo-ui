package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.arabic.ui.Browser;
import com.alphasystem.arabic.ui.RootLettersPicker;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.LocationListCell;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.model.TokenAdapter;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.*;
import static java.lang.String.format;
import static javafx.event.ActionEvent.ACTION;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ButtonType.*;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;
import static javafx.scene.layout.BorderStroke.THIN;
import static javafx.scene.layout.BorderStrokeStyle.SOLID;
import static javafx.scene.layout.CornerRadii.EMPTY;
import static javafx.scene.paint.Color.LIGHTGREY;

/**
 * @author sali
 */
public class TokenEditorDialog extends Dialog<Token> {

    public static final Insets DEFAULT_PADDING = new Insets(5, 5, 5, 5);
    private final static double TOGGLE_BUTTON_WIDTH = 72;
    private final static double TOGGLE_BUTTON_HEIGHT = 72;
    private static final Border BORDER = new Border(new BorderStroke(LIGHTGREY, SOLID, EMPTY, THIN));
    private static final int GAP = 5;
    private final ObjectProperty<Token> token;
    private final TokenAdapter tokenAdapter = new TokenAdapter();
    private final ComboBoxFactory instance = ComboBoxFactory.getInstance();
    private final RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private final ComboBox<PartOfSpeech> partOfSpeechComboBox = instance.getPartOfSpeechComboBox();
    private final ComboBox<NamedTemplate> namedTemplateComboBox = instance.getNamedTemplateComboBox();
    private final ComboBox<NamedTag> namedTagComboBox = instance.getNamedTagComboBox();
    private final RootLettersPicker rootLettersPicker = new RootLettersPicker();
    private final TabPane tabPane = new TabPane();
    private final Browser browser = new Browser();
    private BorderPane lettersPane;
    private TitledPane propertiesPane;
    private AbstractPropertiesPane particlePropertiesPane;
    private AbstractPropertiesPane nounPropertiesPane;
    private AbstractPropertiesPane proNounPropertiesPane;
    private AbstractPropertiesPane verbPropertiesPane;
    private ComboBox<Location> locationComboBox;
    private Tab browseDictionaryTab;

    public TokenEditorDialog(Token token) {
        setTitle(getTitle(token));
        setResizable(true);

        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setBorder(BORDER);
        browseDictionaryTab = new Tab("Browse Dictionary", browser);
        browseDictionaryTab.setDisable(true);

        lettersPane = new BorderPane();
        lettersPane.setBorder(BORDER);
        particlePropertiesPane = new ParticlePropertiesPane(tokenAdapter);
        initListeners();
        nounPropertiesPane = new NounPropertiesPane(tokenAdapter);
        proNounPropertiesPane = new ProNounPropertiesPane(tokenAdapter);
        verbPropertiesPane = new VerbPropertiesPane(tokenAdapter);
        propertiesPane = new TitledPane("Noun Properties", nounPropertiesPane);

        this.token = new SimpleObjectProperty<>(token);
        tokenAdapter.updateToken(token, 0);

        tokenAdapter.rootWordProperty().addListener((o, ov, nv) -> {
            ArabicLetterType firstRadical = (nv == null) ? null : nv.getFirstRadical();
            ArabicLetterType secondRadical = (nv == null) ? null : nv.getSecondRadical();
            ArabicLetterType thirdRadical = (nv == null) ? null : nv.getThirdRadical();
            ArabicLetterType fourthRadical = (nv == null) ? null : nv.getFourthRadical();
            rootLettersPicker.setRootLetters(firstRadical, secondRadical, thirdRadical, fourthRadical);

            boolean disable = firstRadical == null || secondRadical == null || thirdRadical == null;
            browseDictionaryTab.setDisable(disable);

            if (!disable) {
                String fr = fourthRadical == null ? "" : fourthRadical.toCode();
                String url = format("%s%s%s%s%s", MAWRID_READER_URL, firstRadical.toCode(), secondRadical.toCode(),
                        thirdRadical.toCode(), fr);
                browser.loadUrl(url);
            }
        });

        // initialize initial dialog
        setup();

        setResultConverter(db -> {
            if (db.getButtonData().isCancelButton()) {
                return null;
            }
            return updateToken();
        });
        this.token.addListener((observable, oldValue, newToken) -> {
            if (newToken == null) {
                return;
            }
            updateDialog(0, true);
        });

        getDialogPane().getButtonTypes().addAll(APPLY, OK, CLOSE);
        Button applyButton = (Button) getDialogPane().lookupButton(APPLY);
        applyButton.addEventFilter(ACTION, event -> {
            Token t = updateToken();
            setToken(null);
            setToken(t);
            event.consume();
        });
        locationComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int locationIndex = (Integer) newValue;
            if (locationIndex < 0) {
                return;
            }
            updateDialog(locationIndex, false);
        });

    }

    private static String getTitle(Token token) {
        String displayName = (token == null) ? "" : format("(%s)", token.getDisplayName());
        return format("Edit token %s", displayName);
    }

    private static void changePropertiesPaneTitle(AbstractProperties properties, TitledPane propertiesPane) {
        if (isNoun(properties)) {
            propertiesPane.setText("Noun Properties");
        } else if (isPronoun(properties)) {
            propertiesPane.setText("ProNoun Properties");
        } else if (isVerb(properties)) {
            propertiesPane.setText("Verb Properties");
        } else {
            propertiesPane.setText("Properties");
        }
    }

    private Token updateToken() {
        tokenAdapter.updateLocationStartAndEndIndices();
        return repositoryTool.saveToken(tokenAdapter.getToken());
    }

    public ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public void setToken(Token token) {
        tabPane.getSelectionModel().select(0);
        this.token.set(token);
    }

    private void updateDialog(int locationIndex, boolean initLocations) {
        Token token = tokenProperty().get();
        titleProperty().setValue(getTitle(token));
        tokenAdapter.updateToken(token, locationIndex);
        createLettersPane(tokenAdapter.getLetters());
        if (initLocations) {
            initLocations();
        }
        Location location = tokenAdapter.getLocation();
        if (location != null) {
            partOfSpeechComboBox.getSelectionModel().select(location.getPartOfSpeech());
            namedTemplateComboBox.getSelectionModel().select(location.getFormTemplate());
            namedTagComboBox.getSelectionModel().select(location.getNamedTag());
            // TODO: this was done for dictionary , figure out where put this
            // rootWordPane.setNamedTemplate(location.getFormTemplate());

            AbstractProperties properties = location.getProperties();
            AbstractPropertiesPane pp = getPropertiesPane(properties);
            this.propertiesPane.setContent(pp);
            pp.updateComboBoxes();
            changePropertiesPaneTitle(properties, this.propertiesPane);
        }
    }

    private AbstractPropertiesPane getPropertiesPane(AbstractProperties properties) {
        AbstractPropertiesPane p;
        if (isNoun(properties)) {
            p = nounPropertiesPane;
        } else if (isPronoun(properties)) {
            p = proNounPropertiesPane;
        } else if (isVerb(properties)) {
            p = verbPropertiesPane;
        } else {
            p = particlePropertiesPane;
        }
        p.updateComboBoxes();
        return p;
    }

    private GridPane createLocationsPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(DEFAULT_PADDING);

        Label label = new Label(RESOURCE_BUNDLE.getString("locations.label"));
        gridPane.add(label, 0, 0);
        locationComboBox = new ComboBox<>();
        locationComboBox.setButtonCell(new LocationListCell());
        locationComboBox.setCellFactory(param -> new LocationListCell());
        initLocations();
        gridPane.add(locationComboBox, 1, 0);

        gridPane.setBorder(BORDER);
        return gridPane;
    }

    private Pane createTranslationPane() {
        BorderPane borderPane = new BorderPane();

        TextField translationArea = new TextField(tokenAdapter.getTranslation());
        translationArea.setPrefColumnCount(5);
        translationArea.textProperty().bindBidirectional(tokenAdapter.translationProperty());

        borderPane.setCenter(translationArea);
        borderPane.setBorder(BORDER);
        return borderPane;
    }

    private GridPane createCommonPropertiesPane() {
        GridPane gp = new GridPane();
        gp.setHgap(GAP);
        gp.setVgap(GAP);
        gp.setPadding(DEFAULT_PADDING);

        Label label = new Label(RESOURCE_BUNDLE.getString("partOfSpeech.label"));
        gp.add(label, 0, 0);

        label.setLabelFor(partOfSpeechComboBox);
        gp.add(partOfSpeechComboBox, 0, 1);

        label = new Label(RESOURCE_BUNDLE.getString("rootWords.label"));
        gp.add(label, 1, 0);

        gp.add(rootLettersPicker, 1, 1);

        label = new Label(RESOURCE_BUNDLE.getString("form.label"));
        gp.add(label, 0, 2);

        gp.add(namedTemplateComboBox, 0, 3);

        label = new Label(RESOURCE_BUNDLE.getString("namedTag.label"));
        gp.add(label, 1, 2);

        gp.add(namedTagComboBox, 1, 3);

        return gp;
    }

    private void setup() {
        createLettersPane(tokenAdapter.getLetters());

        BorderPane borderPane = new BorderPane();

        VBox vBox = new VBox(5);
        vBox.setPadding(DEFAULT_PADDING);
        vBox.getChildren().addAll(lettersPane, createTranslationPane(), createLocationsPane());
        TitledPane titledPane = new TitledPane("Letters, Translation, & Locations", vBox);

        GridPane commonPropertiesPane = createCommonPropertiesPane();
        commonPropertiesPane.setBorder(BORDER);

        Tab tab = new Tab("Letters, Translation, & Locations", vBox);
        tabPane.getTabs().add(tab);

        tab = new Tab("Common Properties", commonPropertiesPane);
        tabPane.getTabs().add(tab);

        tab = new Tab("Part of Speech Properties", propertiesPane);
        tabPane.getTabs().add(tab);

        tabPane.getTabs().add(browseDictionaryTab);

        borderPane.setCenter(tabPane);

        getDialogPane().setContent(borderPane);
    }

    private void initLocations() {
        ObservableList<Location> items = locationComboBox.getItems();
        int size = items.size();
        items.remove(0, size);

        ObservableList<Location> _locations = tokenAdapter.getLocations();
        Location[] locationAdapters = new Location[_locations.size()];
        for (int i = 0; i < locationAdapters.length; i++) {
            locationAdapters[i] = _locations.get(i);
        }
        items.addAll(locationAdapters);
        locationComboBox.getSelectionModel().select(0);
    }

    private void createLettersPane(ObservableList<ArabicLetter> letters) {
        lettersPane.setCenter(null);
        if (letters == null || letters.isEmpty()) {
            return;
        }
        HBox hBox = new HBox();
        hBox.setSpacing(GAP);
        hBox.setPadding(DEFAULT_PADDING);
        hBox.setAlignment(CENTER);
        hBox.setNodeOrientation(RIGHT_TO_LEFT);
        int index = 0;
        for (ArabicLetter letter : letters) {
            ToggleButton button = new ToggleButton(letter.toUnicode());
            button.setSelected(tokenAdapter.getSelectedValues().get(index).get());
            button.setUserData(index);
            button.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    Integer currentIndex = (Integer) button.getUserData();
                    tokenAdapter.getSelectedValues().get(currentIndex).setValue(true);
                }
            });
            button.setMinSize(TOGGLE_BUTTON_WIDTH, TOGGLE_BUTTON_HEIGHT);
            button.setFont(ARABIC_FONT_MEDIUM);
            button.setAlignment(CENTER);
            hBox.getChildren().add(button);
            index++;
        }

        lettersPane.setCenter(hBox);

        getDialogPane().requestLayout();
    }

    private void initListeners() {
        partOfSpeechComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setPartOfSpeech(newValue);
                    AbstractProperties properties = tokenAdapter.getLocation().getProperties();
                    propertiesPane.setContent(getPropertiesPane(properties));
                    changePropertiesPaneTitle(properties, propertiesPane);
                });
        namedTemplateComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNamedTemplate(newValue);
                    // TODO: this was done for dictionary , figure out where put this
                    // rootWordPane.setNamedTemplate(newValue);
                });
        namedTagComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNamedTag(newValue);
                });
    }

}
