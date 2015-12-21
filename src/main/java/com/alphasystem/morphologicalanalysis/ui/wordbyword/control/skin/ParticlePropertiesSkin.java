package com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin;

import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.ParticlePropertiesView;
import com.alphasystem.morphologicalanalysis.wordbyword.model.ParticleProperties;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.alphasystem.arabic.ui.util.FontConstants.ENGLISH_FONT_ITALIC_12;
import static javafx.geometry.Pos.CENTER;

/**
 * @author sali
 */
public class ParticlePropertiesSkin extends AbstractPropertiesSkin<ParticleProperties, ParticlePropertiesView> {

    public ParticlePropertiesSkin(ParticlePropertiesView control) {
        super(control);
    }

    @Override
    protected void initializeSkin() {
        FlowPane flowPane = new FlowPane();
        Text text = new Text("Particles does not have any properties.");
        text.setFont(ENGLISH_FONT_ITALIC_12);
        flowPane.getChildren().add(text);
        flowPane.setAlignment(CENTER);

        VBox vBox = new VBox();
        vBox.setSpacing(40);
        vBox.getChildren().addAll(new Pane(), flowPane, new Pane());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        getChildren().add(borderPane);
    }
}
