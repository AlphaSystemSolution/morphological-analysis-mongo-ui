package com.alphasystem.morphologicalanalysis.ui.tokeneditor.application;

import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.WordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sali
 */
public final class ApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private static ApplicationController instance;

    /**
     * Do not let any one instantiate this class.
     */
    private ApplicationController() {
    }

    public static synchronized ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    private static String getSubText(ArabicWord arabicWord, int startIndex, int endIndex) {
        return ArabicWord.getSubWord(arabicWord, startIndex, endIndex).toUnicode();
    }

    public void updateLocation(Location location, int newEndIndex, ArabicWord tokenWord) {
        LOGGER.info("Current Location: {}, Start Index: {}, End Index: {}, Text: {}", location,
                location.getStartIndex(), location.getEndIndex(), location.getText());
        // following properties needs to be updated
        // endIndex - new value should be newLocationStartIndex
        // text & derivedText - based on startIndex and new endIndex
        location.setEndIndex(newEndIndex);
        final String text = getSubText(tokenWord, location.getStartIndex(), location.getEndIndex());
        location.setText(text);
        location.setDerivedText(text);
        LOGGER.info("Current Location After: {}, Start Index: {}, End Index: {}, Text: {}", location,
                location.getStartIndex(), location.getEndIndex(), location.getText());
    }

    public Location createNewLocation(Location currentLocation, int newLocationStartIndex, ArabicWord tokenWord, WordType wordType) {
        Location newLocation = new Location(currentLocation.getChapterNumber(), currentLocation.getVerseNumber(),
                currentLocation.getTokenNumber(), currentLocation.getLocationNumber() + 1, wordType);
        newLocation.setStartIndex(newLocationStartIndex);
        newLocation.setEndIndex(tokenWord.getLength());
        final String text = getSubText(tokenWord, newLocation.getStartIndex(), newLocation.getEndIndex());
        newLocation.setText(text);
        newLocation.setDerivedText(text);
        LOGGER.info("New Location: {}, Start Index: {}, End Index: {}, Text: {}", newLocation,
                newLocation.getStartIndex(), newLocation.getEndIndex(), newLocation.getText());
        return newLocation;
    }


}
