package com.alphasystem.morphologicalanalysis.treebank.ui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Bean to capture canvas meta data.
 *
 * @author sali
 */
public class CanvasMetaData {

    private final IntegerProperty width;
    private final IntegerProperty height;
    private final BooleanProperty showGridLines;
    private final BooleanProperty showOutLines;
    private final BooleanProperty debugMode;

    public CanvasMetaData() {
        this(800, 400, true, true, false);
    }

    public CanvasMetaData(int width, int height, boolean showGridLines, boolean showOutLines, boolean debugMode) {
        this.width = new SimpleIntegerProperty(width);
        this.height = new SimpleIntegerProperty(height);
        this.showGridLines = new SimpleBooleanProperty(showGridLines);
        this.showOutLines = new SimpleBooleanProperty(showOutLines);
        this.debugMode = new SimpleBooleanProperty(debugMode);
    }

    public final boolean isDebugMode() {
        return debugMode.get();
    }

    public final void setDebugMode(boolean debugMode) {
        this.debugMode.set(debugMode);
    }

    public final BooleanProperty debugModeProperty() {
        return debugMode;
    }

    public int getHeight() {
        return height.get();
    }

    public void setHeight(int height) {
        this.height.set(height);
    }

    public final IntegerProperty heightProperty() {
        return height;
    }

    public boolean isShowGridLines() {
        return showGridLines.get();
    }

    public void setShowGridLines(boolean showGridLines) {
        this.showGridLines.set(showGridLines);
    }

    public final BooleanProperty showGridLinesProperty() {
        return showGridLines;
    }

    public boolean isShowOutLines() {
        return showOutLines.get();
    }

    public void setShowOutLines(boolean showOutLines) {
        this.showOutLines.set(showOutLines);
    }

    public final BooleanProperty showOutLinesProperty() {
        return showOutLines;
    }

    public int getWidth() {
        return width.get();
    }

    public void setWidth(int width) {
        this.width.set(width);
    }

    public IntegerProperty widthProperty() {
        return width;
    }
}
