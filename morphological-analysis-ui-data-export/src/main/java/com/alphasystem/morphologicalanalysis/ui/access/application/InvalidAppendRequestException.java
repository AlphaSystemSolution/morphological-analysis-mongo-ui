package com.alphasystem.morphologicalanalysis.ui.access.application;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Token;

/**
 * @author sali
 */
public class InvalidAppendRequestException extends Exception {

    InvalidAppendRequestException(String message) {
        super(message);
    }

    InvalidAppendRequestException(final Integer chapterNumber, final Integer previousChapterNumber) {
        this(createMessage(chapterNumber, previousChapterNumber));
    }

    InvalidAppendRequestException(final Token token, final Token previousToken){
        this(createMessage(token, previousToken));
    }

    private static String createMessage(final Integer chapterNumber, final Integer previousChapterNumber) {
        return String.format("Unable to append since given chapter number {%s} is different then previous chapter number {%s}",
                chapterNumber, previousChapterNumber);
    }

    private static String createMessage(final Token token, final Token previousToken) {
        return String.format("Unable to append since given token {%s} is not the next token of {%s}", token, previousToken);
    }
}
