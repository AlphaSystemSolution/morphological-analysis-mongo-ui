package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.CxPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.CyPropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author sali
 */
public abstract class LinkSupportPropertiesEditor<N extends LinkSupport, A extends LinkSupportAdapter<N>>
        extends LineSupportPropertiesEditor<N, A> {

    private final ObjectProperty<CxPropertyAccessor<N, A>> cx = new SimpleObjectProperty<>(null, "cx", new CxPropertyAccessor<>(null));
    private final ObjectProperty<CyPropertyAccessor<N, A>> cy = new SimpleObjectProperty<>(null, "cy", new CyPropertyAccessor<>(null));

    LinkSupportPropertiesEditor(A node) {
        super(node);
    }

    @Override
    protected void setValues(A node) {
        super.setValues(node);
        setCx(new CxPropertyAccessor<>(node));
        setCy(new CyPropertyAccessor<>(node));
    }

    public final CxPropertyAccessor<N, A> getCx() {
        return cx.get();
    }

    public final ObjectProperty<CxPropertyAccessor<N, A>> cxProperty() {
        return cx;
    }

    public final void setCx(CxPropertyAccessor<N, A> cx) {
        this.cx.set(cx);
    }

    public final CyPropertyAccessor<N, A> getCy() {
        return cy.get();
    }

    public final ObjectProperty<CyPropertyAccessor<N, A>> cyProperty() {
        return cy;
    }

    public void setCy(CyPropertyAccessor<N, A> cy) {
        this.cy.set(cy);
    }
}
