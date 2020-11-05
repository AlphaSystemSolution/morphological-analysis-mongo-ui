package com.alphasystem.morphologicalengine.ui.control.controller;

import com.alphasystem.ApplicationException;
import com.alphasystem.BusinessException;
import com.alphasystem.app.morphologicalengine.docx.MorphologicalChartEngine;
import com.alphasystem.app.morphologicalengine.docx.MorphologicalChartEngineFactory;
import com.alphasystem.app.morphologicalengine.spring.MorphologicalEngineFactory;
import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.app.morphologicalengine.util.TemplateReader;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationData;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.morphologicalengine.model.MorphologicalChart;
import com.alphasystem.morphologicalengine.ui.control.*;
import com.alphasystem.morphologicalengine.ui.control.model.TabInfo;
import com.alphasystem.morphologicalengine.ui.control.model.TableModel;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.GenericPreferences;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
import static com.alphasystem.morphologicalengine.ui.Global.*;
import static java.lang.Math.max;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ButtonType.*;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.SelectionMode.SINGLE;
import static javafx.scene.control.TabPane.TabClosingPolicy.SELECTED_TAB;
import static javafx.scene.text.TextAlignment.CENTER;
import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * @author sali
 */
@Component
public class MorphologicalEngineController extends BorderPane {

    private static final double DEFAULT_MIN_HEIGHT = 500.0;
    private static final double ROW_SIZE = 40.0;
    private static final Background BACKGROUND = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private static int counter = 1;

    private final MorphologicalChartEngineFactory morphologicalChartEngineFactory;
    private final MorphologicalEngineView control;

    private TabPane tabPane;
    private FileSelectionDialog fileSelectionDialog;
    private ChartConfigurationDialog chartConfigurationDialog;
    private Stage chartStage;
    private final TemplateReader templateReader = TemplateReader.getInstance();
    private final MorphologicalEnginePreferences preferences;

    public MorphologicalEngineController(@Autowired MorphologicalChartEngineFactory morphologicalChartEngineFactory,
                                         @Autowired MorphologicalEngineView control) {
        preferences = GenericPreferences.getInstance(MorphologicalEnginePreferences.class);
        Platform.runLater(() -> {
            chartStage = new Stage();
            initViewerStage();
            chartConfigurationDialog = new ChartConfigurationDialog();
            fileSelectionDialog = new FileSelectionDialog(new TabInfo());
            FILE_CHOOSER.setInitialDirectory(preferences.getInitialDirectory());
        });
        this.morphologicalChartEngineFactory = morphologicalChartEngineFactory;
        this.control = control;
    }

    @PostConstruct
    void postConstruct() {
        setup();
    }

    private void setup() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(SELECTED_TAB);
        tabPane.setBackground(BACKGROUND);
        openAction(null);

        setCenter(tabPane);
        final MenuController menuController = MenuController.getInstance(this);
        setTop(new VBox(5, menuController.createMenuBar(), menuController.createToolBar()));
        setBackground(BACKGROUND);
    }

    private static String getTabTitle(File file) {
        return (file == null) ? format("Untitled %s", counter++) : getBaseName(file.getAbsolutePath());
    }

    private static double calculateTableHeight(int numOfRows) {
        double height = (numOfRows * ROW_SIZE) + ROW_SIZE;
        height = roundTo100(height);
        return max(height, DEFAULT_MIN_HEIGHT) + 100;
    }

    private Tab getCurrentTab() {
        return tabPane.getSelectionModel().getSelectedItem();
    }

    private TabInfo getTabUserData() {
        Tab currentTab = getCurrentTab();
        return (currentTab == null) ? null : ((TabInfo) currentTab.getUserData());
    }

    @SuppressWarnings({"unchecked"})
    private TableView<TableModel> getCurrentTable() {
        TableView<TableModel> tableView = null;
        Tab currentTab = getCurrentTab();
        if (currentTab != null) {
            ScrollPane scrollPane = (ScrollPane) currentTab.getContent();
            tableView = (TableView<TableModel>) scrollPane.getContent();
        }
        return tableView;
    }

    private void makeDirty(boolean dirty) {
        TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            tabInfo.setDirty(dirty);
        }
    }

    private Tab createTab(File file, ConjugationTemplate template) {
        control.setArabicFontProperty(getArabicFont());
        Tab tab = new Tab(getTabTitle(file), createTable(template));
        TabInfo tabInfo = new TabInfo();
        tabInfo.setChartConfiguration(template.getChartConfiguration());
        if (file != null) {
            final File parentFile = file.getParentFile();
            FILE_CHOOSER.setInitialDirectory(parentFile);
            preferences.setInitialDirectory(parentFile);
            try {
                preferences.save();
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            tabInfo.setSarfxFile(file);
            tabInfo.setDocxFile(TemplateReader.getDocxFile(file));
        }
        tab.setUserData(tabInfo);
        tab.setOnCloseRequest(event -> {
            TabInfo currentTabInfo = getTabUserData();
            if (currentTabInfo != null && currentTabInfo.getDirty()) {
                Alert alert = new Alert(CONFIRMATION);
                alert.setContentText("Do you want to save data before closing?");
                alert.getButtonTypes().setAll(YES, NO, CANCEL);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    ButtonType buttonType = result.get();
                    ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                    String text = buttonType.getText();
                    if (buttonData.isDefaultButton()) {
                        saveAction(MorphologicalEngineController.SaveMode.SAVE);
                    } else if (text.equals("Cancel")) {
                        event.consume();
                    }
                }
            }

        });
        return tab;
    }

    private ScrollPane createTable(ConjugationTemplate conjugationTemplate) {
        ObservableList<TableModel> tableModels = observableArrayList();
        List<ConjugationData> dataList = conjugationTemplate.getData();
        if (dataList.isEmpty()) {
            dataList.add(new ConjugationData());
        }
        dataList.forEach(data -> tableModels.add(new TableModel(data)));

        double boundsWidth = Screen.getPrimary().getVisualBounds().getWidth();

        TableView<TableModel> tableView = new TableView<>(tableModels);
        tableView.setBackground(BACKGROUND);
        tableView.getSelectionModel().setSelectionMode(SINGLE);
        tableView.setEditable(true);
        initializeTable(tableView, boundsWidth);

        tableView.setFixedCellSize(ROW_SIZE);
        tableView.setPrefSize(boundsWidth, calculateTableHeight(tableModels.size()));
        updateTableModel(tableModels, tableView);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);

        return scrollPane;
    }

    private void updateTableModel(ObservableList<TableModel> tableModels, TableView<TableModel> tableView) {
        tableView.setItems(tableModels);
    }

    void addNewRowAction() {
        TableView<TableModel> tableView = getCurrentTable();
        if (tableView != null) {
            ObservableList<TableModel> items = FXCollections.observableArrayList(tableView.getItems());
            items.add(new TableModel());
            updateTableModel(items, tableView);
            tableView.setPrefHeight(calculateTableHeight(items.size()));
            Platform.runLater(() -> {
                tableView.requestFocus();
                tableView.getSelectionModel().selectLast();
                tableView.getFocusModel().focus(tableView.getItems().size() - 1);
            });
        }
    }

    void duplicateRowAction() {
        TableView<TableModel> currentTable = getCurrentTable();
        if (currentTable != null) {
            ObservableList<TableModel> items = currentTable.getItems();
            if (items != null && !items.isEmpty()) {
                ListIterator<TableModel> listIterator = items.listIterator();
                while (listIterator.hasNext()) {
                    TableModel model = listIterator.next();
                    if (model.isChecked()) {
                        listIterator.add(new TableModel(model));
                        model.setChecked(false);
                    } // end of if "model.isChecked()"
                } // end of "while"
            } // end of if "items != null && !items.empty()"
        } // end of if "currentTable != null"
    }

    void removeRowAction() {
        TableView<TableModel> currentTable = getCurrentTable();
        if (currentTable != null) {
            ObservableList<TableModel> items = currentTable.getItems();
            if (items != null && !items.isEmpty()) {
                items.removeIf(TableModel::isChecked);
            }
        }
    }

    void updateChartConfiguration() {
        final TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            chartConfigurationDialog.setChartConfiguration(tabInfo.getChartConfiguration());
            Optional<ChartConfiguration> result = chartConfigurationDialog.showAndWait();
            result.ifPresent(chartConfiguration -> updateChartConfiguration(tabInfo, chartConfiguration));
        }
    }

    private void updateChartConfiguration(final TabInfo tabInfo, final ChartConfiguration chartConfiguration) {
        tabInfo.setChartConfiguration(chartConfiguration);
        control.setArabicFontProperty(getArabicFont());
    }

    private Font getArabicFont() {
        return Font.font(preferences.getArabicFontName(), FontWeight.BLACK, FontPosture.REGULAR,
                preferences.getArabicFontSize());
    }

    /**
     * New action.
     *
     * @see MorphologicalEngineController#openAction(boolean)
     */
    void newAction() {
        openAction(false);
    }

    /**
     * Open action.
     *
     * @see MorphologicalEngineController#openAction(boolean)
     */
    void openAction() {
        openAction(true);
    }

    /**
     * Performs either <strong>OPEN</strong> or <strong>NEW</strong> action. If the <code>showDialog</code> parameter
     * is true then file chooser dialog will get displayed and this method will behave like typical
     * <strong>OPEN</strong> action, if the given parameter is passed then this method will behave like typical
     * <strong>NEW</strong> action.
     * <div>
     * In case of "open" action in following case file not be opened:
     * <ul>
     * <li>If user canceled the file dialog</li>
     * <li>If errors occur reading file</li>
     * </ul>
     * </div>
     *
     * @param showDialog true for "open" action, false for "new" action
     */
    private void openAction(final boolean showDialog) {
        File file = null;
        if (showDialog) {
            try {
                file = FILE_CHOOSER.showOpenDialog(getScene().getWindow());
            } catch (IllegalArgumentException e) {
                FILE_CHOOSER.setInitialDirectory(AppUtil.USER_HOME_DIR);
                file = FILE_CHOOSER.showOpenDialog(getScene().getWindow());
            }
            if (file == null) {
                // use might have cancel the dialog
                return;
            }
        }
        openAction(file);
    }

    private void openAction(File file) {
        if (file == null || !file.exists()) {
            file = null;
        }
        MorphologicalEngineController.FileOpenService service = new MorphologicalEngineController.FileOpenService(file);
        service.setOnSucceeded(event -> {
            Tab tab = (Tab) event.getSource().getValue();
            if (tab != null) {
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
            }
            changeToDefaultCursor();
        });
        service.setOnFailed(this::showErrorServiceFailed);
        service.start();
    }

    void saveAction() {
        saveAction(MorphologicalEngineController.SaveMode.SAVE);
    }

    void saveAsAction() {
        saveAction(MorphologicalEngineController.SaveMode.SAVE_AS);
    }

    void saveSelectedAction() {
        saveAction(MorphologicalEngineController.SaveMode.SAVE_SELECTED);
    }

    void exportToWordAction() {
        saveAction(MorphologicalEngineController.SaveMode.EXPORT_TO_WORD);
    }

    void exportSelectedToWordAction() {
        saveAction(MorphologicalEngineController.SaveMode.EXPORT_SELECTED_TO_WORD);
    }

    void exportAbbreviatedConjugationsToWordAction() {
        saveAction(SaveMode.EXPORT_ABBREVIATED_CONJUGATION_TO_WORD);
    }

    void exportDetailedConjugationsToWordAction() {
        saveAction(SaveMode.EXPORT_DETAILED_CONJUGATION_TO_WORD);
    }

    void closeAction() {
        final Tab currentTab = getCurrentTab();
        if (currentTab != null) {
            final TabPaneSkin skin = (TabPaneSkin) tabPane.getSkin();
            final TabPaneBehavior behavior = skin.getBehavior();
            if (behavior.canCloseTab(currentTab)) {
                behavior.closeTab(currentTab);
            }
        }
    }

    void exitAction() {
        Platform.exit();
    }

    private void saveAction(MorphologicalEngineController.SaveMode saveMode) {
        final TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            if (showDialogIfApplicable(saveMode, tabInfo)) {
                changeToWaitCursor();
                runLater(saveData(saveMode, tabInfo));
            } // end of if "doSave"
        } // end of if "tabInfo != null"
    }

    private ObservableList<TableModel> getSelectedItems() {
        final TableView<TableModel> tableView = getCurrentTable();
        final ObservableList<TableModel> items = tableView.getItems();
        final ObservableList<TableModel> currentItems = observableArrayList();

        items.forEach(tableModel -> {
            if (tableModel.isChecked()) {
                currentItems.add(tableModel);
            }
        });

        if (currentItems.isEmpty()) {
            currentItems.addAll(items);
        }

        return currentItems;
    }

    private Runnable saveData(final MorphologicalEngineController.SaveMode saveMode, final TabInfo tabInfo) {
        return () -> {
            TableView<TableModel> tableView = getCurrentTable();
            final ObservableList<TableModel> currentItems = getSelectedItems();
            if (currentItems.isEmpty()) {
                changeToDefaultCursor();
                return;
            }
            ConjugationTemplate conjugationTemplate = getConjugationTemplate(currentItems, tabInfo.getChartConfiguration());

            if (MorphologicalEngineController.SaveMode.EXPORT_SELECTED_TO_WORD.equals(saveMode)) {
                final TextInputDialog dialog = new TextInputDialog(getTempFileName(tabInfo.getSarfxFile(), "temp"));
                dialog.setHeaderText("Enter the name of the file");
                final Optional<String> optionalResult = dialog.showAndWait();
                if (optionalResult.isPresent()) {
                    final Path path = Paths.get(tabInfo.getParentPath().toString(), optionalResult.get());
                    saveAsDocx(conjugationTemplate, path, currentItems);
                }
            } else if (SaveMode.EXPORT_ABBREVIATED_CONJUGATION_TO_WORD.equals(saveMode)) {
                final TextInputDialog dialog = new TextInputDialog(getTempFileName(tabInfo.getSarfxFile(), "Abbreviated"));
                dialog.setHeaderText("Enter the name of the file");
                final Optional<String> optionalResult = dialog.showAndWait();
                if (optionalResult.isPresent()) {
                    final Path path = Paths.get(tabInfo.getParentPath().toString(), optionalResult.get());
                    final ChartConfiguration cc = new ChartConfiguration(tabInfo.getChartConfiguration())
                            .omitToc(true)
                            .omitDetailedConjugation(true);
                    final ConjugationTemplate ct = new ConjugationTemplate(conjugationTemplate).withChartConfiguration(cc);
                    saveAsDocx(ct, path, currentItems);
                }
            } else if (SaveMode.EXPORT_DETAILED_CONJUGATION_TO_WORD.equals(saveMode)) {
                final TextInputDialog dialog = new TextInputDialog(getTempFileName(tabInfo.getSarfxFile(), "Detailed"));
                dialog.setHeaderText("Enter the name of the file");
                final Optional<String> optionalResult = dialog.showAndWait();
                if (optionalResult.isPresent()) {
                    final Path path = Paths.get(tabInfo.getParentPath().toString(), optionalResult.get());
                    final ChartConfiguration cc = new ChartConfiguration(tabInfo.getChartConfiguration())
                            .omitToc(true)
                            .omitAbbreviatedConjugation(true);
                    final ConjugationTemplate ct = new ConjugationTemplate(conjugationTemplate).withChartConfiguration(cc);
                    saveAsDocx(ct, path, currentItems);
                }
            } else {
                try {
                    File sarfxFile = tabInfo.getSarfxFile();
                    templateReader.saveFile(sarfxFile, conjugationTemplate);
                    if (MorphologicalEngineController.SaveMode.EXPORT_TO_WORD.equals(saveMode)) {
                        saveAsDocx(conjugationTemplate, tabInfo.getDocxFile().toPath(), currentItems);
                    }

                    Tab currentTab = getCurrentTab();
                    currentTab.setText(TemplateReader.getFileNameNoExtension(sarfxFile));
                    if (MorphologicalEngineController.SaveMode.SAVE_SELECTED.equals(saveMode)) {
                        tableView.getItems().clear();
                        tableView.getItems().addAll(currentItems);
                    }
                } catch (ApplicationException e) {
                    changeToDefaultCursor();
                    e.printStackTrace();
                    showError(e);
                }
            }
        };
    }

    private void saveAsDocx(final ConjugationTemplate conjugationTemplate,
                            final Path path,
                            final ObservableList<TableModel> currentItems) {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        changeToWaitCursor();
                        MorphologicalChartEngine engine = morphologicalChartEngineFactory.createMorphologicalChartEngine(conjugationTemplate);
                        engine.createDocument(path);
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded(event -> {
            makeDirty(false);
            currentItems.forEach(tableModel -> tableModel.setChecked(false));
            changeToDefaultCursor();
            Alert alert = new Alert(CONFIRMATION);
            final String message = format("Document \"%s\" has been published.%sWould you like to open it",
                    path.toString(), System.lineSeparator());
            alert.setContentText(message);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.filter(buttonType -> OK == buttonType).isPresent()) {
                // start application with -Djava.awt.headless=false, if Headless exception was thrown
                try {
                    Desktop.getDesktop().open(path.toFile());
                } catch (Throwable e) {
                    // ignore
                    showError(e);
                }
            }
        });
        service.setOnFailed(event -> {
            currentItems.forEach(tableModel -> tableModel.setChecked(false));
            changeToDefaultCursor();
            showError(event.getSource().getException());
        });
        service.start();
    }

    private void showErrorServiceFailed(@SuppressWarnings({"unused"}) WorkerStateEvent event) {
        changeToDefaultCursor();
        Alert alert = new Alert(ERROR);
        alert.setContentText("Error occurred while opening document.");
        alert.showAndWait();
    }


    private void showError(Throwable ex) {
        ex.printStackTrace();
        Alert alert = new Alert(ERROR);
        alert.setContentText(format("%s:%s", ex.getClass().getName(), ex.getMessage()));
        alert.showAndWait();
    }

    private ConjugationTemplate getConjugationTemplate(ObservableList<TableModel> items,
                                                       ChartConfiguration chartConfiguration) {
        final List<ConjugationData> data = items.stream().map(TableModel::getConjugationData).collect(Collectors.toList());
        return new ConjugationTemplate().withChartConfiguration(chartConfiguration).withData(data);
    }

    private boolean showDialogIfApplicable(MorphologicalEngineController.SaveMode saveMode, TabInfo tabInfo) {
        File sarfxFile = tabInfo.getSarfxFile();
        boolean showDialog = sarfxFile == null || MorphologicalEngineController.SaveMode.SAVE_AS.equals(saveMode) ||
                MorphologicalEngineController.SaveMode.SAVE_SELECTED.equals(saveMode);
        if (showDialog) {
            fileSelectionDialog.setTabInfo(tabInfo);
            Optional<TabInfo> result = fileSelectionDialog.showAndWait();
            result.ifPresent(ti -> {
                tabInfo.setSarfxFile(ti.getSarfxFile());
                tabInfo.setDocxFile(ti.getDocxFile());
                tabInfo.setDirty(false);
            });
        }// end of if "showDialog"
        return tabInfo.getSarfxFile() != null;
    }

    @SuppressWarnings("unchecked")
    private void initializeTable(TableView<TableModel> tableView, double boundsWidth) {
        double largeColumnWidth = (boundsWidth * 20) / 100;
        double mediumColumnWidth = (boundsWidth * 8) / 100;
        double smallColumnWidth = (boundsWidth * 4) / 100;

        // start adding columns
        TableColumn<TableModel, Boolean> checkedColumn = createCheckedColumn(smallColumnWidth);
        TableColumn<TableModel, RootLetters> rootLettersColumn = createRootLettersColumn(largeColumnWidth);
        TableColumn<TableModel, NamedTemplate> templateColumn = createTemplateColumn(largeColumnWidth);
        TableColumn<TableModel, String> translationColumn = createTranslationColumn(mediumColumnWidth);
        TableColumn<TableModel, ObservableList<VerbalNoun>> verbalNounsColumn = createVerbalNounsColumn(largeColumnWidth);

        //TODO: figure out how to refresh Verbal Noun column with new values
        templateColumn.setOnEditCommit(event -> {
            makeDirty(true);
            NamedTemplate newValue = event.getNewValue();
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setTemplate(newValue);

            // TODO: figure out how to update table
            List<VerbalNoun> verbalNouns = VerbalNoun.getByTemplate(newValue);

            // clear the currently selected verbal nouns first then add new values, if there is no verbal noun mapped
            // then our list should be empty
            selectedItem.getVerbalNouns().clear();
            selectedItem.getVerbalNouns().addAll(verbalNouns);
        });

        TableColumn<TableModel, Boolean> removePassiveLineColumn = createRemovePassiveLineColumn(mediumColumnWidth);
        TableColumn<TableModel, Boolean> skipRuleProcessingColumn = createSkipRuleProcessingColumn(mediumColumnWidth);
        final TableColumn<TableModel, Boolean> viewConjugationColumn = createViewConjugationColumn(mediumColumnWidth);

        tableView.getColumns().addAll(checkedColumn, rootLettersColumn, templateColumn, translationColumn,
                verbalNounsColumn, removePassiveLineColumn, skipRuleProcessingColumn, viewConjugationColumn);
    }

    private TableColumn<TableModel, Boolean> createCheckedColumn(double smallColumnWidth) {
        TableColumn<TableModel, Boolean> checkedColumn = new TableColumn<>();
        checkedColumn.setPrefWidth(smallColumnWidth);
        checkedColumn.setEditable(true);
        checkedColumn.setCellValueFactory(new PropertyValueFactory<>("checked"));
        final Callback<Integer, ObservableValue<Boolean>> cb = index ->
                checkedColumn.getTableView().getItems().get(index).checkedProperty();
        checkedColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));
        return checkedColumn;
    }

    private TableColumn<TableModel, RootLetters> createRootLettersColumn(double largeColumnWidth) {
        TableColumn<TableModel, RootLetters> rootLettersColumn = new TableColumn<>();
        rootLettersColumn.setText("Root Letters");
        rootLettersColumn.setPrefWidth(largeColumnWidth);
        rootLettersColumn.setEditable(true);
        rootLettersColumn.setCellValueFactory(new PropertyValueFactory<>("rootLetters"));
        //rootLettersTableCell.fontProperty().bind(arabicFontProperty);
        rootLettersColumn.setCellFactory(param -> new RootLettersTableCell(param, control.arabicFontPropertyProperty()));
        rootLettersColumn.setOnEditCommit(event -> {
            makeDirty(true);
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setRootLetters(event.getNewValue());
        });
        return rootLettersColumn;
    }

    private TableColumn<TableModel, NamedTemplate> createTemplateColumn(double largeColumnWidth) {
        TableColumn<TableModel, NamedTemplate> templateColumn = new TableColumn<>();
        templateColumn.setText("Family");
        templateColumn.setEditable(true);
        templateColumn.setPrefWidth(largeColumnWidth);
        templateColumn.setCellValueFactory(new PropertyValueFactory<>("template"));
        templateColumn.setCellFactory(column -> new ComboBoxTableCell<TableModel, NamedTemplate>(NamedTemplate.values()) {
            private final Text labelText;
            private final Text arabicText;
            private final ComboBox<NamedTemplate> comboBox;

            {
                setContentDisplay(GRAPHIC_ONLY);
                setNodeOrientation(RIGHT_TO_LEFT);
                setAlignment(Pos.CENTER);
                comboBox = createComboBox(NamedTemplate.values());
                arabicText = new Text();
                arabicText.setFont(preferences.getArabicFont());
                arabicText.setTextAlignment(CENTER);
                arabicText.setNodeOrientation(RIGHT_TO_LEFT);
                labelText = new Text();
                labelText.setTextAlignment(CENTER);
                labelText.setFont(preferences.getEnglishFont());
                comboBox.setVisibleRowCount(15);

                comboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> commitEdit(nv));
            }

            @Override
            public void startEdit() {
                if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
                    return;
                }

                comboBox.getSelectionModel().select(getItem());

                super.startEdit();
                setText(null);
                setGraphic(comboBox);
            }

            @Override
            public void updateItem(NamedTemplate item, boolean empty) {
                super.updateItem(item, empty);

                TextFlow textFlow = new TextFlow();
                Node graphic = null;
                if (item != null && !empty) {
                    labelText.setText(format("(%s) ", item.getCode()));
                    arabicText.setText(item.toLabel().toUnicode());
                    textFlow.getChildren().addAll(arabicText, createSpaceLabel(), labelText);
                    graphic = new Group(textFlow);
                }
                setGraphic(graphic);
            }
        });
        return templateColumn;
    }

    private TableColumn<TableModel, String> createTranslationColumn(double mediumColumnWidth) {
        TableColumn<TableModel, String> translationColumn = new TableColumn<>();
        translationColumn.setText("Translation");
        translationColumn.setPrefWidth(mediumColumnWidth);
        translationColumn.setEditable(true);
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        translationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        translationColumn.setOnEditCommit(event -> {
            makeDirty(true);
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setTranslation(event.getNewValue());
        });
        return translationColumn;
    }

    private TableColumn<TableModel, ObservableList<VerbalNoun>> createVerbalNounsColumn(double largeColumnWidth) {
        TableColumn<TableModel, ObservableList<VerbalNoun>> verbalNounsColumn = new TableColumn<>();
        verbalNounsColumn.setText("Verbal Nouns");
        verbalNounsColumn.setPrefWidth(largeColumnWidth);
        verbalNounsColumn.setEditable(true);
        verbalNounsColumn.setCellValueFactory(new PropertyValueFactory<>("verbalNouns"));
        verbalNounsColumn.setCellFactory(VerbalNounTableCell::new);
        verbalNounsColumn.setOnEditCommit(event -> {
            makeDirty(true);
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.getVerbalNouns().clear();
            selectedItem.getVerbalNouns().addAll(event.getNewValue());
        });
        return verbalNounsColumn;
    }

    private TableColumn<TableModel, Boolean> createRemovePassiveLineColumn(double mediumColumnWidth) {
        TableColumn<TableModel, Boolean> removePassiveLineColumn = new TableColumn<>();
        removePassiveLineColumn.setText("Remove\nPassive\nLine");
        removePassiveLineColumn.setPrefWidth(mediumColumnWidth);
        removePassiveLineColumn.setEditable(true);
        removePassiveLineColumn.setCellValueFactory(new PropertyValueFactory<>("removePassiveLine"));
        final Callback<Integer, ObservableValue<Boolean>> cb = index ->
                removePassiveLineColumn.getTableView().getItems().get(index).removePassiveLineProperty();
        removePassiveLineColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));
        removePassiveLineColumn.setOnEditCommit(event -> makeDirty(true));
        return removePassiveLineColumn;
    }

    private TableColumn<TableModel, Boolean> createSkipRuleProcessingColumn(double mediumColumnWidth) {
        TableColumn<TableModel, Boolean> skipRuleProcessingColumn = new TableColumn<>();
        skipRuleProcessingColumn.setText("Skip\nRule\nProcessing");
        skipRuleProcessingColumn.setPrefWidth(mediumColumnWidth);
        skipRuleProcessingColumn.setEditable(true);
        skipRuleProcessingColumn.setCellValueFactory(new PropertyValueFactory<>("skipRuleProcessing"));
        final Callback<Integer, ObservableValue<Boolean>> cb = index ->
                skipRuleProcessingColumn.getTableView().getItems().get(index).skipRuleProcessingProperty();
        skipRuleProcessingColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));
        return skipRuleProcessingColumn;
    }

    private TableColumn<TableModel, Boolean> createViewConjugationColumn(double width) {
        final TableColumn<TableModel, Boolean> column = new TableColumn<>();
        column.setText("View\n Conjugation\n        &\n Dictionary");
        column.setPrefWidth(width);
        column.setEditable(true);
        column.setCellValueFactory(new PropertyValueFactory<>("viewConjugation"));
        final Callback<Integer, ObservableValue<Boolean>> cb = index -> {
            final TableModel tableModel = column.getTableView().getItems().get(index);
            final BooleanProperty viewConjugationProperty = tableModel.viewConjugationProperty();
            if (viewConjugationProperty.get()) {
                final RootLetters rootLetters = tableModel.getRootLetters();
                final NamedTemplate template = tableModel.getTemplate();
                if (rootLetters == null || template == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Both RootLetters and Form has to be populated.");
                    alert.show();
                } else {
                    runLater(() -> updateViewer(tableModel));
                }
                viewConjugationProperty.setValue(false);
            }
            return viewConjugationProperty;
        };
        column.setCellFactory(param -> new CheckBoxTableCell<>(cb));
        return column;
    }

    private void updateViewer(TableModel tableModel) {
        ConjugationTemplate conjugationTemplate = new ConjugationTemplate();
        conjugationTemplate.getData().add(tableModel.getConjugationData());
        MorphologicalChartEngine engine = morphologicalChartEngineFactory.createMorphologicalChartEngine(conjugationTemplate);
        final MorphologicalChart morphologicalChart = engine.createMorphologicalCharts().get(0);

        MorphologicalChartViewerControl morphologicalChartViewer;

        final Scene scene = chartStage.getScene();
        if (scene == null) {
            morphologicalChartViewer = MorphologicalEngineFactory.getBean(MorphologicalChartViewerControl.class);
            chartStage.setScene(new Scene(morphologicalChartViewer));
        } else {
            morphologicalChartViewer = (MorphologicalChartViewerControl) scene.getRoot();
        }
        morphologicalChartViewer.setMorphologicalChart(null);
        morphologicalChartViewer.setMorphologicalChart(morphologicalChart);

        if (chartStage.isShowing()) {
            chartStage.toFront();
        } else {
            chartStage.show();
            chartStage.setMaximized(true);
        }
    }

    private void initViewerStage() {
        chartStage.setTitle("Morphological Chart Viewer");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        chartStage.setX(bounds.getMinX());
        chartStage.setY(bounds.getMinY());
        chartStage.setWidth(bounds.getWidth());
        chartStage.setHeight(bounds.getHeight());

        chartStage.setWidth(bounds.getWidth() / 4);
        chartStage.setHeight(bounds.getHeight() / 4);
    }

    @SuppressWarnings("unused")
    private void initDependencies(Stage primaryStage) {
        setDialogOwner(primaryStage);
    }

    private void setDialogOwner(Stage primaryStage) {
        Window owner = fileSelectionDialog.getOwner();
        if (owner == null) {
            fileSelectionDialog.initOwner(primaryStage);
        }
        owner = chartConfigurationDialog.getOwner();
        if (owner == null) {
            chartConfigurationDialog.initOwner(primaryStage);
        }
    }

    private void changeCursor(Cursor cursor) {
        Scene scene = getScene();
        if (scene != null) {
            scene.setCursor(cursor);
        }
    }

    private void changeToDefaultCursor() {
        changeCursor(Cursor.DEFAULT);
    }

    private void changeToWaitCursor() {
        changeCursor(Cursor.WAIT);
    }

    private enum SaveMode {
        SAVE, SAVE_AS, SAVE_SELECTED, EXPORT_TO_WORD, EXPORT_SELECTED_TO_WORD, EXPORT_ABBREVIATED_CONJUGATION_TO_WORD,
        EXPORT_DETAILED_CONJUGATION_TO_WORD
    }

    private class FileOpenService extends Service<Tab> {

        private final File file;

        private FileOpenService(File file) {
            this.file = file;
        }

        @Override
        protected Task<Tab> createTask() {
            return new Task<Tab>() {
                @Override
                protected Tab call() throws Exception {
                    changeToWaitCursor();
                    ConjugationTemplate template = file == null ? null : templateReader.readFile(file);
                    if (template == null) {
                        ChartConfiguration chartConfiguration = new ChartConfiguration();
                        chartConfiguration.setArabicFontFamily(preferences.getArabicFontName());
                        chartConfiguration.setTranslationFontFamily(preferences.getEnglishFontName());
                        chartConfiguration.setArabicFontSize(preferences.getArabicFontSize());
                        chartConfiguration.setTranslationFontSize(preferences.getEnglishFontSize());
                        chartConfiguration.setHeadingFontSize(preferences.getArabicHeadingFontSize());
                        template = new ConjugationTemplate();
                        template.setChartConfiguration(chartConfiguration);
                    }
                    return createTab(file, template);
                } // end of method "call"
            }; // end of anonymous class "Task"
        } // end of method "createTask"
    } // end of class "FileOpenService"


    private String getTempFileName(final File file, final String suffix) {
        return String.format("%s-%s.docx", getBaseName(file.getName()), suffix);
    }

}
