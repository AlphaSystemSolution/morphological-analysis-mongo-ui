package com.alphasystem.morphologicalanalysis.treebank.jfx.ui;

import javafx.scene.Scene;

/**
 * @author sali
 */
public class Global {

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
