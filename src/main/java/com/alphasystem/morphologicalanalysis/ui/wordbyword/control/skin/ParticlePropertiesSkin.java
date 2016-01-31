package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

import static com.alphasystem.fx.ui.util.UiUtilities.loadFXML;
import static com.alphasystem.morphologicalanalysis.ui.common.Global.RESOURCE_BUNDLE;
import static java.lang.String.format;

/**
 * @author sali
 */
public class ParticlePropertiesSkin extends AbstractPropertiesSkin<ParticleProperties, ParticlePropertiesView> {

    public ParticlePropertiesSkin(ParticlePropertiesView control) {
        super(control);
    }

    @Override
    protected void initializeSkin() {
        getChildren().setAll(new SkinView());
    }

    private class SkinView extends BorderPane {

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
    }
}
