package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.app.asciidoctoreditor.ui.ApplicationController;
import com.alphasystem.app.asciidoctoreditor.ui.control.AsciiDoctorEditor;
import com.alphasystem.app.asciidoctoreditor.ui.model.ApplicationMode;
import com.alphasystem.app.morphologicalengine.conjugation.builder.ConjugationBuilder;
import com.alphasystem.app.morphologicalengine.conjugation.builder.ConjugationHelper;
import com.alphasystem.app.morphologicalengine.conjugation.builder.ConjugationRoots;
import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.app.morphologicalengine.guice.GuiceSupport;
import com.alphasystem.app.morphologicalengine.ui.MorphologicalChartControl;
import com.alphasystem.arabic.ui.ArabicLabelToggleGroup;
import com.alphasystem.arabic.ui.ArabicLabelView;
import com.alphasystem.asciidoc.model.AsciiDocumentInfo;
import com.alphasystem.fx.ui.Browser;
import com.alphasystem.morphologicalanalysis.morphology.model.DictionaryNotes;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.repository.DictionaryNotesRepository;
import com.alphasystem.morphologicalanalysis.ui.common.LocationListCell;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisPreferences;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import com.alphasystem.util.GenericPreferences;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

import static com.alphasystem.app.asciidoctoreditor.ui.model.Action.SAVE;
import static com.alphasystem.app.asciidoctoreditor.ui.model.DocumentType.ARTICLE;
import static com.alphasystem.app.asciidoctoreditor.ui.model.IconFontName.FONT_AWESOME;
import static com.alphasystem.app.asciidoctoreditor.ui.model.Icons.FONT;
import static com.alphasystem.fx.ui.util.UiUtilities.defaultCursor;
import static com.alphasystem.fx.ui.util.UiUtilities.waitCursor;
import static com.alphasystem.fx.ui.util.UiUtilities.wrapInScrollPane;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.BORDER;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.DEFAULT_CSS_DIRECTORY;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.DEFAULT_DICTIONARY_DIRECTORY;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.DEFAULT_NOTES_FILE_EXTENSION;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.GAP;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.MAWRID_READER_URL;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView.SelectionStatus.AVAILABLE;
import static com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView.SelectionStatus.NOT_AVAILABLE;
import static com.alphasystem.morphologicalanalysis.ui.wordbyword.control.TokenPropertiesView.SelectionStatus.SELECTED;
import static com.alphasystem.util.nio.NIOFileUtils.fastCopy;
import static java.lang.String.format;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
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
    private final AsciiDoctorEditor asciiDoctorEditor;
    private final MorphologicalChartControl conjugationViewer;
    private final Tab browseDictionaryTab;
    private final Tab morphologicalConjugationTab;
    private final Tab dictionaryNotesTab;
    private final MorphologicalAnalysisRepositoryUtil repositoryUtil = RepositoryTool.getInstance().getRepositoryUtil();
    private final DictionaryNotesRepository dictionaryNotesRepository = repositoryUtil.getDictionaryNotesRepository();
    private ConjugationBuilder conjugationBuilder = GuiceSupport.getInstance().getConjugationBuilder();

    public TokenPropertiesSkin(TokenPropertiesView control) {
        super(control);

        lettersPane = new BorderPane();
        lettersPane.setBorder(BORDER);

        browser = new Browser();
        conjugationViewer = new MorphologicalChartControl();
        asciiDoctorEditor = new AsciiDoctorEditor(ApplicationMode.EMBEDDED);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setBorder(BORDER);

        browseDictionaryTab = new Tab("Browse Dictionary", browser);
        morphologicalConjugationTab = new Tab("Morphological Conjugation", wrapInScrollPane(conjugationViewer));

        dictionaryNotesTab = new Tab("Dictionary Notes", asciiDoctorEditor);
        asciiDoctorEditor.actionProperty().addListener((o, ov, nv) -> {
            if (SAVE.equals(nv)) {
                final DictionaryNotes dictionaryNotes = (DictionaryNotes) dictionaryNotesTab.getUserData();
                final Path path = dictionaryNotes.getFilePath();
                try (InputStream inputStream = newInputStream(path)) {
                    dictionaryNotes.setInputStream(inputStream);
                    dictionaryNotesRepository.store(dictionaryNotes);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        //TODO: whenever we are selecting "Dictionary Notes" tab we should be directly going to source tab
        //dictionaryNotesTab.selectedProperty().addListener((o, ov, nv) -> dictionaryNotesView.selectSource());

        locationComboBox = new ComboBox<>();
        locationPropertiesView = new LocationPropertiesView();

        group = new ArabicLabelToggleGroup();
        group.setWidth(64);
        group.setHeight(64);
        MorphologicalAnalysisPreferences preferences = GenericPreferences.getInstance(MorphologicalAnalysisPreferences.class);
        group.setFont(preferences.getArabicFont36());

        initializeSkin();

        control.reloadProperty().addListener((o, ov, nv) -> locationPropertiesView.setReload(nv));
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
        morphologicalConjugationTab.selectedProperty().addListener((o, ov, nv) -> loadConjugation(locationPropertiesView.getLocation().getMorphologicalEntry()));
    }

    private static void setDictionaryNotesFile(final DictionaryNotes dictionaryNotes) {
        String fileName = dictionaryNotes.getFileName();
        if (fileName == null) {
            Path filePath = get(format("%s%s", dictionaryNotes.getName(), DEFAULT_NOTES_FILE_EXTENSION));
            fileName = filePath.getFileName().toString();
            dictionaryNotes.setFileName(fileName);
        }
        Path path = get(DEFAULT_DICTIONARY_DIRECTORY.getPath(), fileName);
        try {
            if (!exists(path)) {
                path = createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        dictionaryNotes.setFilePath(path);
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
        gridPane.add(locationComboBox, 0, 1);

        TokenPropertiesView view = getSkinnable();
        view.tokenProperty().addListener((o, ov, nv) -> {
            updateLocations(null);
            updateLocations(nv);
        });
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

    private void loadDictionary(final RootLetters rootLetters) {
        String searchString = null;
        boolean disableDictionaryTab = browseDictionaryTab.isDisabled();
        if (!disableDictionaryTab) {
            // not sure whether display is initialized or not
            rootLetters.initDisplayName();
            searchString = rootLetters.toMawridSearchString();
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
            try {
                retrieveDictionaryNotes(rootLetters);
            } catch (UncheckedIOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void retrieveDictionaryNotes(final RootLetters rootLetters) {
        RetrieveDictionaryNotesService service = new RetrieveDictionaryNotesService(rootLetters);
        service.setOnFailed(event -> defaultCursor(getSkinnable()));
        service.setOnSucceeded(event -> {
            defaultCursor(getSkinnable());
            DictionaryNotes dictionaryNotes = (DictionaryNotes) event.getSource().getValue();
            setDictionaryNotesFile(dictionaryNotes);
            Path dictionaryNotesFile = dictionaryNotes.getFilePath();
            if (dictionaryNotes.getId() == null) {
                waitCursor(getSkinnable());
                createNewDictionaryNotes(dictionaryNotes);
            } else {
                try (InputStream inputStream = dictionaryNotes.getInputStream();
                     OutputStream outputStream = newOutputStream(dictionaryNotesFile, WRITE, CREATE)) {
                    fastCopy(inputStream, outputStream);
                    asciiDoctorEditor.setInitialFile(dictionaryNotesFile.toFile());
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex.getMessage(), ex);
                }
            }
            dictionaryNotesTab.setUserData(dictionaryNotes);
        });
        service.start();
    }

    private void createNewDictionaryNotes(DictionaryNotes dictionaryNotes) {
        // new dictionary notes, create now
        AsciiDocumentInfo propertyInfo = new AsciiDocumentInfo();
        propertyInfo.setDocumentType(ARTICLE.getType());
        propertyInfo.setDocumentName(dictionaryNotes.getName());
        propertyInfo.setDocumentTitle(format("[arabic-heading1]#%s#",
                dictionaryNotes.getRootLetters().toLabel().toHtmlCode()));
        propertyInfo.setStylesDir(DEFAULT_CSS_DIRECTORY);
        propertyInfo.setLinkCss(true);
        propertyInfo.setIcons(FONT.getValue());
        propertyInfo.setIconFontName(FONT_AWESOME.getDispalyName());
        propertyInfo.setDocInfo2(true);
        propertyInfo.setOmitLastUpdatedTimeStamp(true);
        propertyInfo.setSrcFile(dictionaryNotes.getFilePath().toFile());
        EventHandler<WorkerStateEvent> onFailed = event -> {
            defaultCursor(getSkinnable());
            final Throwable ex = event.getSource().getException();
            throw new RuntimeException(ex.getMessage(), ex);
        };
        EventHandler<WorkerStateEvent> onSucceeded = event -> {
            asciiDoctorEditor.setInitialFile((File) event.getSource().getValue());
            defaultCursor(getSkinnable());
        };
        ApplicationController.getInstance().doNewDocAction(propertyInfo, true, onFailed, onSucceeded);
    }

    private void loadConjugation(final MorphologicalEntry entry) {
        if (entry != null && !morphologicalConjugationTab.isDisabled()) {
            Service<MorphologicalChart> service = new Service<MorphologicalChart>() {
                @Override
                protected Task<MorphologicalChart> createTask() {
                    return new Task<MorphologicalChart>() {
                        @Override
                        protected MorphologicalChart call() throws Exception {
                            waitCursor(getSkinnable());
                            RootLetters rootLetters = entry.getRootLetters();
                            final ConjugationRoots conjugationRoots = ConjugationHelper.getConjugationRoots(entry)
                                    .conjugationConfiguration(entry.getConfiguration());
                            return conjugationBuilder.doConjugation(conjugationRoots, rootLetters.getFirstRadical(),
                                    rootLetters.getSecondRadical(), rootLetters.getThirdRadical(), rootLetters.getFourthRadical());
                        }
                    };
                }
            };
            service.setOnSucceeded(event -> {
                defaultCursor(getSkinnable());
                MorphologicalChart morphologicalChart = (MorphologicalChart) event.getSource().getValue();
                conjugationViewer.setMorphologicalChart(null);
                conjugationViewer.setMorphologicalChart(morphologicalChart);
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
        if (locations.isEmpty()) {
            return;
        }
        locationComboBox.setDisable(false);
        locations.forEach(location -> locationComboBox.getItems().add(location));
        locationComboBox.getSelectionModel().select(0);
    }

    private void initializeSkin() {
        VBox vBox = new VBox();
        vBox.setSpacing(GAP);

        tabPane.getTabs().addAll(new Tab(RESOURCE_BUNDLE.getString("locationProperties.label"), locationPropertiesView),
                browseDictionaryTab, dictionaryNotesTab, morphologicalConjugationTab);

        createLettersPane();

        HBox subBox = new HBox();
        subBox.setSpacing(GAP);
        subBox.setAlignment(CENTER);
        subBox.setBorder(BORDER);
        subBox.getChildren().addAll(lettersPane, createLocationsPane());

        TitledPane titledPane = new TitledPane("Token Letters and Locations", subBox);

        vBox.getChildren().addAll(titledPane, tabPane);
        getChildren().add(vBox);
        updateLocations(getSkinnable().getToken());
    }

    private class RetrieveDictionaryNotesService extends Service<DictionaryNotes> {

        private final RootLetters rootLetters;

        private RetrieveDictionaryNotesService(final RootLetters rootLetters) {
            this.rootLetters = rootLetters;
        }

        @Override
        protected Task<DictionaryNotes> createTask() {
            return new Task<DictionaryNotes>() {
                @Override
                protected DictionaryNotes call() throws Exception {
                    waitCursor(getSkinnable());
                    return dictionaryNotesRepository.retrieve(rootLetters);
                }
            };
        }
    }
}
