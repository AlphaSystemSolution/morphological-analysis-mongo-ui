package com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.controller;

import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.DetailEditorView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenEditorView;
import com.alphasystem.morphologicalanalysis.ui.tokeneditor.control.TokenPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.util.ApplicationHelper;
import com.alphasystem.morphologicalanalysis.ui.util.RestClient;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author sali
 */
@Component
public class TokenEditorController extends BorderPane {

    @Autowired private RestClient restClient;
    @Autowired private TokenEditorView control;
    @Autowired private TokenPropertiesView tokenPropertiesView;
    @Autowired private DetailEditorView detailEditorView;
    @FXML private MenuBar menuBar;
    @FXML private ToolBar toolBar;

    private static String getTokenPropertiesViewPaneTitle(Token token) {
        String title = "Token Properties - %s";
        String tokenDisplayName = token == null ? "Unknown" : token.getDisplayName();
        return String.format(title, tokenDisplayName);
    }

    @PostConstruct
    void postConstruct() throws IOException, URISyntaxException {
        ApplicationHelper.loadFxml(control, this);
    }

    @FXML
    void initialize() {
        TitledPane tokenPropertiesViewPane = new TitledPane(getTokenPropertiesViewPaneTitle(this.control.getToken()), tokenPropertiesView);
        control.tokenProperty().addListener((observable, oldValue, newValue) -> {
            tokenPropertiesViewPane.setText(getTokenPropertiesViewPaneTitle(newValue));
            tokenPropertiesView.setToken(newValue);
        });

        tokenPropertiesView.setToken(control.getToken());
        detailEditorView.setLocation(tokenPropertiesView.getSelectedLocation());
        detailEditorView.locationProperty().bind(tokenPropertiesView.selectedLocationProperty());

        final VBox top = (VBox) getTop();
        top.getChildren().add(tokenPropertiesViewPane);
        setCenter(detailEditorView);
    }

    @FXML
    private void onSave() {
        UiUtilities.waitCursor(control);
        Service<Token> service = new Service<Token>() {
            @Override
            protected Task<Token> createTask() {
                return new Task<Token>() {
                    @Override
                    protected Token call() throws Exception {
                        return restClient.saveToken(control.getToken());
                    }
                };
            }
        };
        service.setOnSucceeded(event -> UiUtilities.defaultCursor(control));
        service.setOnFailed(event -> {
            UiUtilities.defaultCursor(control);
            try {
                throw event.getSource().getException();
            } catch (Throwable throwable) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(String.format("%s:%s", throwable.getClass().getName(), throwable.getMessage()));
                alert.showAndWait();
            }
        });
        service.start();
    }

    @FXML
    private void onNext() {

    }

    @FXML
    private void onPrevious() {

    }
}
