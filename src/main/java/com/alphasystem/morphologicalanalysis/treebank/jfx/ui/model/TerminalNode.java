package com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model;

import com.alphasystem.morphologicalanalysis.model.Token;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import static com.alphasystem.morphologicalanalysis.treebank.jfx.ui.model.NodeType.TERMINAL;

/**
 * @author sali
 */
public class TerminalNode extends LineSupport {

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
        super(nodeType, id, text, x, y, x1, y1, x2, y2);
        this.x3 = new SimpleDoubleProperty(x3);
        this.y3 = new SimpleDoubleProperty(y3);
    }

    private static String getTokenValue(Token token) {
        return (token == null) ? null : token.getTokenWord().toUnicode();
    }

    public final double getX3() {
        return x3.get();
    }

    public final void setX3(double x3) {
        this.x3.set(x3);
    }

    public final DoubleProperty x3Property() {
        return x3;
    }

    public final double getY3() {
        return y3.get();
    }

    public final void setY3(double y3) {
        this.y3.set(y3);
    }

    public final DoubleProperty y3Property() {
        return y3;
    }

    public Token getToken() {
        return token;
    }

}
