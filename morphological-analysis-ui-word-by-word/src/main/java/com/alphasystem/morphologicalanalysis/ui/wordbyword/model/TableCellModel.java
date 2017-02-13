package com.alphasystem.morphologicalanalysis.ui.wordbyword.model;


import com.alphasystem.arabic.model.ArabicTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static java.lang.String.format;

/**
 * @author sali
 */
public final class TableCellModel {

    private final BooleanProperty checked;
    private final ReadOnlyStringWrapper displayName;
    private final ReadOnlyStringWrapper text;
    private final StringProperty morphologicalDescription;
    private Token token;

    public TableCellModel(Token token) {
        this.token = token;
        checked = new SimpleBooleanProperty(false);
        displayName = new ReadOnlyStringWrapper(getTokenNumber(this.token));
        text = new ReadOnlyStringWrapper((token == null) ? "" : token.tokenWord().toUnicode());
        morphologicalDescription = new SimpleStringProperty();
    }

    private static String getTokenNumber(Token token) {
        if (token == null) {
            return "";
        }
        String chapterNumber = ArabicTool.getArabicNumberWord(token.getChapterNumber()).toUnicode();
        String verseNumber = ArabicTool.getArabicNumberWord(token.getVerseNumber()).toUnicode();
        String tokenNumber = ArabicTool.getArabicNumberWord(token.getTokenNumber()).toUnicode();
        return format("(%s:%s:%s)", chapterNumber, verseNumber, tokenNumber);
    }

    private static String getLocationNumber(Location location) {
        if (location == null) {
            return "";
        }
        String chapterNumber = ArabicTool.getArabicNumberWord(location.getChapterNumber()).toUnicode();
        String verseNumber = ArabicTool.getArabicNumberWord(location.getVerseNumber()).toUnicode();
        String tokenNumber = ArabicTool.getArabicNumberWord(location.getTokenNumber()).toUnicode();
        String locationNumber = ArabicTool.getArabicNumberWord(location.getLocationNumber()).toUnicode();
        return format("(%s:%s:%s:%s)", chapterNumber, verseNumber, tokenNumber, locationNumber);
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
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

    public final String getDisplayName() {
        return displayName.get();
    }

    public final ReadOnlyStringProperty displayNameProperty() {
        return displayName.getReadOnlyProperty();
    }

    public final String getText() {
        return textProperty().get();
    }

    public final ReadOnlyStringProperty textProperty() {
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

