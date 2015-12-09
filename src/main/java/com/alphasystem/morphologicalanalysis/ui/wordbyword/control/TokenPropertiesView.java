package com.alphasystem.morphologicalanalysis.ui.wordbyword.control;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.ArabicSupport;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.ui.wordbyword.control.skin.TokenPropertiesSkin;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
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
    private final ObservableList<LocationLabel> labels = observableArrayList();
    private final ReadOnlyIntegerWrapper from = new ReadOnlyIntegerWrapper(-1, "from");
    private final ReadOnlyIntegerWrapper to = new ReadOnlyIntegerWrapper(-1, "to");
    private final ReadOnlyObjectWrapper<SelectionStatus> newStatus = new ReadOnlyObjectWrapper<>(null, "newStatus");

    public TokenPropertiesView() {
        tokenProperty().addListener((o, ov, nv) -> {
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
        labels.addListener((ListChangeListener<LocationLabel>) c -> {
            while (c.next()) {
                int addedSize = c.getAddedSize();
                // if something added in the list that becuase user has selected / un-selected label
                // now if it was the "selected" action then start from index of selected label going backward and make
                // all labels selected as long as the status is not "NOT_AVAILABLE", do similar action for "un-select
                // this will allow user to select entire labels by selecting last label.
                if (addedSize > 0) {
                    List<LocationLabel> addedSubList = (List<LocationLabel>) c.getAddedSubList();
                    int index = addedSize - 1;
                    LocationLabel locationLabel = addedSubList.get(index);
                    SelectionStatus status = locationLabel.getStatus();
                    index = locationLabel.getIndex();
                    to.setValue(index + 1);
                    newStatus.setValue(status);
                    setStatus:
                    for (int i = index - 1; i >= 0; i--) {
                        LocationLabel ll = labels.get(i);
                        if (ll.getStatus().equals(SelectionStatus.NOT_AVAILABLE)) {
                            break setStatus;
                        }
                        from.setValue(i);
                        ll.setStatus(status);
                    }
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

    public final Integer getFrom() {
        return from.get();
    }

    public final Integer getTo() {
        return to.get();
    }

    public final SelectionStatus getNewStatus() {
        return newStatus.get();
    }

    public void changeStatuses(Location currentLocation) {
        // first reset all the label status to available
        labels.forEach(locationLabel -> locationLabel.setStatus(SelectionStatus.AVAILABLE));

        Token token = getToken();
        if (token == null || currentLocation == null) {
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
            SelectionStatus status = location.equals(currentLocation) ? SelectionStatus.SELECTED :
                    SelectionStatus.NOT_AVAILABLE;
            for (int i = startIndex; i < endIndex; i++) {
                labels.get(i).setStatus(status);
            }
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
        public ArabicWord getLabel() {
            return getLetter().getLabel();
        }

        @Override
        public String toString() {
            return format("%s: %s : %s", getLabel().toBuckWalter(), getStatus(), getIndex());
        }
    }
}
