package com.alphasystem.app.morphologicalengine.ui.skin;

import com.alphasystem.app.morphologicalengine.conjugation.model.ConjugationGroup;
import com.alphasystem.app.morphologicalengine.conjugation.model.DetailedConjugationPair;
import com.alphasystem.app.morphologicalengine.ui.ConjugationGroupView;
import com.alphasystem.app.morphologicalengine.ui.DetailedConjugationPairView;
import com.alphasystem.fx.ui.util.UiUtilities;
import com.alphasystem.util.AppUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author sali
 */
abstract class DetailedConjugationPairSkin<G extends ConjugationGroup, P extends DetailedConjugationPair<G>,
        GC extends ConjugationGroupView<G>, C extends DetailedConjugationPairView<G, P>> extends BorderPane {

    protected final C control;

    @FXML private GC left;
    @FXML private GC right;

    DetailedConjugationPairSkin(C control) {
        this.control = control;
        try {
            UiUtilities.loadFXML(this, AppUtil.getUrl(String.format("fxml.%s.fxml", control.getClass().getSimpleName())), null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @FXML
    void initialize() {
        control.leftProperty().addListener((observable, oldValue, newValue) -> left.setGroup(newValue));
        control.rightProperty().addListener((observable, oldValue, newValue) -> right.setGroup(newValue));

        G group = control.getLeft();
        if (group != null) {
            left.setGroup(group);
        }

        group = control.getRight();
        if (group != null) {
            right.setGroup(group);
        }
    }
}
