package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.DictionaryNotesSkin;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.io.File;
import java.net.MalformedURLException;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.DEFAULT_DICTIONARY_DIRECTORY;
import static java.lang.String.format;

/**
 * @author sali
 */
public class DictionaryNotesView extends Control {

    private final ObjectProperty<DictionaryNotes> dictionaryNotes = new SimpleObjectProperty<>(null, "dictionaryNotes");
    private final ObjectProperty<RootLetters> rootLetters = new SimpleObjectProperty<>(null, "rootLetters");
    private final StringProperty notes = new SimpleStringProperty(null, "notes");
    private final ReadOnlyObjectWrapper<File> previewFile = new ReadOnlyObjectWrapper<>(null, "previewFile");
    private final ReadOnlyStringWrapper previewUrl = new ReadOnlyStringWrapper(null, "previewUrl");

    public DictionaryNotesView() {
        dictionaryNotesProperty().addListener((o, ov, nv) -> {
            boolean disable = (nv == null) || nv.isEmpty();
            setDisable(disable);
            previewFile.setValue(null);
            previewUrl.setValue(null);
            setRootLetters(disable ? null : nv.getRootLetters());
            setNotes(disable ? null : nv.getNotes());
        });
        setDisable(true);
        rootLettersProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                String fileName = format("%s.html", nv.getDisplayName());
                File file = new File(DEFAULT_DICTIONARY_DIRECTORY, fileName);
                previewFile.setValue(file);
                String url = null;
                try {
                    url = file.toURI().toURL().toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                previewUrl.setValue(url);
                getDictionaryNotes().setRootLetters(nv);
            }
        });
        notesProperty().addListener((o, ov, nv) -> {
            DictionaryNotes dictionaryNotes = getDictionaryNotes();
            if (dictionaryNotes != null) {
                dictionaryNotes.setNotes(nv);
            }
        });
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

    public final RootLetters getRootLetters() {
        return rootLetters.get();
    }

    public final void setRootLetters(RootLetters rootLetters) {
        this.rootLetters.set(rootLetters);
    }

    public final ObjectProperty<RootLetters> rootLettersProperty() {
        return rootLetters;
    }

    public final String getNotes() {
        return notes.get();
    }

    public final void setNotes(String notes) {
        this.notes.set(notes);
    }

    public final StringProperty notesProperty() {
        return notes;
    }

    public final File getPreviewFile() {
        return previewFile.get();
    }

    public final String getPreviewUrl() {
        return previewUrl.get();
    }

    public final ReadOnlyStringProperty previewUrlProperty() {
        return previewUrl.getReadOnlyProperty();
    }

    public final void selectSource() {
        ((DictionaryNotesSkin) getSkin()).selectSource();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        DictionaryNotesSkin skin = new DictionaryNotesSkin(this);
        setSkin(skin);
        return skin;
    }
}
