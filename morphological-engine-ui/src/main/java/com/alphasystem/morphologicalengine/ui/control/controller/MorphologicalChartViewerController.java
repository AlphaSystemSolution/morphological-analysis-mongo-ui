package com.alphasystem.morphologicalengine.ui.control.controller;

import com.alphasystem.app.morphologicalengine.ui.MorphologicalChartView;
import com.alphasystem.fx.ui.Browser;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalengine.model.MorphologicalChart;
import com.alphasystem.morphologicalengine.ui.control.MorphologicalChartViewerControl;
import com.alphasystem.util.AppUtil;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.MalformedURLException;

/**
 * @author sali
 */
@Component
public class MorphologicalChartViewerController extends BorderPane {

    private static final String MAWRID_READER_URL = "http://ejtaal.net/";
    private static final String MAWRID_READER_URL_SUFFIX = "aa/index.html#bwq=";

    private static final String mawridReaderUrl;

    static {
        String urlPrefix = MAWRID_READER_URL;
        File file = new File(AppUtil.USER_HOME_DIR, "Mawrid_Reader");
        if (file.exists()) {
            try {
                urlPrefix = file.toURI().toURL().toExternalForm();
            } catch (MalformedURLException e) {
                // ignore
            }
        }
        mawridReaderUrl = String.format("%s%s", urlPrefix, MAWRID_READER_URL_SUFFIX);
    }

    private final MorphologicalChartViewerControl control;
    private final MorphologicalChartView morphologicalChartControl;
    private Browser browser;
    private final TabPane tabPane;

    public MorphologicalChartViewerController(@Autowired MorphologicalChartViewerControl control,
                                              @Autowired MorphologicalChartView morphologicalChartControl) {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Platform.runLater(() -> {
            browser = new Browser();
            browser.getWebEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (Worker.State.READY.equals(newValue) || Worker.State.SUCCEEDED.equals(newValue) || Worker.State.FAILED.equals(newValue)) {
                    UiUtilities.defaultCursor(control);
                }
            });

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(UiUtilities.wrapInScrollPane(morphologicalChartControl));
            tabPane.getTabs().addAll(new Tab("Morphological Chart", borderPane), new Tab("Dictionary", browser));

            setCenter(tabPane);
        });
        this.control = control;
        this.morphologicalChartControl = morphologicalChartControl;
    }

    @PostConstruct
    void postConstruct() {
        initialize(control.getMorphologicalChart());
        this.control.morphologicalChartProperty().addListener((observable, oldValue, newValue) -> initialize(newValue));
    }

    private void initialize(MorphologicalChart morphologicalChart) {
        tabPane.getSelectionModel().select(0);
        morphologicalChartControl.setMorphologicalChart(null);
        morphologicalChartControl.setMorphologicalChart(morphologicalChart);
        loadDictionary();
    }

    private void loadUrl() {
        if (browser == null) {
            return;
        }
        RootLetters rootLetters = null;
        final MorphologicalChart morphologicalChart = control.getMorphologicalChart();
        if (morphologicalChart != null) {
            rootLetters = morphologicalChart.rootLetters();
        }
        if (rootLetters == null || rootLetters.isEmpty()) {
            browser.loadUrl(getMawridReaderUrl("a"));
            return;
        }
        // not sure whether display is initialized or not
        rootLetters.initDisplayName();
        browser.loadUrl(getMawridReaderUrl(rootLetters.toMawridSearchString()));
    }

    private void loadDictionary() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        UiUtilities.waitCursor(control);
                        Platform.runLater(MorphologicalChartViewerController.this::loadUrl);
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded(event -> UiUtilities.defaultCursor(control));
        service.setOnFailed(event -> UiUtilities.defaultCursor(control));
        service.start();
    }

    private static String getMawridReaderUrl(String query) {
        return String.format("%s%s", mawridReaderUrl, query);
    }

}
