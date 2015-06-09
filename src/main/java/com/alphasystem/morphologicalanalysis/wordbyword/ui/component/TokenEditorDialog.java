package com.alphasystem.morphologicalanalysis.wordbyword.ui.component;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.morphologicalanalysis.ui.common.ComboBoxFactory;
import com.alphasystem.morphologicalanalysis.ui.common.model.LocationAdapter;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech;
import com.alphasystem.morphologicalanalysis.wordbyword.ui.model.TokenAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static java.lang.String.format;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.ButtonType.*;

/**
 * @author sali
 */
public class TokenEditorDialog extends Dialog<Token> {

    private final static double TOGGLE_BUTTON_WIDTH = 72;
    private final static double TOGGLE_BUTTON_HEIGHT = 72;

    private final ObjectProperty<Token> token;
    private final TokenAdapter tokenAdapter = new TokenAdapter();

    private TextArea transaltionArea;
    private ComboBox<LocationAdapter> locationComboBox;
    private ComboBox<PartOfSpeech> partOfSpeechComboBox;
    private BorderPane lettersPane;

    public TokenEditorDialog(Token token) {
        setTitle(getTitle(token));

        lettersPane = new BorderPane();

        this.token = new SimpleObjectProperty<>(token);
        tokenAdapter.updateToken(token, 0);

        // initialize initial dialog
        setup();

        setResultConverter(db -> {
            System.out.println("////////////////// " + db.getButtonData());
            //TODO:
            return tokenProperty().get();
        });
        this.token.addListener((observable, oldValue, newToken) -> {
            updateDialog();
        });

        getDialogPane().getButtonTypes().addAll(APPLY, OK, CLOSE);
        Button applyButton = (Button) getDialogPane().lookupButton(APPLY);
    }

    private static String getTitle(Token token) {
        String displayName = (token == null) ? "" : format("(%s)", token.getDisplayName());
        return format("Edit token %s", displayName);
    }

    public ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public void setToken(Token token) {
        this.token.set(token);
    }

    private void updateDialog() {
        Token token = tokenProperty().get();
        titleProperty().setValue(getTitle(token));
        tokenAdapter.updateToken(token, 0);
        createLettersPane(tokenAdapter.getLetters());
        initLocations();
    }

    private void setup() {
        createLettersPane(tokenAdapter.getLetters());

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        // token letters
        gridPane.add(lettersPane, 0, 0, 2, 1);

        // translation
        BorderPane borderPane = new BorderPane();
        transaltionArea = new TextArea(tokenAdapter.getTranslation());
        transaltionArea.setPrefColumnCount(5);
        transaltionArea.setPrefRowCount(5);
        borderPane.setCenter(transaltionArea);
        gridPane.add(borderPane, 0, 1, 2, 1);
        transaltionArea.textProperty().bindBidirectional(tokenAdapter.translationProperty());

        //locationComboBox comboBox
        borderPane = new BorderPane();
        locationComboBox = new ComboBox<>();
        locationComboBox.getStylesheets().add(TREE_BANK_STYLE_SHEET);
        initLocations();
        locationComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // TODO:
        });
        borderPane.setCenter(locationComboBox);
        gridPane.add(borderPane, 0, 2, 2, 1);

        gridPane.add(new Separator(), 0, 3, 2, 1);

        // part of speech comboBox
        borderPane = new BorderPane();
        GridPane gp = new GridPane();
        Label label = new Label(RESOURCE_BUNDLE.getString("partOfSpeech.label"));
        gp.add(label, 0, 0);
        partOfSpeechComboBox = ComboBoxFactory.getInstance().getPartOfSpeechComboBox();
        partOfSpeechComboBox.getSelectionModel().select(0);
        gp.add(partOfSpeechComboBox, 0, 1);
        label = new Label(RESOURCE_BUNDLE.getString("rootWords.label"));
        gp.add(label, 1, 0);
        gp.add(new Pane(), 1, 1);
        borderPane.setCenter(gp);
        gridPane.add(borderPane, 0, 4, 2, 1);

        getDialogPane().setContent(gridPane);
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
        for (ArabicLetter letter : letters) {
            ToggleButton button = new ToggleButton(letter.toUnicode());
            button.setMinSize(TOGGLE_BUTTON_WIDTH, TOGGLE_BUTTON_HEIGHT);
            button.setFont(ARABIC_FONT_MEDIUM);
            button.setAlignment(CENTER);
            hBox.getChildren().add(button);
        }

        lettersPane.setCenter(hBox);

        getDialogPane().requestLayout();
    }
}
