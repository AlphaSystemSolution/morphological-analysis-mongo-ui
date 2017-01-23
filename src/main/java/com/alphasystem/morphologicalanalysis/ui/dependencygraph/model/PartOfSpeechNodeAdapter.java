package com.alphasystem.morphologicalanalysis.ui.dependencygraph.model;

import com.alphasystem.morphologicalanalysis.graph.model.PartOfSpeechNode;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author sali
 */
public class PartOfSpeechNodeAdapter extends LinkSupportAdapter<PartOfSpeechNode> {

    private final ReadOnlyStringWrapper locationText = new ReadOnlyStringWrapper(this, "locationText");
    private final BooleanProperty hidden = new SimpleBooleanProperty();

    public PartOfSpeechNodeAdapter() {
        super();
    }

    @Override
    protected void initValues(PartOfSpeechNode graphNode) {
        super.initValues(graphNode);
        if (graphNode != null) {
            final Location location = graphNode.getLocation();
            if (location != null) {
                locationText.setValue(location.getText());
            }
        }
        setHidden(graphNode != null && graphNode.isHidden());
        hiddenProperty().addListener((observable, oldValue, newValue) -> getSrc().setHidden(newValue));
    }

    public final String getLocationText() {
        return locationText.get();
    }

    public boolean isHidden() {
        return hidden.get();
    }

    public void setHidden(boolean hidden) {
        this.hidden.set(hidden);
    }

    public BooleanProperty hiddenProperty() {
        return hidden;
    }

}
