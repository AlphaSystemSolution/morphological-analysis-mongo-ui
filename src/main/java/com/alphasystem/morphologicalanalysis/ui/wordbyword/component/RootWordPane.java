package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabickeyboard.ui.Keyboard;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.RootWord;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import static com.alphasystem.arabic.model.ArabicLetterType.getByUnicode;
import static com.alphasystem.util.AppUtil.getResourceAsStream;
import static javafx.scene.input.KeyEvent.KEY_TYPED;
import static javafx.scene.paint.Color.LIGHTGREY;

/**
 * @author sali
 */
public class RootWordPane extends HBox {

    private final ObjectProperty<RootWord> rootWord;
    private RootLetterPane currentPane;

    public RootWordPane(RootWord rootWord) {
        setSpacing(10);
        setPadding(new Insets(10));

        this.rootWord = new SimpleObjectProperty<>();
        HBox lettersPane = createLettersPane();

        final Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        final Keyboard keyboard = new Keyboard(lettersPane);
        final VBox keyboardView = keyboard.view();
        keyboardView.setBackground(new Background(new BackgroundFill(LIGHTGREY, null, null)));
        popup.getContent().add(keyboardView);

        final Button keyboardButton = new Button();
        keyboardButton.setGraphic(new ImageView(new Image(getResourceAsStream("images.keyboard-icon.png"))));
        keyboardButton.setOnAction(event -> showPopup(popup, keyboardButton));

        lettersPane.addEventFilter(KEY_TYPED, event -> {
            if (currentPane != null) {
                ArabicLetterType arabicLetterType = getByUnicode(event.getCharacter().charAt(0));
                if (arabicLetterType != null) {
                    currentPane.setLetter(arabicLetterType);
                }
            }
        });

        setRootWord(rootWord);

        getChildren().addAll(lettersPane, keyboardButton);
    }

    private void showPopup(Popup popup, Button keyboardButton) {
        if (popup.isShowing()) {
            popup.hide();
        } else {
            final Bounds bounds = keyboardButton.localToScreen(keyboardButton.getBoundsInLocal());
            popup.show(keyboardButton, bounds.getMinX(), bounds.getMinY() + bounds.getHeight());
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
        });

        firstRadicalPane.letterProperty().addListener((observable, oldValue, newValue) -> {
            getRootWord().setFirstRadical(newValue);
            firstRadicalPane.setSelected(false);
            secondRadicalPane.setSelected(true);
            currentPane = secondRadicalPane;
        });
        secondRadicalPane.letterProperty().addListener((observable, oldValue, newValue) -> {
            getRootWord().setSecondRadical(newValue);
            secondRadicalPane.setSelected(false);
            thirdRadicalPane.setSelected(true);
            currentPane = thirdRadicalPane;
        });
        thirdRadicalPane.letterProperty().addListener((observable, oldValue, newValue) -> {
            getRootWord().setThirdRadical(newValue);
            thirdRadicalPane.setSelected(false);
            fourthRadicalPane.setSelected(true);
            currentPane = fourthRadicalPane;
        });
        fourthRadicalPane.letterProperty().addListener((observable, oldValue, newValue) -> {
            getRootWord().setFourthRadical(newValue);
            fourthRadicalPane.setSelected(false);
            firstRadicalPane.setSelected(true);
            currentPane = firstRadicalPane;
        });

        hBox.getChildren().addAll(fourthRadicalPane, thirdRadicalPane, secondRadicalPane, firstRadicalPane);
        return hBox;
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
