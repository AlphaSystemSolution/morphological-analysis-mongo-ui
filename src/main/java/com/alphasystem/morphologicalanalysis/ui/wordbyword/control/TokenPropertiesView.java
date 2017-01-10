package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.TokenPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.List;

import static java.lang.String.format;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class TokenPropertiesView extends Control {

    private final ObjectProperty<Token> token = new SimpleObjectProperty<>(null, "token");
    private final ObjectProperty<Location> selectedLocation = new SimpleObjectProperty<>(null, "selectedLocation");
    private final ObservableList<LocationLabel> labels = observableArrayList();

    public TokenPropertiesView() {
        tokenProperty().addListener((o, ov, nv) -> {
            // Following code was trying to update start and end indices upon changing the token, but this was
            // changing the actual data

            // TODO: find out implications
            // Location selectedLocation = this.selectedLocation.get();
            // updateStartAndEndIndex(selectedLocation);
            labels.clear();
            if (nv != null) {
                ArabicWord tokenWord = nv.getTokenWord();
                ArabicLetter[] letters = tokenWord.getLetters();
                // first initialize all the letters with "AVAILABLE" status
                int index = 0;
                for (ArabicLetter letter : letters) {
                    labels.add(new LocationLabel(letter, index++));
                }
            }
        });
    }

    public final Token getToken() {
        return token.get();
    }

    public final void setToken(Token token) {
        this.token.set(token);
    }

    public final ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public ObservableList<LocationLabel> getLabels() {
        return labels;
    }

    public ObjectProperty<Location> selectedLocationProperty() {
        return selectedLocation;
    }

    public final void setSelectedLocation(int index) {
        Location location = getToken().getLocations().get(index);
        selectedLocation.setValue(location);
    }

    public final int getSelectedLocationIndex() {
        Location location = selectedLocation.get();
        return getToken().getLocations().indexOf(location);
    }

    /**
     * Updates the current token and selected location for start and end indices.
     * <div></div>
     * <div>
     * <strong>Implementation Note:-</strong> Upon selecting token letters thr start and end indices doesn't get
     * updated, if we change the location or token then last selected location will get updated but from UI when
     * user is clicking "OK" or "Apply" button there is no way to update location, this method will get called from UI
     * to update selected location.
     * </div>
     */
    public void updateToken() {
        updateStartAndEndIndex(selectedLocation.get());
    }

    public void changeLocation(Location oldLocation, Location newLocation) {
        // make sure we saves values in old location before resetting vales
        updateStartAndEndIndex(oldLocation);
        selectedLocation.setValue(newLocation);

        // first reset all the label status to available
        labels.forEach(locationLabel -> locationLabel.setStatus(SelectionStatus.AVAILABLE));

        Token token = getToken();
        if (token == null || newLocation == null) {
            return;
        }
        // next pass go though locations of this token make labels either "SELECTED" or "NOT_AVAILABLE" based on
        // their start and end indices
        List<Location> locations = token.getLocations();
        for (Location location : locations) {
            // no need to do any thing if location does not have start and end index, this location may not have
            // been set yet
            if (location.isTransient()) {
                continue;
            }
            Integer startIndex = location.getStartIndex();
            Integer endIndex = location.getEndIndex();
            // if this location is our current location then make status "SELECTED" of letters in the range of
            // startIndex (inclusive) and endIndex (exclusive), otherwise should be part of other locations
            // make them "NOT_AVAILABLE"
            SelectionStatus status = location.equals(newLocation) ? SelectionStatus.SELECTED :
                    SelectionStatus.NOT_AVAILABLE;
            for (int i = startIndex; i < endIndex; i++) {
                labels.get(i).setStatus(status);
            }
        }
    }

    private void updateStartAndEndIndex(Location oldLocation) {
        if (oldLocation != null) {
            int startIndex = -1;
            int endIndex = -1;
            for (int i = 0; i < labels.size(); i++) {
                LocationLabel locationLabel = labels.get(i);
                if (locationLabel.getStatus().equals(SelectionStatus.SELECTED)) {
                    if (startIndex <= -1) {
                        startIndex = i;
                    }
                    endIndex = i;
                } // end of if
            } // end of for
            // endIndex in exclusive
            endIndex++;
            oldLocation.setStartIndex(startIndex);
            oldLocation.setEndIndex(endIndex);
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TokenPropertiesSkin(this);
    }

    /**
     * Represents the selection status of letters in current token
     */
    public enum SelectionStatus {

        /**
         * This letter is not part of any location and available to get selected
         */
        AVAILABLE,

        /**
         * This letter part of other location and un-available for selection
         */
        NOT_AVAILABLE,

        /**
         * This letter is part of current location and already selected
         */
        SELECTED
    }

    /**
     * Represents a letter of current token
     */
    public static class LocationLabel implements ArabicSupport {

        private final ArabicLetter letter;
        private final int index;
        private SelectionStatus status;

        /**
         * Initialize the instance with given status and letter
         *
         * @param status status of this letter
         * @param letter this letter
         * @param index  index of this letter
         */
        public LocationLabel(SelectionStatus status, ArabicLetter letter, int index) {
            this.status = status == null ? SelectionStatus.AVAILABLE : status;
            this.letter = letter;
            this.index = index;
        }

        /**
         * Initialize the instance with {@link SelectionStatus#AVAILABLE} status
         *
         * @param letter this letter
         * @param index  index of this letter
         */
        private LocationLabel(ArabicLetter letter, int index) {
            this(SelectionStatus.AVAILABLE, letter, index);
        }

        public SelectionStatus getStatus() {
            return status;
        }

        public void setStatus(SelectionStatus status) {
            this.status = status;
        }

        public ArabicLetter getLetter() {
            return letter;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public ArabicWord toLabel() {
            return getLetter().toLabel();
        }

        @Override
        public String toString() {
            return format("{'label': '%s', 'status': '%s', 'index': %s}", toLabel().toBuckWalter(), getStatus(), getIndex());
        }
    }
}
