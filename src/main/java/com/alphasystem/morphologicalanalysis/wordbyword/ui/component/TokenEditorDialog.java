package com.alphasystem.morphologicalanalysis.wordbyword.ui.component;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.model.LocationAdapter;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.NamedTag;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.ui.model.TokenAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static java.lang.String.format;
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

    private final static double TOGGLE_BUTTON_WIDTH = 72;
    private final static double TOGGLE_BUTTON_HEIGHT = 72;
    private static final Border BORDER = new Border(new BorderStroke(LIGHTGREY, SOLID, EMPTY, THIN));

    private final ObjectProperty<Token> token;
    private final TokenAdapter tokenAdapter = new TokenAdapter();
    private final ComboBoxFactory instance = ComboBoxFactory.getInstance();
    private final RepositoryTool repositoryTool = RepositoryTool.getInstance();
    private ComboBox<LocationAdapter> locationComboBox;
    private ComboBox<PartOfSpeech> partOfSpeechComboBox;
    private ComboBox<NamedTemplate> namedTemplateComboBox;
    private ComboBox<NamedTag> namedTagComboBox;
    private BorderPane lettersPane;

    public TokenEditorDialog(Token token) {
        setTitle(getTitle(token));

        lettersPane = new BorderPane();
        lettersPane.setBorder(BORDER);

        this.token = new SimpleObjectProperty<>(token);
        tokenAdapter.updateToken(token, 0);

        // initialize initial dialog
        setup();

        setResultConverter(db -> {
            System.out.println("////////////////// " + db.getButtonData());
            if (db.getButtonData().isCancelButton()) {
                return null;
            }
            return updateToken();
        });
        this.token.addListener((observable, oldValue, newToken) -> {
            updateDialog(0, true);
        });

        getDialogPane().getButtonTypes().addAll(APPLY, OK, CLOSE);
        Button applyButton = (Button) getDialogPane().lookupButton(APPLY);
        applyButton.addEventFilter(ActionEvent.ACTION, event -> {
            setToken(updateToken());
            event.consume();
        });
        locationComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int locationIndex = (Integer) newValue;
            if (locationIndex < 0) {
                System.out.println(locationIndex);
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
        if(initLocations) {
            initLocations();
        }
        Location location = tokenAdapter.getLocation();
        if (location != null) {
            partOfSpeechComboBox.getSelectionModel().select(location.getPartOfSpeech());
            namedTemplateComboBox.getSelectionModel().select(location.getFormTemplate());
            namedTagComboBox.getSelectionModel().select(location.getNamedTag());
        }
    }

    private GridPane createLocationsPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label(RESOURCE_BUNDLE.getString("locations.label"));
        gridPane.add(label, 0, 0);
        locationComboBox = new ComboBox<>();
        locationComboBox.getStylesheets().add(TREE_BANK_STYLE_SHEET);
        initLocations();
        gridPane.add(locationComboBox, 1, 0);

        gridPane.setBorder(BORDER);
        return gridPane;
    }

    private Pane createTranslationPane() {
        BorderPane borderPane = new BorderPane();

        TextArea translationArea = new TextArea(tokenAdapter.getTranslation());
        translationArea.setPrefColumnCount(5);
        translationArea.setPrefRowCount(5);
        translationArea.textProperty().bindBidirectional(tokenAdapter.translationProperty());

        borderPane.setCenter(translationArea);
        borderPane.setBorder(BORDER);
        return borderPane;
    }

    private GridPane createPartOfSpeechPane() {
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(25, 25, 25, 25));

        Label label = new Label(RESOURCE_BUNDLE.getString("partOfSpeech.label"));
        gp.add(label, 0, 0);

        partOfSpeechComboBox = instance.getPartOfSpeechComboBox();
        partOfSpeechComboBox.getSelectionModel().select(0);
        partOfSpeechComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setPartOfSpeech(newValue);
                    // TODO: refresh properties panel
                });
        label.setLabelFor(partOfSpeechComboBox);
        gp.add(partOfSpeechComboBox, 0, 1);

        label = new Label(RESOURCE_BUNDLE.getString("rootWords.label"));
        gp.add(label, 1, 0);

        gp.add(new Pane(), 1, 1);

        label = new Label(RESOURCE_BUNDLE.getString("form.label"));
        gp.add(label, 0, 2);

        namedTemplateComboBox = instance.getNamedTemplateComboBox();
        namedTemplateComboBox.getSelectionModel().select(0);
        namedTemplateComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNamedTemplate(newValue);
                });
        gp.add(namedTemplateComboBox, 0, 3);

        label = new Label(RESOURCE_BUNDLE.getString("namedTag.label"));
        gp.add(label, 1, 2);
        namedTagComboBox = instance.getNamedTagComboBox();
        namedTagComboBox.getSelectionModel().select(0);
        namedTagComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    tokenAdapter.setNamedTag(newValue);
                });
        gp.add(namedTagComboBox, 1, 3);

        gp.setBorder(BORDER);
        return gp;
    }

    private void setup() {
        createLettersPane(tokenAdapter.getLetters());

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(25, 25, 25, 25));

        vBox.getChildren().addAll(lettersPane, createTranslationPane(), createLocationsPane(),
                new Separator(), createPartOfSpeechPane(), new Separator());

        getDialogPane().setContent(vBox);
    }

    private void initLocations() {
        ObservableList<LocationAdapter> items = locationComboBox.getItems();
        int size = items.size();
        items.remove(0, size);

        ObservableList<Location> _locations = tokenAdapter.getLocations();
        LocationAdapter[] locationAdapters = new LocationAdapter[_locations.size()];
        for (int i = 0; i < locationAdapters.length; i++) {
            locationAdapters[i] = new LocationAdapter(_locations.get(i));
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
        hBox.setPadding(new Insets(20, 20, 20, 20));
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

}
