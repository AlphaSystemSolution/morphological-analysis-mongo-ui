package com.alphasystem.access.model;

import com.alphasystem.morphologicalanalysis.common.model.VerseTokenPairGroup;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.IdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sali
 */
public class QuestionData implements Comparable<QuestionData> {

    private final String id;
    private QuestionType type;
    private List<VerseTokenPairGroup> verseTokenPairGroups;
    private List<String> highlightedTokens;

    public QuestionData() {
        this(null);
    }

    public QuestionData(QuestionType type) {
        id = IdGenerator.nextId();
        setType(type);
        setVerseTokenPairGroups(null);
        setHighlightedTokens(null);
    }

    public String getId() {
        return id;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = (type == null) ? QuestionType.LIST : type;
    }

    public List<VerseTokenPairGroup> getVerseTokenPairGroups() {
        return verseTokenPairGroups;
    }

    public void setVerseTokenPairGroups(List<VerseTokenPairGroup> verseTokenPairGroups) {
        this.verseTokenPairGroups = new ArrayList<>();
        if (verseTokenPairGroups != null) {
            this.verseTokenPairGroups.addAll(verseTokenPairGroups);
        }
    }

    public void addVerseTokenPairGroup(VerseTokenPairGroup... verseTokenPairGroups) {
        Collections.addAll(this.verseTokenPairGroups, verseTokenPairGroups);
    }

    public List<String> getHighlightedTokens() {
        return highlightedTokens;
    }

    public void setHighlightedTokens(List<String> highlightedTokens) {
        this.highlightedTokens = new ArrayList<>();
        if (highlightedTokens != null) {
            this.highlightedTokens.addAll(highlightedTokens);
        }
    }

    public void addHighlightedToken(String... tokenNumbers) {
        Collections.addAll(highlightedTokens, tokenNumbers);
    }

    public boolean empty() {
        return (verseTokenPairGroups == null || verseTokenPairGroups.isEmpty()) &&
                (highlightedTokens == null || highlightedTokens.isEmpty());
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String result = super.toString();
        try {
            result = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // ignore
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = super.equals(obj);
        if (AppUtil.isInstanceOf(QuestionData.class, obj)) {
            result = getId().equals(((QuestionData) obj).getId());
        }
        return result;
    }

    @Override
    public int compareTo(QuestionData o) {
        return getId().compareTo(o.getId());
    }
}
