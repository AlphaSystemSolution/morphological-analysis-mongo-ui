package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.morphologicalengine.ui.control.skin.MorphologicalEngineSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author sali
 */
@Component
public class MorphologicalEngineView extends Control {

    private final ObjectProperty<Font> arabicFontProperty = new SimpleObjectProperty<>(this, "font");
    private final ObjectProperty<File> fileProperty = new SimpleObjectProperty<>(this, "file");

    public final ObjectProperty<Font> arabicFontPropertyProperty() {
        return arabicFontProperty;
    }

    @PostConstruct
    void postConstruct(){
    }

    public final void setArabicFontProperty(Font arabicFontProperty) {
        this.arabicFontProperty.set(arabicFontProperty);
    }

    public final ObjectProperty<File> filePropertyProperty() {
        return fileProperty;
    }

    public final void setFileProperty(File fileProperty) {
        this.fileProperty.set(fileProperty);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MorphologicalEngineSkin(this);
    }
}
