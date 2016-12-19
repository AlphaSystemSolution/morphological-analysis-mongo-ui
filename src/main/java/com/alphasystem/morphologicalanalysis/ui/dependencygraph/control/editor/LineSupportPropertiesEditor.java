package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.LineSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LineSupportAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.X1PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.X2PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.Y1PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.Y2PropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public abstract class LineSupportPropertiesEditor<N extends LineSupport, A extends LineSupportAdapter<N>> extends PropertiesEditor<N, A> {

    private final ObjectProperty<X1PropertyAccessor<N, A>> x1 = new SimpleObjectProperty<>(null, "x1", new X1PropertyAccessor<>(null));
    private final ObjectProperty<Y1PropertyAccessor<N, A>> y1 = new SimpleObjectProperty<>(null, "y1", new Y1PropertyAccessor<>(null));
    private final ObjectProperty<X2PropertyAccessor<N, A>> x2 = new SimpleObjectProperty<>(null, "x2", new X2PropertyAccessor<>(null));
    private final ObjectProperty<Y2PropertyAccessor<N, A>> y2 = new SimpleObjectProperty<>(null, "y2", new Y2PropertyAccessor<>(null));

    @Override
    protected void updateBounds(A node) {
        if (node == null) {
            return;
        }
        setLowerXBound(node.getX1());
        setUpperXBound(node.getX2());
    }

    @Override
    protected void setValues(A node) {
        super.setValues(node);
        setX1(new X1PropertyAccessor<>(node));
        setY1(new Y1PropertyAccessor<>(node));
        setX2(new X2PropertyAccessor<>(node));
        setY2(new Y2PropertyAccessor<>(node));
    }

    public final X1PropertyAccessor<N, A> getX1() {
        return x1.get();
    }

    public final ObjectProperty<X1PropertyAccessor<N, A>> x1Property() {
        return x1;
    }

    public final void setX1(X1PropertyAccessor<N, A> x1) {
        this.x1.set(x1);
    }

    public final Y1PropertyAccessor<N, A> getY1() {
        return y1.get();
    }

    public final ObjectProperty<Y1PropertyAccessor<N, A>> y1Property() {
        return y1;
    }

    public final void setY1(Y1PropertyAccessor<N, A> y1) {
        this.y1.set(y1);
    }

    public final X2PropertyAccessor<N, A> getX2() {
        return x2.get();
    }

    public final ObjectProperty<X2PropertyAccessor<N, A>> x2Property() {
        return x2;
    }

    public final void setX2(X2PropertyAccessor<N, A> x2) {
        this.x2.set(x2);
    }

    public final Y2PropertyAccessor<N, A> getY2() {
        return y2.get();
    }

    public final ObjectProperty<Y2PropertyAccessor<N, A>> y2Property() {
        return y2;
    }

    public final void setY2(Y2PropertyAccessor<N, A> y2) {
        this.y2.set(y2);
    }
}
