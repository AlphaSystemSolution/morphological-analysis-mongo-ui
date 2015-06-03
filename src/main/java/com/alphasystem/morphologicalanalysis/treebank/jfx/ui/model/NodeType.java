package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

/**
 * @author sali
 */
public enum NodeType {

    /**
     * A node that will always has part of speech tag.
     */
    TERMINAL,

    /**
     * Represents part of speech.
     */
    PART_OF_SPEECH,

    /**
     * Represents two or more terminal nodes to make a phrase.
     */
    PHRASE,

    /**
     * Represents relationship between two nodes.
     */
    RELATIONSHIP,

    /**
     * Represents a node from outside of current set of nodes.
     */
    REFERENCE,

    /**
     * A implicit word with part of speech and Arabic text to fill grammatical meaning / relationship.
     */
    HIDDEN,

    /**
     * Hidden node without any Arabic text and only part of speech to fill grammatical meaning / relationship.
     */
    EMPTY,

    /**
     * Represents the root of each of above category in the tree. This is not used in actual graph.
     */
    ROOT,

    /**
     * Represents the group of nodes.
     */
    GROUP
}
