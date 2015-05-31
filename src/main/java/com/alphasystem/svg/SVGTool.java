package com.alphasystem.svg;

import org.w3.svg.ObjectFactory;

/**
 * @author sali
 */
public final class SVGTool {

    public static final ObjectFactory SVG_OBJECT_FACTORY = new ObjectFactory();

    public static final com.alphasystem.morphologicalanalysis.treebank.model.ObjectFactory TREE_BANK_OBJECT_FACTORY =
            new com.alphasystem.morphologicalanalysis.treebank.model.ObjectFactory();

    private static SVGTool instance;

    /**
     * Do not let anyone instantiate this class.
     */
    private SVGTool() {
    }

    public synchronized static SVGTool getInstance() {
        if (instance == null) {
            instance = new SVGTool();
        }
        return instance;
    }


}
