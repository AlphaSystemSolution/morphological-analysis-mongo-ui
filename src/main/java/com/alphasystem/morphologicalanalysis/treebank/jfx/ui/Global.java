package com.alphasystem.morphologicalanalysis.treebank.jfx.ui;

import javafx.scene.Scene;

import java.util.ResourceBundle;

/**
 * @author sali
 */
public class Global {

    public static final String TREE_BANK_STYLE_SHEET = Global.class.getResource("/treebank.css").toExternalForm();
    public static final String ARABIC_FONT_NAME = System.getProperty("arabic-font-name", "Traditional Arabic");
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

    public Scene getGlobalScene() {
        return globalScene;
    }

    public void setGlobalScene(Scene globalScene) {
        this.globalScene = globalScene;
    }
}
