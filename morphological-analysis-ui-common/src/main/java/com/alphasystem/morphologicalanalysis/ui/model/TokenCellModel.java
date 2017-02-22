package com.alphasystem.morphologicalanalysis.ui.model;


import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.model.ArabicLetters;
import com.alphasystem.arabic.model.ArabicTool;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author sali
 */
public final class TokenCellModel {

    private final BooleanProperty checked;
    private final ReadOnlyObjectWrapper<ArabicWord> displayName = new ReadOnlyObjectWrapper<>(this, "displayName");
    private final ReadOnlyObjectWrapper<ArabicWord> text = new ReadOnlyObjectWrapper<>(this, "text");
    private final StringProperty morphologicalDescription;
    private Token token;

    public TokenCellModel(Token token) {
        this.token = token;
        checked = new SimpleBooleanProperty(false);
        displayName.set(getTokenNumber(this.token));
        text.set(getText(this.token));
        morphologicalDescription = new SimpleStringProperty();
    }

    private static ArabicWord getTokenNumber(Token token) {
        if (token == null) {
            return ArabicLetters.WORD_SPACE;
        }
        ArabicWord chapterNumber = ArabicTool.getArabicNumberWord(token.getChapterNumber());
        ArabicWord verseNumber = ArabicTool.getArabicNumberWord(token.getVerseNumber());
        ArabicWord tokenNumber = ArabicTool.getArabicNumberWord(token.getTokenNumber());
        ArabicWord.concatenate(chapterNumber, ArabicWord.getWord(ArabicLetterType.SEMI_COLON), verseNumber,
                ArabicWord.getWord(ArabicLetterType.SEMI_COLON), tokenNumber);
        return ArabicWord.concatenate(chapterNumber, ArabicWord.getWord(ArabicLetterType.SEMI_COLON), verseNumber,
                ArabicWord.getWord(ArabicLetterType.SEMI_COLON), tokenNumber);
    }

    private static ArabicWord getText(Token token){
        if (token == null) {
            return ArabicLetters.WORD_SPACE;
        }
        return token.tokenWord();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isChecked() {
        return checked.get();
    }

    public final BooleanProperty checkedProperty() {
        return checked;
    }

    public final ArabicWord getDisplayName() {
        return displayName.get();
    }

    public final ReadOnlyObjectProperty<ArabicWord> displayNameProperty() {
        return displayName.getReadOnlyProperty();
    }

    public final ArabicWord getText() {
        return textProperty().get();
    }

    public final ReadOnlyObjectProperty<ArabicWord> textProperty() {
        return text.getReadOnlyProperty();
    }

    public final String getMorphologicalDescription() {
        return morphologicalDescriptionProperty().get();
    }

    public final void setMorphologicalDescription(String value) {
        morphologicalDescriptionProperty().setValue(value);
    }

    public final StringProperty morphologicalDescriptionProperty() {
        return morphologicalDescription;
    }
}

