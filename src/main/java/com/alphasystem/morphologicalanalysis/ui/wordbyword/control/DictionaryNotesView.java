package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.DictionaryNotesSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import static java.lang.String.format;

/**
 * @author sali
 */
public class DictionaryNotesView extends Control {

    private final ObjectProperty<DictionaryNotes> dictionaryNotes = new SimpleObjectProperty<>(null, "dictionaryNotes");
    private final ReadOnlyStringWrapper previewFileName = new ReadOnlyStringWrapper(null, "previewFileName");

    public DictionaryNotesView() {
        dictionaryNotesProperty().addListener((o, ov, nv) -> {
            boolean disable = (nv == null) || nv.isEmpty();
            setDisable(disable);
            if (!disable) {
                RootLetters rootLetters = nv.getRootLetters();
                previewFileName.setValue(format("%s.html", rootLetters.getDisplayName()));
            }
        });
        setDisable(true);
    }

    public final DictionaryNotes getDictionaryNotes() {
        return dictionaryNotes.get();
    }

    public final void setDictionaryNotes(DictionaryNotes dictionaryNotes) {
        this.dictionaryNotes.set(dictionaryNotes);
    }

    public final ObjectProperty<DictionaryNotes> dictionaryNotesProperty() {
        return dictionaryNotes;
    }

    public final String getPreviewFileName() {
        return previewFileName.get();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DictionaryNotesSkin(this);
    }
}
