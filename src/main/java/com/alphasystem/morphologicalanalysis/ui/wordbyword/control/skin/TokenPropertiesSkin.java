package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.app.sarfengine.conjugation.builder.ConjugationBuilder;
import com.alphasystem.app.sarfengine.conjugation.model.SarfChart;
import com.alphasystem.app.sarfengine.guice.GuiceSupport;
import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.arabic.ui.Browser;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.model.support.NounOfPlaceAndTime;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalanalysis.ui.common.LocationListCell;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.DictionaryNotesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.SarfChartView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.arabic.ui.util.FontConstants.ARABIC_FONT_36;
import static com.alphasystem.arabic.ui.util.UiUtilities.*;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.*;
import static com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView.SelectionStatus.*;
import static java.lang.String.format;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

/**
 * @author sali
 */
public class TokenPropertiesSkin extends SkinBase<TokenPropertiesView> {

    private final ComboBox<Location> locationComboBox;
    private final ArabicLabelToggleGroup group;
    private final LocationPropertiesView locationPropertiesView;
    private final BorderPane lettersPane;
    private final TabPane tabPane;
    private final Browser browser;
    private final SarfChartView conjugationViewer;
    private final DictionaryNotesView dictionaryNotesView;
    private final Tab browseDictionaryTab;
    private final Tab morphologicalConjugationTab;
    private final Tab dictionaryNotesTab;
    private ConjugationBuilder conjugationBuilder = GuiceSupport.getInstance().getConjugationBuilderFactory()
            .getConjugationBuilder();
    private MorphologicalAnalysisRepositoryUtil repositoryUtil = RepositoryTool.getInstance().getRepositoryUtil();

    public TokenPropertiesSkin(TokenPropertiesView control) {
        super(control);

        lettersPane = new BorderPane();
        lettersPane.setBorder(BORDER);

        browser = new Browser();
        conjugationViewer = new SarfChartView();
        dictionaryNotesView = new DictionaryNotesView();

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setBorder(BORDER);

        browseDictionaryTab = new Tab("Browse Dictionary", browser);
        morphologicalConjugationTab = new Tab("Morphological Conjugation", wrapInScrollPane(conjugationViewer));

        dictionaryNotesTab = new Tab("Dictionary Notes", dictionaryNotesView);
        dictionaryNotesTab.selectedProperty().addListener((o, ov, nv) -> dictionaryNotesView.selectSource());

        locationComboBox = new ComboBox<>();
        locationPropertiesView = new LocationPropertiesView();

        group = new ArabicLabelToggleGroup();
        group.setWidth(64);
        group.setHeight(64);
        group.setFont(ARABIC_FONT_36);

        initializeSkin();

        locationPropertiesView.rootLettersProperty().addListener((o, ov, nv) -> {
            loadDictionary(nv);
            loadDictionaryNotes(nv);
        });
        locationPropertiesView.formProperty().addListener((o, ov, nv) -> {
            // leave it for any future need
            // looks like we do not need it now
        });

        BooleanBinding disableTab = locationPropertiesView.rootLettersProperty().isNull()
                .and(locationPropertiesView.emptyRootLettersProperty());
        browseDictionaryTab.disableProperty().bind(disableTab);
        morphologicalConjugationTab.disableProperty().bind(disableTab.and(locationPropertiesView.formProperty().isNull()));
        dictionaryNotesTab.disableProperty().bind(disableTab);
        morphologicalConjugationTab.selectedProperty().addListener((o, ov, nv) -> {
            loadConjugation(locationPropertiesView.getLocation().getMorphologicalEntry());
        });
    }

    private void createLettersPane() {
        lettersPane.setCenter(null);

        HBox hBox = new HBox();
        hBox.setSpacing(GAP);
        hBox.setPadding(new Insets(GAP));
        hBox.setAlignment(CENTER);
        hBox.setNodeOrientation(RIGHT_TO_LEFT);

        TokenPropertiesView view = getSkinnable();
        final ObservableList<TokenPropertiesView.LocationLabel> labels = view.getLabels();
        labels.forEach(locationLabel -> {
            ArabicLabelView labelView = new ArabicLabelView();
            labelView.setLabel(locationLabel);
            labelView.setGroup(group);
            // final int index = locationLabel.getIndex();
            labelView.selectedProperty().addListener((o, ov, nv) -> {
                TokenPropertiesView.SelectionStatus status = nv ? SELECTED : AVAILABLE;
                // replace element at "index" this will fire change event in the "labels" list
                // labels.set(index, new TokenPropertiesView.LocationLabel(status, locationLabel.getLetter(), index));
                locationLabel.setStatus(status);
            });
            labelView.setSelect(locationLabel.getStatus().equals(SELECTED));
            labelView.setDisable(locationLabel.getStatus().equals(NOT_AVAILABLE));
            hBox.getChildren().add(labelView);
        });

        lettersPane.setCenter(hBox);
    }

    private GridPane createLocationsPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(GAP));
        gridPane.setAlignment(CENTER);

        Label label = new Label(RESOURCE_BUNDLE.getString("locations.label"));
        gridPane.add(label, 0, 0);

        locationComboBox.setDisable(true);
        locationComboBox.setButtonCell(new LocationListCell());
        locationComboBox.setCellFactory(param -> new LocationListCell());
        gridPane.add(locationComboBox, 1, 0);

        TokenPropertiesView view = getSkinnable();
        view.tokenProperty().addListener((o, ov, nv) -> updateLocations(nv));
        locationComboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                locationPropertiesView.setLocation(nv);
                tabPane.getSelectionModel().select(0);
                MorphologicalEntry morphologicalEntry = nv.getMorphologicalEntry();
                if (morphologicalEntry != null) {
                    RootLetters rootLetters = morphologicalEntry.getRootLetters();
                    loadDictionary(rootLetters);
                    loadDictionaryNotes(rootLetters);
                }
                view.changeLocation(ov, nv);
            }
            createLettersPane();
        });
        view.selectedLocationProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                locationComboBox.setValue(nv);
            }
        });
        gridPane.setBorder(BORDER);
        return gridPane;
    }

    private void loadDictionary(RootLetters rootLetters) {
        String searchString = null;
        boolean disableDictionaryTab = browseDictionaryTab.isDisabled();
        if (!disableDictionaryTab) {
            // not sure whether display is initialized or not
            rootLetters.initDisplayName();
            searchString = rootLetters.getDisplayName();
        }
        disableDictionaryTab = searchString == null;
        if (!disableDictionaryTab) {
            String url = format("%s%s", MAWRID_READER_URL, searchString);
            browser.loadUrl(url);
        }
    }

    private void loadDictionaryNotes(final RootLetters rootLetters) {
        boolean disabled = dictionaryNotesTab.isDisabled();
        if (!disabled) {
            retrieveDictionaryNotes(rootLetters);
        }
    }

    private void retrieveDictionaryNotes(final RootLetters rootLetters) {
        Location location = locationPropertiesView.getLocation();
        DictionaryNotes dictionaryNotes = location.getDictionaryNotes();
        RetrieveDictionaryNotesService service = new RetrieveDictionaryNotesService(dictionaryNotes, rootLetters);
        service.setOnFailed(event -> defaultCursor(getSkinnable()));
        service.setOnSucceeded(event -> {
            defaultCursor(getSkinnable());
            DictionaryNotes notes = (DictionaryNotes) event.getSource().getValue();
            if (notes != null) {
                dictionaryNotesView.setDictionaryNotes(null);
                dictionaryNotesView.setDictionaryNotes(notes);
                location.setDictionaryNotes(notes);
            }
        });
        service.start();
    }

    private void loadConjugation(final MorphologicalEntry entry) {
        if (entry != null && !morphologicalConjugationTab.isDisabled()) {
            Service<SarfChart> service = new Service<SarfChart>() {
                @Override
                protected Task<SarfChart> createTask() {
                    return new Task<SarfChart>() {
                        @Override
                        protected SarfChart call() throws Exception {
                            waitCursor(getSkinnable());
                            ConjugationConfiguration configuration = entry.getConfiguration();
                            boolean removePassiveLine = false;
                            boolean skipRuleProcessing = false;
                            if (configuration != null) {
                                removePassiveLine = configuration.isRemovePassiveLine();
                                skipRuleProcessing = configuration.isSkipRuleProcessing();
                            }
                            RootLetters rootLetters = entry.getRootLetters();
                            List<VerbalNoun> verbalNouns = new ArrayList<>(entry.getVerbalNouns());
                            List<NounOfPlaceAndTime> nounOfPlaceAndTimes = new ArrayList<>(entry.getNounOfPlaceAndTimes());
                            return conjugationBuilder.doConjugation(entry.getForm(), entry.getShortTranslation(),
                                    removePassiveLine, skipRuleProcessing, rootLetters.getFirstRadical(),
                                    rootLetters.getSecondRadical(), rootLetters.getThirdRadical(),
                                    rootLetters.getFourthRadical(), verbalNouns, nounOfPlaceAndTimes);
                        }
                    };
                }
            };
            service.setOnSucceeded(event -> {
                defaultCursor(getSkinnable());
                SarfChart sarfChart = (SarfChart) event.getSource().getValue();
                conjugationViewer.setSarfChart(sarfChart);
            });
            service.setOnFailed(event -> defaultCursor(getSkinnable()));
            service.start();
        }
    }

    private void updateLocations(Token token) {
        tabPane.getSelectionModel().select(0);
        locationComboBox.getItems().clear();
        locationComboBox.setDisable(true);
        if (token == null) {
            return;
        }
        List<Location> locations = token.getLocations();
        if (locations == null || locations.isEmpty()) {
            return;
        }
        locationComboBox.setDisable(false);
        locationComboBox.getItems().addAll(locations.toArray(new Location[locations.size()]));
        locationComboBox.getSelectionModel().select(0);
    }

    private void initializeSkin() {
        VBox vBox = new VBox();
        vBox.setSpacing(GAP);

        tabPane.getTabs().addAll(new Tab(RESOURCE_BUNDLE.getString("locationProperties.label"), locationPropertiesView),
                browseDictionaryTab, dictionaryNotesTab, morphologicalConjugationTab);

        createLettersPane();

        VBox subBox = new VBox();
        subBox.setSpacing(GAP);
        subBox.getChildren().addAll(lettersPane, createLocationsPane());

        TitledPane titledPane = new TitledPane("Token Letters and Locations", subBox);

        vBox.getChildren().addAll(titledPane, tabPane);
        getChildren().add(vBox);
        updateLocations(getSkinnable().getToken());
    }

    private class RetrieveDictionaryNotesService extends Service<DictionaryNotes> {

        private final DictionaryNotes source;
        private final RootLetters rootLetters;

        private RetrieveDictionaryNotesService(final DictionaryNotes source, final RootLetters rootLetters) {
            this.source = source;
            this.rootLetters = rootLetters;
        }

        @Override
        protected Task<DictionaryNotes> createTask() {
            return new Task<DictionaryNotes>() {
                @Override
                protected DictionaryNotes call() throws Exception {
                    waitCursor(getSkinnable());
                    DictionaryNotes tmp = new DictionaryNotes(rootLetters);
                    DictionaryNotes saved = repositoryUtil.findDictionaryNotes(tmp);
                    return (saved == null) ? tmp : (saved.equals(source) ? null : saved);
                }
            };
        }
    }
}
