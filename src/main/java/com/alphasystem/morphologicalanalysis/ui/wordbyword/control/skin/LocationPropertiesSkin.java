package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.MorphologicalEntry;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.AbstractPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.CommonPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.LocationPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.MorphologicalEntryView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.NounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.ProNounPropertiesView;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.VerbPropertiesView;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.util.RepositoryTool;
import com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.NounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ProNounProperties;
import com.alphasystem.morphologicalanalysis.wordbyword.model.VerbProperties;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

import static com.alphasystem.fx.ui.util.UiUtilities.defaultCursor;
import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.fx.ui.util.UiUtilities.waitCursor;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.isNoun;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.isPronoun;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.isVerb;
import static java.lang.String.format;

/**
 * @author sali
 */
public class LocationPropertiesSkin extends SkinBase<LocationPropertiesView> {

    private MorphologicalAnalysisRepositoryUtil repositoryUtil = RepositoryTool.getInstance().getRepositoryUtil();

    public LocationPropertiesSkin(LocationPropertiesView control) {
        super(control);
        getChildren().setAll(new SkinView());
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

    private class RetrieveMorphologicalEntryService extends Service<MorphologicalEntry> {

        private final MorphologicalEntry source;
        private final RootLetters rootLetters;
        private final NamedTemplate form;

        private RetrieveMorphologicalEntryService(MorphologicalEntry source, RootLetters rootLetters, NamedTemplate form) {
            this.source = source;
            this.rootLetters = rootLetters;
            this.form = form;
        }

        @Override
        protected Task<MorphologicalEntry> createTask() {
            return new Task<MorphologicalEntry>() {
                @Override
                protected MorphologicalEntry call() throws Exception {
                    waitCursor(getSkinnable());
                    MorphologicalEntry entry = new MorphologicalEntry(rootLetters, form);
                    MorphologicalEntry savedEntry = repositoryUtil.findMorphologicalEntry(entry);
                    // if current entry is same as the one just retrieved from DB, then we will not update the UI
                    return (savedEntry == null || savedEntry.equals(source)) ? null : savedEntry;
                }
            };
        }
    }

    private class SkinView extends BorderPane {

        @FXML
        private CommonPropertiesView commonPropertiesView;

        @FXML
        private TitledPane propertiesTitledPane;

        @FXML
        private NounPropertiesView nounPropertiesView;

        @FXML
        private ProNounPropertiesView proNounPropertiesView;

        @FXML
        private VerbPropertiesView verbPropertiesView;

        @FXML
        private ParticlePropertiesView particlePropertiesView;

        @FXML
        private MorphologicalEntryView morphologicalEntryView;

        private SkinView() {
            init();
        }

        private void init() {
            URL fxmlURL = getClass().getResource(format("/fxml/%s.fxml",
                    getSkinnable().getClass().getSimpleName()));
            try {
                loadFXML(this, fxmlURL, RESOURCE_BUNDLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        void initialize() {
            final LocationPropertiesView view = getSkinnable();
            final Location location = view.getLocation();

            commonPropertiesView.setLocation(location);
            view.locationProperty().addListener((o, ov, nv) -> {
                commonPropertiesView.setLocation(nv);
                changePropertiesView(nv);
                changeMorphologicalEntryView(nv);
            });
            commonPropertiesView.partOfSpeechProperty().addListener((o, ov, nv) -> changePropertiesView(view.getLocation()));

            morphologicalEntryView.setMorphologicalEntry(getMorphologicalEntry(location));
            morphologicalEntryView.rootLettersProperty().addListener((o, ov, nv) -> {
                getSkinnable().setRootLetters(nv);
                if (nv != null && !nv.isEmpty()) {
                    MorphologicalEntry morphologicalEntry = morphologicalEntryView.getMorphologicalEntry();
                    retrieveEntry(morphologicalEntry, nv, morphologicalEntry.getForm());
                }
            });
            morphologicalEntryView.formProperty().addListener((o, ov, nv) -> {
                getSkinnable().setForm(nv);
                if (nv != null) {
                    MorphologicalEntry morphologicalEntry = morphologicalEntryView.getMorphologicalEntry();
                    retrieveEntry(morphologicalEntry, morphologicalEntry.getRootLetters(), nv);
                }
            });

            AbstractProperties properties = (location == null) ? null : location.getProperties();
            propertiesTitledPane.setText(getPropertiesPaneTitle(properties));
            propertiesTitledPane.setContent(getPropertiesView(properties));
        }

        private MorphologicalEntry getMorphologicalEntry(Location location) {
            if (location == null) {
                return null;
            }
            MorphologicalEntry morphologicalEntry = location.getMorphologicalEntry();
            if (morphologicalEntry == null) {
                morphologicalEntry = new MorphologicalEntry();
                location.setMorphologicalEntry(morphologicalEntry);
            }
            return morphologicalEntry;
        }

        @SuppressWarnings({"unchecked"})
        private AbstractPropertiesView getPropertiesView(AbstractProperties properties) {
            if (isNoun(properties)) {
                nounPropertiesView.setLocationProperties((NounProperties) properties);
                return nounPropertiesView;
            } else if (isPronoun(properties)) {
                proNounPropertiesView.setLocationProperties((ProNounProperties) properties);
                return proNounPropertiesView;
            } else if (isVerb(properties)) {
                verbPropertiesView.setLocationProperties((VerbProperties) properties);
                return verbPropertiesView;
            } else {
                particlePropertiesView.setLocationProperties((ParticleProperties) properties);
                return particlePropertiesView;
            }
        }

        private void retrieveEntry(final MorphologicalEntry morphologicalEntry, RootLetters nv, NamedTemplate form) {
            if (nv == null || nv.isEmpty() || form == null) {
                return;
            }
            RetrieveMorphologicalEntryService service = new RetrieveMorphologicalEntryService(
                    morphologicalEntry, nv, form);
            service.setOnSucceeded(event -> {
                defaultCursor(getSkinnable());
                Worker source = event.getSource();
                MorphologicalEntry value = (MorphologicalEntry) source.getValue();
                if (value == null) {
                    value = morphologicalEntry;
                } else {
                    getSkinnable().getLocation().setMorphologicalEntry(value);
                }
                morphologicalEntryView.setMorphologicalEntry(value);
            });
            service.setOnFailed(event -> {
                defaultCursor(getSkinnable());
                Worker source = event.getSource();
                source.getException().printStackTrace();
            });
            service.start();
        }

        private void changePropertiesView(Location location) {
            AbstractProperties properties;
            if (location == null) {
                properties = new NounProperties();
            } else {
                properties = location.getProperties();
            }
            propertiesTitledPane.setContent(getPropertiesView(properties));
            propertiesTitledPane.setText(getPropertiesPaneTitle(properties));
        }

        private void changeMorphologicalEntryView(Location location) {
            morphologicalEntryView.setMorphologicalEntry(getMorphologicalEntry(location));
        }
    }
}
