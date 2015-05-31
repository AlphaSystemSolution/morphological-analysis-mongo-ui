package com.alphasystem.morphologicalanalysis.treebank.jfx.ui;

import javafx.scene.Scene;
import javafx.scene.text.Font;

import java.util.ResourceBundle;

import static java.lang.String.format;
import static javafx.scene.text.Font.font;
import static javafx.scene.text.FontPosture.REGULAR;

/**
 * @author sali
 */
public class Global {

    public static final String TREE_BANK_STYLE_SHEET = Global.class.getResource("/treebank.css").toExternalForm();
    public static final String ARABIC_FONT_NAME = System.getProperty("arabic-font-name", "Traditional Arabic");
    public static final Font ARABIC_FONT_BIG = font(ARABIC_FONT_NAME, REGULAR, 48);
    public static final Font ARABIC_FONT_SMALL = font(ARABIC_FONT_NAME, REGULAR, 24);
    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("resources");
    private static Global instance;
    private Scene globalScene;

    private Global() {
    }

    public synchronized static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    public static String getLabel(Class<?> klass, String label) {
        String key = format("%s.%s.label", klass.getSimpleName(), label);
        return RESOURCE_BUNDLE.getString(key);
    }
}
