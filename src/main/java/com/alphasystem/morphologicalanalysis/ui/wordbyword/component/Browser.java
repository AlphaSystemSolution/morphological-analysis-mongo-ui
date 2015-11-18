package com.alphasystem.morphologicalanalysis.ui.wordbyword.component;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author sali
 */
public class Browser extends Region {

    private static final int DEFAULT_WIDTH = 750;
    private static final int DEFAULT_HEIGHT = 500;

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public Browser() {
        getChildren().add(browser);
    }

    public void loadUrl(String url) {
        webEngine.load(url);
    }

    @Override
    protected void layoutChildren() {
        layoutInArea(browser, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return DEFAULT_WIDTH;
    }

    @Override
    protected double computePrefHeight(double width) {
        return DEFAULT_HEIGHT;
    }
}
