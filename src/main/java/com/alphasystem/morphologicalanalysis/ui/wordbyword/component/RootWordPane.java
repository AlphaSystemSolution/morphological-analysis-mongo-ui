package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabickeyboard.ui.Keyboard;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RootWord;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Popup;

import static com.alphasystem.arabic.model.ArabicLetterType.getByUnicode;
import static com.alphasystem.util.AppUtil.getResourceAsStream;
import static java.lang.String.format;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.input.KeyEvent.KEY_TYPED;
import static javafx.scene.paint.Color.LAVENDER;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class RootWordPane extends HBox {

    private static final String MAWRID_READER_URL = System.getProperty("mawrid-reader.url");
    final Browser browser = new Browser();
    private final ObjectProperty<RootWord> rootWord;
    private final Button dictionaryButton;
    private RootLetterPane currentPane;

    public RootWordPane(RootWord rootWord) {
        setSpacing(10);
        setPadding(new Insets(10));

        this.rootWord = new SimpleObjectProperty<>();
        HBox lettersPane = createLettersPane();

        final Popup keyboardPopup = new Popup();
        keyboardPopup.setAutoHide(true);
        keyboardPopup.setHideOnEscape(true);

        final Keyboard keyboard = new Keyboard(lettersPane);
        final VBox keyboardView = keyboard.view();
        keyboardView.setBackground(new Background(new BackgroundFill(LAVENDER, new CornerRadii(1.0), new Insets(10))));
        keyboardPopup.getContent().add(keyboardView);


        final Button keyboardButton = new Button();
        keyboardButton.setGraphic(new ImageView(new Image(getResourceAsStream("images.keyboard-icon.png"))));
        keyboardButton.setOnAction(event -> showPopup(keyboardPopup, keyboardButton, -1, -1));

        lettersPane.addEventFilter(KEY_TYPED, event -> {
            if (currentPane != null) {
                ArabicLetterType arabicLetterType = getByUnicode(event.getCharacter().charAt(0));
                if (arabicLetterType != null) {
                    currentPane.setLetter(arabicLetterType);
                }
            }
        });


        final Popup browserPopup = new Popup();
        browserPopup.setX(200);
        browserPopup.setY(200);
        browserPopup.setAutoHide(true);
        browserPopup.setHideOnEscape(true);
        ScrollPane scrollPane = new ScrollPane(browser);
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);
        browserPopup.getContent().add(scrollPane);

        dictionaryButton = new Button();
        dictionaryButton.setGraphic(new ImageView(new Image(getResourceAsStream("images.dictionary-icon.png"))));
        dictionaryButton.setOnAction(event -> showBrowser(browserPopup));

        setRootWord(rootWord);

        getChildren().addAll(lettersPane, keyboardButton, dictionaryButton);
    }

    private void enableDictionaryButton() {
        RootWord rootWord = getRootWord();
        boolean enable = !isBlank(MAWRID_READER_URL) && rootWord != null && rootWord.getFirstRadical() != null &&
                rootWord.getSecondRadical() != null && rootWord.getThirdRadical() != null;
        dictionaryButton.setDisable(!enable);
    }

    private void showPopup(Popup popup, Button button, double anchorX, double anchorY) {
        if (popup.isShowing()) {
            popup.hide();
        } else {
            final Bounds bounds = button.localToScreen(button.getBoundsInLocal());
            anchorX = anchorX <= -1 ? bounds.getMinX() : anchorX;
            anchorY = anchorY <= -1 ? bounds.getMinY() + bounds.getHeight() : anchorY;
            popup.show(button, anchorX, anchorY);
        }
    }

    private void showBrowser(Popup browserPopup) {
        RootWord rw = getRootWord();

        if (rw != null) {
            ArabicLetterType fourthRadical = rw.getFourthRadical();
            String fr = fourthRadical == null ? "" : fourthRadical.toCode();
            String url = format("%s%s%s%s%s", MAWRID_READER_URL, rw.getFirstRadical().toCode(),
                    rw.getSecondRadical().toCode(), rw.getThirdRadical().toCode(), fr);
            browser.loadUrl(url);
            showPopup(browserPopup, dictionaryButton, 200, 200);
        }
    }

    private HBox createLettersPane() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(5));

        RootLetterPane firstRadicalPane = new RootLetterPane();
        RootLetterPane secondRadicalPane = new RootLetterPane();
        RootLetterPane thirdRadicalPane = new RootLetterPane();
        RootLetterPane fourthRadicalPane = new RootLetterPane();
        firstRadicalPane.setSelected(true);
        currentPane = firstRadicalPane;

        rootWordProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                setRootWord(null);
            }
            firstRadicalPane.setLetter(getRootWord().getFirstRadical());
            secondRadicalPane.setLetter(getRootWord().getSecondRadical());
            thirdRadicalPane.setLetter(getRootWord().getThirdRadical());
            fourthRadicalPane.setLetter(getRootWord().getFourthRadical());
            enableDictionaryButton();
        });

        firstRadicalPane.letterProperty().addListener((observable, oldValue, newValue) ->
                selectPane(1, newValue, firstRadicalPane, secondRadicalPane));
        firstRadicalPane.setOnMouseClicked(event -> selectPane(currentPane, firstRadicalPane));

        secondRadicalPane.letterProperty().addListener((observable, oldValue, newValue) ->
                selectPane(2, newValue, secondRadicalPane, thirdRadicalPane));
        secondRadicalPane.setOnMouseClicked(event -> selectPane(currentPane, secondRadicalPane));

        thirdRadicalPane.letterProperty().addListener((observable, oldValue, newValue) ->
                selectPane(3, newValue, thirdRadicalPane, fourthRadicalPane));
        thirdRadicalPane.setOnMouseClicked(event -> selectPane(currentPane, thirdRadicalPane));

        fourthRadicalPane.letterProperty().addListener((observable, oldValue, newValue) ->
                selectPane(4, newValue, fourthRadicalPane, firstRadicalPane));
        fourthRadicalPane.setOnMouseClicked(event -> selectPane(currentPane, fourthRadicalPane));

        hBox.getChildren().addAll(fourthRadicalPane, thirdRadicalPane, secondRadicalPane, firstRadicalPane);
        return hBox;
    }

    private void selectPane(int index, ArabicLetterType newValue, RootLetterPane currentPane, RootLetterPane newPane) {
        if (index >= 1) {
            switch (index) {
                case 1:
                    getRootWord().setFirstRadical(newValue);
                    break;
                case 2:
                    getRootWord().setSecondRadical(newValue);
                    break;
                case 3:
                    getRootWord().setThirdRadical(newValue);
                    break;
                case 4:
                    getRootWord().setFourthRadical(newValue);
                    break;
            }
        }
        currentPane.setSelected(false);
        newPane.setSelected(true);
        this.currentPane = newPane;
        enableDictionaryButton();
    }

    private void selectPane(RootLetterPane currentPane, RootLetterPane newPane) {
        selectPane(-1, null, currentPane, newPane);
    }

    public final RootWord getRootWord() {
        return rootWord.get();
    }

    public final void setRootWord(RootWord rootWord) {
        RootWord rw = rootWord == null ? new RootWord() : rootWord;
        this.rootWord.set(rw);
    }

    public final ObjectProperty<RootWord> rootWordProperty() {
        return rootWord;
    }
}
