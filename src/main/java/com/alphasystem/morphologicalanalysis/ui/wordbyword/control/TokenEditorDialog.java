package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.stage.Screen;

import static java.lang.String.format;
import static javafx.event.ActionEvent.ACTION;
import static javafx.scene.control.ButtonType.*;

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

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double offset = 50.0;
        setWidth(visualBounds.getWidth() - offset);
        setHeight(visualBounds.getHeight() - offset);
        setX(offset);
        setY(offset);

        setResultConverter(param -> param.getButtonData().isDefaultButton() ? updateToken() : null);

        tokenProperty().addListener((o, ov, nv) -> setTitle(getTitle(nv)));
        view.tokenProperty().bind(tokenProperty());
        getDialogPane().setContent(view);
        getDialogPane().getButtonTypes().addAll(APPLY, OK, CLOSE);
        Button applyButton = (Button) getDialogPane().lookupButton(APPLY);
        applyButton.addEventFilter(ACTION, event -> {
            Token t = updateToken();
            setToken(null);
            setToken(t);
            event.consume();
        });
    }

    private static String getTitle(Token token) {
        String displayName = (token == null) ? "" : format("(%s)", token.getDisplayName());
        return format("Edit token %s", displayName);
    }

    private Token updateToken() {
        view.updateToken();
        return repositoryTool.saveToken(view.getToken());
    }

    public final ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public final void setToken(Token token) {
        this.token.set(token);
    }
}
