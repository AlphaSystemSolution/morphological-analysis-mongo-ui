package com.alphasystem.app.morphologicalengine.ui.skin;

import com.alphasystem.app.morphologicalengine.conjugation.model.DetailedConjugation;
import com.alphasystem.app.morphologicalengine.conjugation.model.NounDetailedConjugationPair;
import com.alphasystem.app.morphologicalengine.conjugation.model.VerbDetailedConjugationPair;
import com.alphasystem.app.morphologicalengine.ui.DetailedConjugationView;
import com.alphasystem.app.morphologicalengine.ui.NounDetailedConjugationPairView;
import com.alphasystem.app.morphologicalengine.ui.VerbDetailedConjugationPairView;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author sali
 */
public class DetailedConjugationSkin extends SkinBase<DetailedConjugationView> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public DetailedConjugationSkin(DetailedConjugationView control) {
        super(control);
        getChildren().setAll(new SkinView(control));
    }

    private class SkinView extends BorderPane {

        private final DetailedConjugationView control;
        private final VBox pane = new VBox();

        private SkinView(DetailedConjugationView control) {
            this.control = control;
            initialize();
            setCenter(pane);
        }

        private void initialize() {
            pane.setAlignment(Pos.CENTER);
            pane.setSpacing(12);

            control.detailedConjugationProperty().addListener((observable, oldValue, newValue) -> setup(newValue));
            setup(control.getDetailedConjugation());
        }

        private void setup(DetailedConjugation detailedConjugation) {
            pane.getChildren().remove(0, pane.getChildren().size());
            if (detailedConjugation != null) {
                addVerbPair(detailedConjugation.getActiveTensePair());
                addNounPair(detailedConjugation.getActiveParticiplePair());
                addNounPairs(detailedConjugation.getVerbalNounPairs());
                addVerbPair(detailedConjugation.getPassiveTensePair());
                addNounPair(detailedConjugation.getPassiveParticiplePair());
                addVerbPair(detailedConjugation.getImperativeAndForbiddingPair());
                addNounPairs(detailedConjugation.getAdverbPairs());
            }
        }

        private void addVerbPair(VerbDetailedConjugationPair pair) {
            if (pair == null) {
                return;
            }
            VerbDetailedConjugationPairView verbPairControl = new VerbDetailedConjugationPairView();
            verbPairControl.setPair(pair);
            pane.getChildren().add(verbPairControl);
        }

        private void addNounPair(NounDetailedConjugationPair pair) {
            if (pair == null) {
                return;
            }
            NounDetailedConjugationPairView nounPairControl = new NounDetailedConjugationPairView();
            nounPairControl.setPair(pair);
            pane.getChildren().add(nounPairControl);
        }

        private void addNounPairs(NounDetailedConjugationPair[] pairs) {
            if (!ArrayUtils.isEmpty(pairs)) {
                for (NounDetailedConjugationPair pair : pairs) {
                    addNounPair(pair);
                }
            }
        }
    }
}
