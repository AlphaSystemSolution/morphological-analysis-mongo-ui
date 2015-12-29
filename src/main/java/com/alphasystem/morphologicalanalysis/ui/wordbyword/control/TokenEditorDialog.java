package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.ApplicationErrorCode;
import com.alphasystem.SystemException;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.stage.Screen;

import java.util.List;

import static com.alphasystem.util.AppUtil.NEW_LINE;
import static java.lang.String.format;
import static javafx.event.ActionEvent.ACTION;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ButtonType.*;
import static javafx.stage.Modality.NONE;

/**
 * @author sali
 */
@Deprecated
public class TokenEditorDialog extends Dialog<Token> {

    private final ObjectProperty<Token> token = new SimpleObjectProperty<>(null, "token");
    private final ObjectProperty<Token> nextToken = new SimpleObjectProperty<>(null, "nextToken");
    private final ObjectProperty<Token> previousToken = new SimpleObjectProperty<>(null, "previousToken");
    private final TokenPropertiesView view = new TokenPropertiesView();
    private final RepositoryTool repositoryTool = RepositoryTool.getInstance();

    public TokenEditorDialog() {
        setTitle(getTitle(null));
        setResizable(true);
        initModality(NONE);

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double offset = 50.0;
        setWidth(visualBounds.getWidth() - offset);
        setHeight(visualBounds.getHeight() - offset);
        setX(offset);
        setY(offset);

        setResultConverter(param -> {
            Token token = null;
            if (param.getButtonData().isDefaultButton()) {
                try {
                    token = updateToken();
                } catch (SystemException e) {
                    showError(e);
                }
            }
            return token;
        });

        token.addListener((o, ov, nv) -> updateDialog(nv));
        view.tokenProperty().bind(token);
        getDialogPane().setContent(view);
        getDialogPane().getButtonTypes().addAll(NEXT, PREVIOUS, APPLY, OK, CLOSE);
        Button applyButton = (Button) getDialogPane().lookupButton(APPLY);
        applyButton.addEventFilter(ACTION, event -> {
            Token t = null;
            try {
                t = updateToken();
                setToken(null);
                setToken(t);
            } catch (SystemException e) {
                showError(e);
            } finally {
                event.consume();
            }
        });
        Button nextButton = (Button) getDialogPane().lookupButton(NEXT);
        nextButton.disableProperty().bind(nextToken.isNull());
        nextButton.addEventFilter(ACTION, event -> {
            setToken(nextToken.get());
            event.consume();
        });

        Button previousButton = (Button) getDialogPane().lookupButton(PREVIOUS);
        previousButton.disableProperty().bind(previousToken.isNull());
        previousButton.addEventFilter(ACTION, event -> {
            setToken(previousToken.get());
            event.consume();
        });
    }

    private static String getTitle(Token token) {
        String displayName = (token == null) ? "" : format("(%s)", token.getDisplayName());
        return format("Edit token %s", displayName);
    }

    private void updateDialog(Token newToken) {
        setTitle(getTitle(newToken));
        getPreviousToken(newToken);
        getNextToken(newToken);
    }

    private void getPreviousToken(Token newToken) {
        // get new token in separate thread
        RetrieveTokenService getPreviousTokenService = new RetrieveTokenService(newToken, false);
        getPreviousTokenService.setOnFailed(event -> {
            // do some thing
            event.getSource().getException().printStackTrace();
        });
        getPreviousTokenService.setOnSucceeded(event -> {
            Token token = (Token) event.getSource().getValue();
            previousToken.setValue(token);
        });
        getPreviousTokenService.start();
    }

    private void getNextToken(Token newToken) {
        // get new token in separate thread
        RetrieveTokenService getNextTokenService = new RetrieveTokenService(newToken, true);
        getNextTokenService.setOnFailed(event -> {
            // do some thing
            event.getSource().getException().printStackTrace();
        });
        getNextTokenService.setOnSucceeded(event -> {
            Token token = (Token) event.getSource().getValue();
            nextToken.setValue(token);
        });
        getNextTokenService.start();
    }

    private void showError(SystemException e) {
        Alert alert = new Alert(ERROR);
        List<ApplicationErrorCode> errorCodes = e.getErrorCodes();
        ApplicationErrorCode errorCode = errorCodes.get(0);
        alert.setContentText(errorCode.getCode());
    }

    private Token updateToken() throws SystemException {
        view.updateToken();
        Token token = view.getToken();
        List<Location> locations = token.getLocations();
        StringBuilder builder = new StringBuilder();
        locations.forEach(location -> {
            if (location.isTransient()) {
                builder.append(format("Location {%s} does not have start and end indices set", location)).append(NEW_LINE);
            }
        });
        if (builder.length() > 0) {
            throw new SystemException(builder.toString());
        }
        return repositoryTool.saveToken(token);
    }

    public final void setToken(Token token) {
        this.token.set(token);
    }

    private class RetrieveTokenService extends Service<Token> {

        private final Token referenceToken;
        private final boolean next;

        private RetrieveTokenService(Token referenceToken, boolean next) {
            this.referenceToken = referenceToken;
            this.next = next;
        }

        @Override
        protected Task<Token> createTask() {
            return new Task<Token>() {
                @Override
                protected Token call() throws Exception {
                    return next ? repositoryTool.getNextToken(referenceToken) :
                            repositoryTool.getPreviousToken(referenceToken);
                }
            };
        }
    }

}
