package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import static com.alphasystem.morphologicalanalysis.ui.common.Global.GAP;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.*;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;

/**
 * @author sali
 */
public class LocationPropertiesSkin extends SkinBase<LocationPropertiesView> {

    private CommonPropertiesView commonPropertiesView;
    private MorphologicalEntryView morphologicalEntryView;
    private NounPropertiesView nounPropertiesView;
    private ProNounPropertiesView proNounPropertiesView;
    private VerbPropertiesView verbPropertiesView;
    private ParticlePropertiesView particlePropertiesView;
    private TitledPane propertiesTitledPane;

    public LocationPropertiesSkin(LocationPropertiesView control) {
        super(control);
        initializeSkin();

        LocationPropertiesView view = getSkinnable();
        view.locationProperty().addListener((o, ov, nv) -> {
            commonPropertiesView.setLocation(nv);
            changePropertiesView(nv);
            changeMorphologicalEntryView(nv);
        });
        commonPropertiesView.partOfSpeechProperty().addListener((o, ov, nv) -> changePropertiesView(commonPropertiesView.getLocation()));
        morphologicalEntryView.rootLettersProperty().addListener((o, ov, nv) -> sendUpdatePropertyNotification(view, nv));
        morphologicalEntryView.formProperty().addListener((o, ov, nv) -> sendUpdatePropertyNotification(view, nv));
    }

    private static String getPropertiesPaneTitle(AbstractProperties properties) {
        String title = "Particle Properties";
        if (isNoun(properties)) {
            title = "Noun Properties";
        } else if (isPronoun(properties)) {
            title = "ProNoun Properties";
        } else if (isVerb(properties)) {
            title = "Verb Properties";
        }
        return title;
    }

    private static ScrollPane createScrollPane(Node content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(AS_NEEDED);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(content);
        return scrollPane;
    }

    private void sendUpdatePropertyNotification(LocationPropertiesView view, Object nv) {
        view.setUpdatedProperty(null);
        view.setUpdatedProperty(nv);
    }

    private void changePropertiesView(Location location) {
        AbstractProperties properties = location.getProperties();
        propertiesTitledPane.setContent(getPropertiesView(properties));
        propertiesTitledPane.setText(getPropertiesPaneTitle(properties));
    }

    private void changeMorphologicalEntryView(Location location) {
        morphologicalEntryView.setMorphologicalEntry(getMorphologicalEntry(location));
    }

    private void initializeSkin() {
        HBox hBox = new HBox();
        hBox.setSpacing(GAP);

        LocationPropertiesView view = getSkinnable();
        Location location = view.getLocation();
        commonPropertiesView = new CommonPropertiesView();
        commonPropertiesView.setLocation(location);

        TitledPane titledPane = new TitledPane("Location's Common Properties", createScrollPane(commonPropertiesView));
        hBox.getChildren().add(titledPane);

        AbstractProperties properties = (location == null) ? null : location.getProperties();
        propertiesTitledPane = new TitledPane(getPropertiesPaneTitle(properties), getPropertiesView(properties));
        hBox.getChildren().add(propertiesTitledPane);

        morphologicalEntryView = new MorphologicalEntryView();
        morphologicalEntryView.setMorphologicalEntry(getMorphologicalEntry(location));

        titledPane = new TitledPane("Morphological Entry Properties", createScrollPane(morphologicalEntryView));
        hBox.getChildren().add(titledPane);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(hBox);
        getChildren().add(borderPane);
    }

    private MorphologicalEntry getMorphologicalEntry(Location location) {
        MorphologicalEntry morphologicalEntry = location.getMorphologicalEntry();
        if (morphologicalEntry == null) {
            morphologicalEntry = new MorphologicalEntry();
            location.setMorphologicalEntry(morphologicalEntry);
        }
        return morphologicalEntry;
    }

    @SuppressWarnings({"unchecked"})
    private AbstractPropertiesView getPropertiesView(AbstractProperties properties) {
        AbstractPropertiesView propertiesView;
        if (isNoun(properties)) {
            if (nounPropertiesView == null) {
                nounPropertiesView = new NounPropertiesView();
            }
            propertiesView = nounPropertiesView;
        } else if (isPronoun(properties)) {
            if (proNounPropertiesView == null) {
                proNounPropertiesView = new ProNounPropertiesView();
            }
            propertiesView = proNounPropertiesView;
        } else if (isVerb(properties)) {
            if (verbPropertiesView == null) {
                verbPropertiesView = new VerbPropertiesView();
            }
            propertiesView = verbPropertiesView;
        } else {
            if (particlePropertiesView == null) {
                particlePropertiesView = new ParticlePropertiesView();
            }
            propertiesView = particlePropertiesView;
        }
        propertiesView.setLocationProperties(properties);
        return propertiesView;
    }
}
