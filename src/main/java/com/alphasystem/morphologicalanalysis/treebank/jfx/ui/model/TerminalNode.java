package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.morphologicalanalysis.model.Token;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.TERMINAL;

/**
 * @author sali
 */
public class TerminalNode extends GraphNode {

    /**
     * x1 location for line
     */
    protected final DoubleProperty x1;

    /**
     * y1 location for line
     */
    protected final DoubleProperty y1;

    /**
     * x2 location for line
     */
    protected final DoubleProperty x2;

    /**
     * y2 location for line
     */
    protected final DoubleProperty y2;

    /**
     * x location for translation
     */
    protected final DoubleProperty x3;

    /**
     * y location for translation
     */
    protected final DoubleProperty y3;

    protected Token token;

    /**
     *
     */
    public TerminalNode() {
        this(null, null, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0);
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public TerminalNode(Token token, String id, Double x, Double y, Double x1, Double y1, Double x2,
                        Double y2, Double x3, Double y3) {
        this(TERMINAL, token, id, x, y, x1, y1, x2, y2, x3, y3);
    }

    /**
     * @param nodeType
     * @param token
     * @param id
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    protected TerminalNode(NodeType nodeType, Token token, String id, Double x, Double y,
                           Double x1, Double y1, Double x2, Double y2, Double x3, Double y3) {
        this(nodeType, id, getTokenValue(token), x, y, x1, y1, x2, y2, x3, y3);
        this.token = token;
    }

    /**
     * @param nodeType
     * @param id
     * @param text
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    protected TerminalNode(NodeType nodeType, String id, String text, Double x, Double y,
                           Double x1, Double y1, Double x2, Double y2, Double x3, Double y3) {
        super(nodeType, id, text, x, y);
        this.x1 = new SimpleDoubleProperty(x1);
        this.y1 = new SimpleDoubleProperty(y1);
        this.x2 = new SimpleDoubleProperty(x2);
        this.y2 = new SimpleDoubleProperty(y2);
        this.x3 = new SimpleDoubleProperty(x3);
        this.y3 = new SimpleDoubleProperty(y3);
    }

    private static String getTokenValue(Token token) {
        return (token == null) ? null : token.getTokenWord().toUnicode();
    }

    public double getX1() {
        return x1.get();
    }

    public void setX1(double x1) {
        this.x1.set(x1);
    }

    public final DoubleProperty x1Property() {
        return x1;
    }

    public double getX2() {
        return x2.get();
    }

    public void setX2(double x2) {
        this.x2.set(x2);
    }

    public final DoubleProperty x2Property() {
        return x2;
    }

    public double getY1() {
        return y1.get();
    }

    public void setY1(double y1) {
        this.y1.set(y1);
    }

    public final DoubleProperty y1Property() {
        return y1;
    }

    public double getY2() {
        return y2.get();
    }

    public void setY2(double y2) {
        this.y2.set(y2);
    }

    public final DoubleProperty y2Property() {
        return y2;
    }

    public final double getX3() {
        return x3.get();
    }

    public final DoubleProperty x3Property() {
        return x3;
    }

    public final void setX3(double x3) {
        this.x3.set(x3);
    }

    public final double getY3() {
        return y3.get();
    }

    public final DoubleProperty y3Property() {
        return y3;
    }

    public final void setY3(double y3) {
        this.y3.set(y3);
    }

    public Token getToken() {
        return token;
    }

}
