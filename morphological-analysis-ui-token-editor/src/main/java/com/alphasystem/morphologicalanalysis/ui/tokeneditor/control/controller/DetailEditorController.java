package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.app.morphologicalengine.conjugation.builder.ConjugationHelper;
import com.alphasystem.app.morphologicalengine.conjugation.builder.ConjugationRoots;
import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.app.morphologicalengine.guice.GuiceSupport;
import com.alphasystem.app.morphologicalengine.ui.MorphologicalChartView;
import com.alphasystem.fx.ui.Browser;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.DetailEditorView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author sali
 */
@Component
public class DetailEditorController extends BorderPane {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(DetailEditorView.class.getSimpleName());
    @Autowired private DetailEditorView control;
    @Autowired private LocationPropertiesView locationPropertiesView;
    @Autowired private MorphologicalChartView morphologicalChartView;
    private Browser browser;
    private final TabPane tabPane = new TabPane();

    public DetailEditorController() {
        Platform.runLater(() -> {
            browser = new Browser();
            final WebEngine webEngine = browser.getWebEngine();
            webEngine.setPromptHandler(param -> {
                TextInputDialog dialog = new TextInputDialog(param.getDefaultValue());
                dialog.setHeaderText(param.getMessage());
                final Optional<String> result = dialog.showAndWait();
                return result.isPresent() ? result.get() : param.getDefaultValue();
            });
            browser.getWebEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (Worker.State.READY.equals(newValue) || Worker.State.SUCCEEDED.equals(newValue) || Worker.State.FAILED.equals(newValue)) {
                    UiUtilities.defaultCursor(control);
                }
            });
            browser.loadUrl(ApplicationHelper.getMawridReaderUrl("a"));
        });
    }

    @PostConstruct
    void postConstruct() {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        control.locationProperty().addListener((observable, oldValue, newValue) -> refresh(newValue));
        setCenter(tabPane);
        initializeSkin();
        refresh(control.getLocation());
    }

    private void initializeSkin() {
        locationPropertiesView.setLocation(control.getLocation());
        Tab locationPropertiesViewTab = new Tab(RESOURCE_BUNDLE.getString("locationPropertiesView.label"), locationPropertiesView);
        Tab dictionaryTab = new Tab(RESOURCE_BUNDLE.getString("dictionaryTab.label"), browser);
        dictionaryTab.selectedProperty().addListener((observable, oldValue, newValue) -> loadDictionary());
        Tab morphologicalChartViewTab = new Tab(RESOURCE_BUNDLE.getString("morphologicalChartView.label"),
                UiUtilities.wrapInScrollPane(morphologicalChartView));
        morphologicalChartViewTab.setDisable(true);
        morphologicalChartViewTab.disableProperty().bind(locationPropertiesView.morphologicalEntryProperty().not());
        tabPane.getTabs().addAll(locationPropertiesViewTab, dictionaryTab, morphologicalChartViewTab);
    }

    private void refresh(Location location) {
        locationPropertiesView.setLocation(location);
        if (location != null) {
            loadConjugation(location.getMorphologicalEntry());
        }
    }

    private void loadDictionary() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        UiUtilities.waitCursor(control);
                        Platform.runLater(DetailEditorController.this::loadUrl);
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded(event -> UiUtilities.defaultCursor(control));
        service.setOnFailed(event -> UiUtilities.defaultCursor(control));
        service.start();
    }

    private void loadUrl() {
        RootLetters rootLetters = null;
        final Location location = control.getLocation();
        if (location != null) {
            rootLetters = location.getMorphologicalEntry().getRootLetters();
        }
        if (rootLetters == null || rootLetters.isEmpty()) {
            return;
        }
        // not sure whether display is initialized or not
        rootLetters.initDisplayName();
        browser.loadUrl(ApplicationHelper.getMawridReaderUrl(rootLetters.toMawridSearchString()));
    }

    private void loadConjugation(final MorphologicalEntry entry) {
        if (entry != null) {
            Service<MorphologicalChart> service = new Service<MorphologicalChart>() {
                @Override
                protected Task<MorphologicalChart> createTask() {
                    return new Task<MorphologicalChart>() {
                        @Override
                        protected MorphologicalChart call() throws Exception {
                            UiUtilities.waitCursor(control);
                            RootLetters rootLetters = entry.getRootLetters();
                            final ConjugationRoots conjugationRoots = ConjugationHelper.getConjugationRoots(entry)
                                    .conjugationConfiguration(entry.getConfiguration());
                            return GuiceSupport.getInstance().getConjugationBuilder().doConjugation(conjugationRoots,
                                    rootLetters.getFirstRadical(), rootLetters.getSecondRadical(), rootLetters.getThirdRadical(),
                                    rootLetters.getFourthRadical());
                        }
                    };
                }
            };
            service.setOnSucceeded(event -> {
                UiUtilities.defaultCursor(control);
                MorphologicalChart morphologicalChart = (MorphologicalChart) event.getSource().getValue();
                morphologicalChartView.setMorphologicalChart(null);
                morphologicalChartView.setMorphologicalChart(morphologicalChart);
            });
            service.setOnFailed(event -> UiUtilities.defaultCursor(control));
            service.start();
        }
    }
}
