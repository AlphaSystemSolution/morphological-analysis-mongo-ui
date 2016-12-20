package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.GraphNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.GraphNodeAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.XPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.YPropertyAccessor;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.text.Font;

/**
 * @author sali
 */
public abstract class PropertiesEditor<N extends GraphNode, A extends GraphNodeAdapter<N>> extends Control {

    private final ObjectProperty<A> node = new SimpleObjectProperty<>(null, "node");
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty(null, "canvasWidth", 0);
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty(null, "canvasHeight", 0);
    private final DoubleProperty lowerXBound = new SimpleDoubleProperty(null, "lowerXBound", -1);
    private final DoubleProperty upperXBound = new SimpleDoubleProperty(null, "upperXBound", 0);
    private final DoubleProperty lowerYBound = new SimpleDoubleProperty(null, "lowerYBound", -1);
    private final DoubleProperty upperYBound = new SimpleDoubleProperty(null, "upperYBound", 0);
    private final StringProperty text = new SimpleStringProperty(null, "text");
    private final ObjectProperty<XPropertyAccessor<N, A>> x = new SimpleObjectProperty<>(null, "x", new XPropertyAccessor<>(null));
    private final ObjectProperty<YPropertyAccessor<N, A>> y = new SimpleObjectProperty<>(null, "y", new YPropertyAccessor<>(null));
    private final ObjectProperty<Font> arabicFont = new SimpleObjectProperty<>(null, "arabicFont");

    PropertiesEditor(A node) {
        nodeProperty().addListener((observable, oldValue, newValue) -> setValues(newValue));
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && getNode() != null) {
                getNode().setText(newValue);
            }
        });
        arabicFontProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || getNode() == null) {
                return;
            }
            getNode().setFont(newValue);
        });
        setNode(node);
    }

    protected void updateBounds(A node) {
        setLowerYBound(0);
        setUpperYBound(getCanvasHeight() / 3);
    }

    protected void setValues(A node) {
        updateBounds(node);
        setText((node == null) ? "" : node.getText());
        if (node != null) {
            System.out.println(String.format("<<<<< %s:%s:%s:%s >>>>>", node.getGraphNodeType(), node.getId(), node.getX(), node.getY()));
        }
        setX(new XPropertyAccessor<>(node));
        setY(new YPropertyAccessor<>(node));
        setArabicFont((node == null) ? null : node.getFont());
    }

    public final A getNode() {
        return node.get();
    }

    public final void setNode(A node) {
        this.node.set(node);
    }

    public final ObjectProperty<A> nodeProperty() {
        return node;
    }

    public final double getCanvasWidth() {
        return canvasWidth.get();
    }

    public final DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }

    public final void setCanvasWidth(double canvasWidth) {
        this.canvasWidth.set(canvasWidth);
    }

    public final double getCanvasHeight() {
        return canvasHeight.get();
    }

    public final DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    public final void setCanvasHeight(double canvasHeight) {
        this.canvasHeight.set(canvasHeight);
    }

    public final double getLowerXBound() {
        return lowerXBound.get();
    }

    public final DoubleProperty lowerXBoundProperty() {
        return lowerXBound;
    }

    public final void setLowerXBound(double lowerXBound) {
        this.lowerXBound.set(lowerXBound);
    }

    public final double getUpperXBound() {
        return upperXBound.get();
    }

    public final DoubleProperty upperXBoundProperty() {
        return upperXBound;
    }

    public final void setUpperXBound(double upperXBound) {
        this.upperXBound.set(upperXBound);
    }

    public final double getLowerYBound() {
        return lowerYBound.get();
    }

    public final DoubleProperty lowerYBoundProperty() {
        return lowerYBound;
    }

    public final void setLowerYBound(double lowerYBound) {
        this.lowerYBound.set(lowerYBound);
    }

    public final double getUpperYBound() {
        return upperYBound.get();
    }

    public final DoubleProperty upperYBoundProperty() {
        return upperYBound;
    }

    public final void setUpperYBound(double upperYBound) {
        this.upperYBound.set(upperYBound);
    }

    public final String getText() {
        return text.get();
    }

    public final StringProperty textProperty() {
        return text;
    }

    public final void setText(String text) {
        this.text.set(text);
    }

    public final XPropertyAccessor<N, A> getX() {
        return x.get();
    }

    public final ObjectProperty<XPropertyAccessor<N, A>> xProperty() {
        return x;
    }

    public final void setX(XPropertyAccessor<N, A> x) {
        this.x.set(x);
    }

    public final YPropertyAccessor<N, A> getY() {
        return y.get();
    }

    public final ObjectProperty<YPropertyAccessor<N, A>> yProperty() {
        return y;
    }

    public final void setY(YPropertyAccessor<N, A> y) {
        this.y.set(y);
    }

    public final Font getArabicFont() {
        return arabicFont.get();
    }

    public final ObjectProperty<Font> arabicFontProperty() {
        return arabicFont;
    }

    public final void setArabicFont(Font arabicFont) {
        this.arabicFont.set(arabicFont);
    }
}
