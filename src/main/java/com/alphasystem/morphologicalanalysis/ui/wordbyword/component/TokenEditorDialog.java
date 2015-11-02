package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.NamedTemplate;
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

import static com.alphasystem.morphologicalanalysis.ui.common.Global.ARABIC_FONT_MEDIUM;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.*;
import static java.lang.String.format;
import static javafx.event.ActionEvent.ACTION;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ButtonType.*;
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
    private final ObjectProperty<Token> token;
    private final TokenAdapter tokenAdapter = new TokenAdapter();
    private final ComboBoxFactory instance = ComboBoxFactory.getInstance();
    private final RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private final ComboBox<PartOfSpeech> partOfSpeechComboBox = instance.getPartOfSpeechComboBox();
    private final ComboBox<NamedTemplate> namedTemplateComboBox = instance.getNamedTemplateComboBox();
    private final ComboBox<NamedTag> namedTagComboBox = instance.getNamedTagComboBox();
    private final RootWordPane rootWordPane = new RootWordPane(null);
    private BorderPane lettersPane;
    private TitledPane propertiesPane;
    private AbstractPropertiesPane particlePropertiesPane;
    private AbstractPropertiesPane nounPropertiesPane;
    private AbstractPropertiesPane proNounPropertiesPane;
    private AbstractPropertiesPane verbPropertiesPane;
    private ComboBox<Location> locationComboBox;

    public TokenEditorDialog(Token token) {
        setTitle(getTitle(token));
        setResizable(true);
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
        rootWordPane.rootWordProperty().bind(tokenAdapter.rootWordProperty());

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

    private Token updateToken() {
        tokenAdapter.updateLocationStartAndEndIndices();
        return repositoryTool.saveToken(tokenAdapter.getToken());
    }

    public ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public void setToken(Token token) {
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

            AbstractProperties properties = location.getProperties();
            AbstractPropertiesPane pp = getPropertiesPane(properties);
            this.propertiesPane.setContent(pp);
            pp.updateComboBoxes();
            if (isNoun(properties)) {
                this.propertiesPane.setText("Noun Properties");
            } else if (isPronoun(properties)) {
                this.propertiesPane.setText("ProNoun Properties");
            } else if (isVerb(properties)) {
                this.propertiesPane.setText("Verb Properties");
            } else {
                this.propertiesPane.setText("Properties");
            }
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
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

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
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(DEFAULT_PADDING);

        Label label = new Label(RESOURCE_BUNDLE.getString("partOfSpeech.label"));
        gp.add(label, 0, 0);

        label.setLabelFor(partOfSpeechComboBox);
        gp.add(partOfSpeechComboBox, 0, 1);

        label = new Label(RESOURCE_BUNDLE.getString("rootWords.label"));
        gp.add(label, 1, 0);

        gp.add(rootWordPane, 1, 1);

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

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(25, 25, 25, 25));

        TitledPane locationPane = new TitledPane("Location", createLocationsPane());
        TitledPane commonPropertiesPane = new TitledPane("Common Properties", createCommonPropertiesPane());

        VBox vBox1 = new VBox(5);
        vBox1.setPadding(DEFAULT_PADDING);
        vBox1.getChildren().addAll(lettersPane, createTranslationPane());
        TitledPane lettersAndTranslationPane = new TitledPane("Letters & Translation", vBox1);

        vBox.getChildren().addAll(lettersAndTranslationPane, locationPane, commonPropertiesPane, propertiesPane);

        getDialogPane().setContent(vBox);
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
        hBox.setSpacing(10);
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
                    if (isNoun(properties)) {
                        this.propertiesPane.setText("Noun Properties");
                    } else if (isPronoun(properties)) {
                        this.propertiesPane.setText("ProNoun Properties");
                    } else if (isVerb(properties)) {
                        this.propertiesPane.setText("Verb Properties");
                    } else {
                        this.propertiesPane.setText("Properties");
                    }
                });
        namedTemplateComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNamedTemplate(newValue);
                });
        namedTagComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNamedTag(newValue);
                });
    }

}
