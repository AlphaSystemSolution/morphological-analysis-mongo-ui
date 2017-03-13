package com.alphasystem.morphologicalengine.ui.control.model;

import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import com.alphasystem.util.GenericPreferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author sali
 */
public final class TabInfo {

    private final ObjectProperty<File> docxFile = new SimpleObjectProperty<>();
    private final ObjectProperty<File> sarfxFile = new SimpleObjectProperty<>();
    private final ObjectProperty<ChartConfiguration> chartConfiguration = new SimpleObjectProperty<>();
    private final BooleanProperty dirty = new SimpleBooleanProperty();
    private final MorphologicalEnginePreferences preferences = GenericPreferences.getInstance(MorphologicalEnginePreferences.class);

    public TabInfo() {
        setDirty(true);
        setChartConfiguration(null);
    }

    public final File getDocxFile() {
        return docxFile.get();
    }

    public final void setDocxFile(File docxFile) {
        this.docxFile.set(docxFile);
    }

    public final File getSarfxFile() {
        return sarfxFile.get();
    }

    public final void setSarfxFile(File sarfxFile) {
        this.sarfxFile.set(sarfxFile);
    }

    public ChartConfiguration getChartConfiguration() {
        return chartConfiguration.get();
    }

    public void setChartConfiguration(ChartConfiguration chartConfiguration) {
        if (chartConfiguration == null) {
            chartConfiguration = new ChartConfiguration();
        }
        String fontFamily = chartConfiguration.getArabicFontFamily();
        fontFamily = isEmpty(fontFamily) ? preferences.getArabicFontName() : fontFamily;
        chartConfiguration.setArabicFontFamily(fontFamily);

        fontFamily = chartConfiguration.getTranslationFontFamily();
        fontFamily = isEmpty(fontFamily) ? preferences.getEnglishFontName() : fontFamily;
        chartConfiguration.setTranslationFontFamily(fontFamily);

        long fontSize = chartConfiguration.getArabicFontSize();
        fontSize = (fontSize <= 0) ? preferences.getArabicFontSize() : fontSize;
        chartConfiguration.setArabicFontSize(fontSize);

        fontSize = chartConfiguration.getTranslationFontSize();
        fontSize = (fontSize <= 0) ? preferences.getEnglishFontSize() : fontSize;
        chartConfiguration.setTranslationFontSize(fontSize);

        fontSize = chartConfiguration.getHeadingFontSize();
        fontSize = (fontSize <= 0) ? preferences.getArabicHeadingFontSize() : fontSize;
        chartConfiguration.setHeadingFontSize(fontSize);
        this.chartConfiguration.set(chartConfiguration);
    }

    public final boolean getDirty() {
        return dirty.get();
    }

    public final void setDirty(boolean dirty) {
        this.dirty.set(dirty);
    }

}
