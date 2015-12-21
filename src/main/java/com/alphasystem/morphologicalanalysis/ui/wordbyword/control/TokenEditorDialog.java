package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.ApplicationErrorCode;
import com.alphasystem.SystemException;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
public class TokenEditorDialog extends Dialog<Token> {

    private final ObjectProperty<Token> token = new SimpleObjectProperty<>(null, "token");
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
            if (true) {
                System.out.println("Inside applyButton.setResultConverter");
                return null;
            }
            System.out.println("Inside applyButton.setResultConverter, why we are here");
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

        tokenProperty().addListener((o, ov, nv) -> setTitle(getTitle(nv)));
        view.tokenProperty().bind(tokenProperty());
        getDialogPane().setContent(view);
        getDialogPane().getButtonTypes().addAll(APPLY, OK, CLOSE);
        Button applyButton = (Button) getDialogPane().lookupButton(APPLY);
        applyButton.addEventFilter(ACTION, event -> {
            if (true) {
                System.out.println("Inside applyButton.addEventFilter");
                event.consume();
                return;
            }
            System.out.println("Inside applyButton.addEventFilter, why we are here");
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
    }

    private static String getTitle(Token token) {
        String displayName = (token == null) ? "" : format("(%s)", token.getDisplayName());
        return format("Edit token %s", displayName);
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

    public final ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public final void setToken(Token token) {
        this.token.set(token);
    }
}
