package com.alphasystem.morphologicalanalysis.wordbyword.ui.model;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.*;

import static com.alphasystem.arabic.model.ArabicWord.getArabicNumber;
import static java.lang.String.format;

/**
 * @author sali
 */
public final class TableCellModel {

    private final Token token;
    private final BooleanProperty checked;
    private final ReadOnlyStringWrapper tokenNumber;
    private final ReadOnlyStringWrapper tokenText;
    private final StringProperty morphologicalDescription;

    public TableCellModel(Token token) {
        this.token = token;
        checked = new SimpleBooleanProperty(false);
        tokenNumber = new ReadOnlyStringWrapper(getTokenNumber(this.token));
        tokenText = new ReadOnlyStringWrapper(token.getTokenWord().toUnicode());
        morphologicalDescription = new SimpleStringProperty();
    }

    private static String getTokenNumber(Token token) {
        if (token == null) {
            return "";
        }
        String chapterNumber = getArabicNumber(token.getChapterNumber())
                .toUnicode();
        String verseNumber = getArabicNumber(token.getVerseNumber())
                .toUnicode();
        String tokenNumber = getArabicNumber(token.getTokenNumber())
                .toUnicode();
        return format("(%s:%s:%s)", chapterNumber, verseNumber, tokenNumber);
    }

    public final Token getToken() {
        return token;
    }

    public final boolean isChecked() {
        return checked.get();
    }

    public final void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public final BooleanProperty checkedProperty() {
        return checked;
    }

    public final String getTokenNumber() {
        return tokenNumber.get();
    }

    public final ReadOnlyStringProperty tokenNumberProperty() {
        return tokenNumber.getReadOnlyProperty();
    }

    public final String getTokenText() {
        return tokenTextProperty().get();
    }

    public final ReadOnlyStringProperty tokenTextProperty() {
        return tokenText.getReadOnlyProperty();
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
