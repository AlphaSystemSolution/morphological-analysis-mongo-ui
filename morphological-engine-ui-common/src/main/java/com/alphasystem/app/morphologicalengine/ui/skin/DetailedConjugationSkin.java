package com.alphasystem.app.morphologicalengine.ui.skin;

import org.apache.commons.lang3.ArrayUtils;

import com.alphasystem.app.morphologicalengine.ui.DetailedConjugationView;
import com.alphasystem.app.morphologicalengine.ui.NounDetailedConjugationPairView;
import com.alphasystem.app.morphologicalengine.ui.VerbDetailedConjugationPairView;
import com.alphasystem.morphologicalengine.model.DetailedConjugation;
import com.alphasystem.morphologicalengine.model.NounConjugationGroup;
import com.alphasystem.morphologicalengine.model.NounDetailedConjugationPair;
import com.alphasystem.morphologicalengine.model.VerbConjugationGroup;
import com.alphasystem.morphologicalengine.model.VerbDetailedConjugationPair;

import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
                addVerbPairs(detailedConjugation.getPresentTense(), detailedConjugation.getPastTense());
                addNounPairs(detailedConjugation.getActiveParticipleFeminine(), detailedConjugation.getActiveParticipleMasculine());
                addNounPairs(detailedConjugation.getVerbalNouns());
                addVerbPairs(detailedConjugation.getPresentPassiveTense(), detailedConjugation.getPastPassiveTense());
                addNounPairs(detailedConjugation.getPassiveParticipleFeminine(), detailedConjugation.getPassiveParticipleFeminine());
                addVerbPairs(detailedConjugation.getForbidding(), detailedConjugation.getImperative());
                addNounPairs(detailedConjugation.getAdverbs());
            }
        }

        private void addVerbPairs(VerbConjugationGroup leftPair, VerbConjugationGroup rightPair) {
            if (rightPair == null && leftPair == null) {
                return;
            }
            VerbDetailedConjugationPairView verbPairControl = new VerbDetailedConjugationPairView();
            verbPairControl.setPair(new VerbDetailedConjugationPair(leftPair, rightPair));
            pane.getChildren().add(verbPairControl);
        }

        private void addNounPairs(NounConjugationGroup leftPair, NounConjugationGroup rightPair) {
            if (rightPair == null && leftPair == null) {
                return;
            }
            NounDetailedConjugationPairView nounPairControl = new NounDetailedConjugationPairView();
            nounPairControl.setPair(new NounDetailedConjugationPair(leftPair, rightPair));
            pane.getChildren().add(nounPairControl);
        }

        private void addNounPairs(NounConjugationGroup[] pairs) {
            if (!ArrayUtils.isEmpty(pairs)) {
                int index = 0;
                while (index < pairs.length) {
                    final NounConjugationGroup rightPair = pairs[index++];
                    final NounConjugationGroup leftPair = (index >= pairs.length) ? null : pairs[index];
                    addNounPairs(leftPair, rightPair);
                }
            }
        }
    }
}
