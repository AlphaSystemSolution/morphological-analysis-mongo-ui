package com.alphasystem.morphologicalanalysis.ui.access.control.controller;

import com.alphasystem.access.model.QuestionData;
import com.alphasystem.access.model.QuestionType;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.ui.access.application.ApplicationController;
import com.alphasystem.morphologicalanalysis.ui.access.application.InvalidAppendRequestException;
import com.alphasystem.morphologicalanalysis.ui.access.control.DataExportView;
import com.alphasystem.morphologicalanalysis.ui.access.control.QuestionTypeSelectionDialog;
import com.alphasystem.morphologicalanalysis.ui.access.control.TokenView;
import com.alphasystem.morphologicalanalysis.ui.control.ChapterVerseSelectionPane;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.util.AppUtil;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * @author sali
 */
@Component
public class DataExportController extends BorderPane {

    private final ApplicationController applicationController = ApplicationController.getInstance();
    private QuestionTypeSelectionDialog dialog;
    private final FileChooser fileChooser = new FileChooser();
    private Alert addTokenConfirmationAlert;

    @Autowired private DataExportView control;
    @Autowired private ChapterVerseSelectionPane chapterVerseSelectionPane;
    @Autowired private TokenView tokenView;

    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem addNewQuestionMenuItem;
    @FXML private MenuItem addTokensMenuItem;
    @FXML private MenuItem exportMenuItem;

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
        chapterVerseSelectionPane.selectedVerseProperty().addListener((observable, oldValue, newValue) ->
                tokenView.setVerseTokenPairGroup(newValue));
        fileChooser.setInitialDirectory(AppUtil.USER_HOME_DIR);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON File", "*.json"));
        Platform.runLater(() -> {
            dialog = new QuestionTypeSelectionDialog();
            createAddTokenConfirmationAlert();
        });
    }

    @FXML
    void initialize() {
        saveMenuItem.disableProperty().bind(applicationController.currentProperty().isNull());
        addNewQuestionMenuItem.disableProperty().bind(applicationController.currentProperty().isNull());
        addTokensMenuItem.disableProperty().bind(applicationController.currentProperty().isNull());
        exportMenuItem.disableProperty().bind(applicationController.currentProperty().isNull());

        final VBox top = (VBox) getTop();
        top.getChildren().add(chapterVerseSelectionPane);
        setCenter(UiUtilities.wrapInScrollPane(tokenView));
    }

    @FXML
    private void onNew() {
        final List<QuestionData> currentData = applicationController.getCurrentData();
        if (!currentData.isEmpty()) {
            // TODO: confirm user to save
        }
        showQuestionTypeSelectionDialog(true);
    }

    @FXML
    private void onOpen() {
        final File currentFile = fileChooser.showOpenDialog(control.getScene().getWindow());
        if (currentFile != null) {
            fileChooser.setInitialDirectory(currentFile.getParentFile());
            try {
                applicationController.openFile(currentFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onSave() {
        File currentFile = applicationController.getCurrentFile();
        if (currentFile == null) {
            currentFile = fileChooser.showSaveDialog(control.getScene().getWindow());
            if (currentFile == null) {
                return;
            }
            fileChooser.setInitialDirectory(currentFile.getParentFile());
            applicationController.setCurrentFile(currentFile);
        }

        try {
            applicationController.saveFile(currentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onExport() {
        final File currentFile = applicationController.getCurrentFile();
        if (currentFile == null) {
            onSave();
        }
        if (currentFile == null) {
            return;
        }
        UiUtilities.waitCursor(control);
        final Service<Path> exportService = applicationController.export();
        exportService.setOnSucceeded(event -> {
            UiUtilities.defaultCursor(control);
            final Path path = (Path) event.getSource().getValue();
            try {
                Desktop.getDesktop().open(path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        exportService.setOnFailed(event -> {
            UiUtilities.defaultCursor(control);
            try {
                throw event.getSource().getException();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        exportService.start();
    }

    @FXML
    private void onAddNewQuestion() {
        final QuestionData current = applicationController.getCurrent();
        boolean empty = current != null && current.empty();
        if (empty) {
        }
        showQuestionTypeSelectionDialog(false);
    }

    @FXML
    private void onAddTokens() {
        Optional<ButtonType> resultButtonType = addTokenConfirmationAlert.showAndWait();
        if (resultButtonType.isPresent()) {
            ButtonBar.ButtonData buttonData = resultButtonType.get().getButtonData();
            if (ButtonBar.ButtonData.CANCEL_CLOSE.equals(buttonData)) {
                return;
            }
            try {
                applicationController.addTokens(tokenView.getSelectedTokens(), ButtonBar.ButtonData.YES.equals(buttonData));
            } catch (InvalidAppendRequestException e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Unable to append selected tokens, would you like to start new group instead?");
                resultButtonType = alert.showAndWait();
                if (resultButtonType.isPresent()) {
                    buttonData = resultButtonType.get().getButtonData();
                    if (ButtonBar.ButtonData.CANCEL_CLOSE.equals(buttonData)) {
                        return;
                    }
                    if (ButtonBar.ButtonData.OK_DONE.equals(buttonData)) {
                        try {
                            applicationController.addTokens(tokenView.getSelectedTokens(), false);
                        } catch (InvalidAppendRequestException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void showQuestionTypeSelectionDialog(boolean initializeCurrentData) {
        dialog.getView().setQuestionType(QuestionType.LIST);
        final Optional<QuestionType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (initializeCurrentData) {
                applicationController.setCurrentData(null);
            }
            applicationController.setCurrent(new QuestionData(result.get()));
        }
    }

    private void createAddTokenConfirmationAlert() {
        addTokenConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);

        String text = String.format("%s%s%s%s%s", "Would you like append selected tokens in the previous group?",
                System.lineSeparator(), "Click 'Yes' to append to previous group.", System.lineSeparator(),
                "Click 'No' to start new group.");
        Label label = new Label(text);
        label.setFont(Font.font("Candara", 12));
        addTokenConfirmationAlert.getDialogPane().setContent(label);

        addTokenConfirmationAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    }

}
