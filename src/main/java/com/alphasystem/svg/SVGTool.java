package com.alphasystem.svg;

import com.alphasystem.morphologicalanalysis.treebank.model.Line;
import com.alphasystem.morphologicalanalysis.treebank.model.Point;
import com.alphasystem.morphologicalanalysis.treebank.model.TreeBankData;
import com.alphasystem.morphologicalanalysis.treebank.model.TreeBankMetaData;
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

    public Point createPoint(double x, double y) {
        return TREE_BANK_OBJECT_FACTORY.createPoint().withCx(x).withCy(y);
    }

    public Line createLine(double x1, double y1, double x2, double y2) {
        return TREE_BANK_OBJECT_FACTORY.createLine().withPoint1(createPoint(x1, y1))
                .withPoint2(createPoint(x2, y2));
    }

    public TreeBankData createTreeBankData() {
        TreeBankMetaData treeBankMetaData = TREE_BANK_OBJECT_FACTORY.createTreeBankMetaData().withShowOutline(true)
                .withShowGridLines(true).withDebugMode(true).withHeight(600);
        return TREE_BANK_OBJECT_FACTORY.createTreeBankData()
                .withMetaData(treeBankMetaData);
    }

}
