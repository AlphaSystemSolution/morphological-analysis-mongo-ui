package com.alphasystem.morphologicalengine.ui.control;

import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.morphologicalengine.ui.control.skin.ChartConfigurationSkin;
import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.PageOption;
import com.alphasystem.morphologicalanalysis.morphology.model.support.PageOrientation;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirection;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirective;
import com.alphasystem.util.GenericPreferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import static com.alphasystem.util.AppUtil.getResource;

/**
 * @author sali
 */
public class ChartConfigurationView extends Control {

    private final MorphologicalEnginePreferences preferences = GenericPreferences.getInstance(MorphologicalEnginePreferences.class);
    private final ObjectProperty<ChartConfiguration> chartConfiguration = new SimpleObjectProperty<>();
    private final BooleanProperty omitToc = new SimpleBooleanProperty();
    private final BooleanProperty omitAbbreviatedConjugation = new SimpleBooleanProperty();
    private final BooleanProperty omitDetailedConjugation = new SimpleBooleanProperty();
    private final BooleanProperty omitTitle = new SimpleBooleanProperty();
    private final BooleanProperty omitHeader = new SimpleBooleanProperty();
    private final BooleanProperty omitSarfTermCaption = new SimpleBooleanProperty();
    private final ObjectProperty<SortDirective> sortDirective = new SimpleObjectProperty<>();
    private final ObjectProperty<SortDirection> sortDirection = new SimpleObjectProperty<>();
    private final StringProperty arabicFontFamily = new SimpleStringProperty(this, "arabicFontFamily");
    private final StringProperty arabicUiFontFamily = new SimpleStringProperty(this, "arabicUiFontFamily");
    private final StringProperty translationFontFamily = new SimpleStringProperty(this, "translationFontFamily");
    private final StringProperty translationUiFontFamily = new SimpleStringProperty(this, "translationUiFontFamily");
    private final LongProperty arabicFontSize = new SimpleLongProperty(this, "arabicFontSize");
    private final LongProperty arabicUiFontSize = new SimpleLongProperty(this, "arabicUiFontSize");
    private final LongProperty translationFontSize = new SimpleLongProperty(this, "translationFontSize");
    private final LongProperty translationUiFontSize = new SimpleLongProperty(this, "translationUiFontSize");
    private final LongProperty headingFontSize = new SimpleLongProperty(this, "headingFontSize");
    private final LongProperty headingUiFontSize = new SimpleLongProperty(this, "headingUiFontSize");
    private final ObjectProperty<PageOrientation> pageOrientation = new SimpleObjectProperty<>(this, "pageOrientation", PageOrientation.PORTRAIT);

    public ChartConfigurationView() {
        chartConfiguration.addListener((o, ov, nv) -> {
            setOmitAbbreviatedConjugation(nv.isOmitAbbreviatedConjugation());
            setOmitDetailedConjugation(nv.isOmitDetailedConjugation());
            setOmitHeader(nv.isOmitHeader());
            setOmitSarfTermCaption(nv.isOmitSarfTermCaption());
            setOmitTitle(nv.isOmitTitle());
            setOmitToc(nv.isOmitToc());
            setSortDirection(nv.getSortDirection());
            setSortDirective(nv.getSortDirective());

            String fontFamily = nv.getArabicFontFamily();
            fontFamily = (fontFamily == null) ? preferences.getArabicFontName() : fontFamily;
            setArabicFontFamily(fontFamily);
            setArabicUiFontFamily(preferences.getArabicFontName());

            fontFamily = nv.getTranslationFontFamily();
            fontFamily = (fontFamily == null) ? preferences.getEnglishFontName() : fontFamily;
            setTranslationFontFamily(fontFamily);
            setTranslationUiFontFamily(preferences.getEnglishFontName());

            long size = nv.getArabicFontSize();
            size = (size <= 0) ? preferences.getArabicFontSize() : size;
            setArabicFontSize(size);
            setArabicUiFontSize(preferences.getArabicFontSize());

            size = nv.getTranslationFontSize();
            size = (size <= 0) ? preferences.getEnglishFontSize() : size;
            setTranslationFontSize(size);
            setTranslationUiFontSize(preferences.getEnglishFontSize());

            size = nv.getHeadingFontSize();
            size = (size <= 0) ? preferences.getArabicHeadingFontSize() : size;
            setHeadingFontSize(size);
            setHeadingUiFontSize(preferences.getArabicHeadingFontSize());

            final PageOption pageOption = nv.getPageOption();
            setPageOrientation(pageOption.getOrientation());
        });

        omitAbbreviatedConjugationProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitAbbreviatedConjugation(nv));
        omitDetailedConjugationProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitDetailedConjugation(nv));
        omitHeaderProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitHeader(nv));
        omitSarfTermCaptionProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitSarfTermCaption(nv));
        omitTitleProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitTitle(nv));
        omitTocProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitToc(nv));
        sortDirectionProperty().addListener((o, ov, nv) -> getChartConfiguration().setSortDirection(nv));
        sortDirectiveProperty().addListener((o, ov, nv) -> getChartConfiguration().setSortDirective(nv));
        arabicFontFamilyProperty().addListener((o, ov, nv) -> getChartConfiguration().setArabicFontFamily(nv));
        arabicUiFontFamilyProperty().addListener((o, ov, nv) -> preferences.setArabicFontName(nv));
        translationFontFamilyProperty().addListener((o, ov, nv) -> getChartConfiguration().setTranslationFontFamily(nv));
        translationUiFontFamilyProperty().addListener((o, ov, nv) -> preferences.setEnglishFontName(nv));
        arabicFontSizeProperty().addListener((o, ov, nv) -> getChartConfiguration().setArabicFontSize((Long) nv));
        arabicUiFontSizeProperty().addListener((o, ov, nv) -> preferences.setArabicFontSize((Long) nv));
        translationFontSizeProperty().addListener((o, ov, nv) -> getChartConfiguration().setTranslationFontSize((Long) nv));
        translationUiFontSizeProperty().addListener((o, ov, nv) -> preferences.setEnglishFontSize((Long) nv));
        headingFontSizeProperty().addListener((o, ov, nv) -> getChartConfiguration().setHeadingFontSize((Long) nv));
        headingUiFontSizeProperty().addListener((o, ov, nv) -> preferences.setArabicHeadingFontSize((Long) nv));
        pageOrientationProperty().addListener((o, ov, nv) -> getChartConfiguration().getPageOption().setOrientation(nv));

        setChartConfiguration(null);
        setMinWidth(600);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getResource("arabic-ui-support.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ChartConfigurationSkin(this);
    }

    final ChartConfiguration getChartConfiguration() {
        return chartConfiguration.get();
    }

    final void setChartConfiguration(ChartConfiguration chartConfiguration) {
        this.chartConfiguration.set(chartConfiguration == null ? new ChartConfiguration() : chartConfiguration);
    }

    public final BooleanProperty omitTocProperty() {
        return omitToc;
    }

    private void setOmitToc(boolean omitToc) {
        this.omitToc.set(omitToc);
    }

    public final BooleanProperty omitAbbreviatedConjugationProperty() {
        return omitAbbreviatedConjugation;
    }

    private void setOmitAbbreviatedConjugation(boolean omitAbbreviatedConjugation) {
        this.omitAbbreviatedConjugation.set(omitAbbreviatedConjugation);
    }

    public final BooleanProperty omitDetailedConjugationProperty() {
        return omitDetailedConjugation;
    }

    private void setOmitDetailedConjugation(boolean omitDetailedConjugation) {
        this.omitDetailedConjugation.set(omitDetailedConjugation);
    }

    public final BooleanProperty omitTitleProperty() {
        return omitTitle;
    }

    private void setOmitTitle(boolean omitTitle) {
        this.omitTitle.set(omitTitle);
    }

    public final BooleanProperty omitHeaderProperty() {
        return omitHeader;
    }

    private void setOmitHeader(boolean omitHeader) {
        this.omitHeader.set(omitHeader);
    }

    public final BooleanProperty omitSarfTermCaptionProperty() {
        return omitSarfTermCaption;
    }

    private void setOmitSarfTermCaption(boolean omitSarfTermCaption) {
        this.omitSarfTermCaption.set(omitSarfTermCaption);
    }

    public final ObjectProperty<SortDirective> sortDirectiveProperty() {
        return sortDirective;
    }

    private void setSortDirective(SortDirective sortDirective) {
        this.sortDirective.set(sortDirective);
    }

    public final ObjectProperty<SortDirection> sortDirectionProperty() {
        return sortDirection;
    }

    private void setSortDirection(SortDirection sortDirection) {
        this.sortDirection.set(sortDirection);
    }

    public final String getArabicFontFamily() {
        return arabicFontFamily.get();
    }

    public final StringProperty arabicFontFamilyProperty() {
        return arabicFontFamily;
    }

    private void setArabicFontFamily(String arabicFontFamily) {
        this.arabicFontFamily.set(arabicFontFamily);
    }

    public final StringProperty arabicUiFontFamilyProperty() {
        return arabicUiFontFamily;
    }

    private void setArabicUiFontFamily(String arabicUiFontFamily) {
        this.arabicUiFontFamily.set(arabicUiFontFamily);
    }

    public final String getTranslationFontFamily() {
        return translationFontFamily.get();
    }

    public final StringProperty translationFontFamilyProperty() {
        return translationFontFamily;
    }

    private void setTranslationFontFamily(String translationFontFamily) {
        this.translationFontFamily.set(translationFontFamily);
    }

    public final StringProperty translationUiFontFamilyProperty() {
        return translationUiFontFamily;
    }

    private void setTranslationUiFontFamily(String translationUiFontFamily) {
        this.translationUiFontFamily.set(translationUiFontFamily);
    }

    public final long getArabicFontSize() {
        return arabicFontSize.get();
    }

    public LongProperty arabicFontSizeProperty() {
        return arabicFontSize;
    }

    public final void setArabicFontSize(long arabicFontSize) {
        this.arabicFontSize.set(arabicFontSize);
    }

    public final long getArabicUiFontSize() {
        return arabicUiFontSize.get();
    }

    public final LongProperty arabicUiFontSizeProperty() {
        return arabicUiFontSize;
    }

    public final void setArabicUiFontSize(long arabicUiFontSize) {
        this.arabicUiFontSize.set(arabicUiFontSize);
    }

    public final long getTranslationFontSize() {
        return translationFontSize.get();
    }

    public final LongProperty translationFontSizeProperty() {
        return translationFontSize;
    }

    public final void setTranslationFontSize(long translationFontSize) {
        this.translationFontSize.set(translationFontSize);
    }

    public final long getTranslationUiFontSize() {
        return translationUiFontSize.get();
    }

    public final LongProperty translationUiFontSizeProperty() {
        return translationUiFontSize;
    }

    public final void setTranslationUiFontSize(long translationUiFontSize) {
        this.translationUiFontSize.set(translationUiFontSize);
    }

    public final long getHeadingFontSize() {
        return headingFontSize.get();
    }

    public final LongProperty headingFontSizeProperty() {
        return headingFontSize;
    }

    public final void setHeadingFontSize(long headingFontSize) {
        this.headingFontSize.set(headingFontSize);
    }

    public final long getHeadingUiFontSize() {
        return headingUiFontSize.get();
    }

    public final LongProperty headingUiFontSizeProperty() {
        return headingUiFontSize;
    }

    public final void setHeadingUiFontSize(long headingUiFontSize) {
        this.headingUiFontSize.set(headingUiFontSize);
    }

    public final PageOrientation getPageOrientation() {
        return pageOrientation.get();
    }

    public final ObjectProperty<PageOrientation> pageOrientationProperty() {
        return pageOrientation;
    }

    public final void setPageOrientation(PageOrientation pageOrientation) {
        this.pageOrientation.set((pageOrientation == null) ? PageOrientation.PORTRAIT : pageOrientation);
    }
}
