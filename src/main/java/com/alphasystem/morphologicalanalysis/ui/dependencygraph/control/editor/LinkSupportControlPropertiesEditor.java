package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.LinkSupportControlPropertiesEditorSkin;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.PropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.CxPropertyAccessor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.util.accessor.GraphNodePropertyAccessors.CyPropertyAccessor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class LinkSupportControlPropertiesEditor<N extends LinkSupport, A extends LinkSupportAdapter<N>>
        extends CommonPropertiesEditor<N, A> {

    private final ObjectProperty<PropertyAccessor<N, A>> cx = new SimpleObjectProperty<>(null, "x", new CxPropertyAccessor<>(null));
    private final ObjectProperty<PropertyAccessor<N, A>> cy = new SimpleObjectProperty<>(null, "y", new CyPropertyAccessor<>(null));

    public LinkSupportControlPropertiesEditor() {
        super();
        setSkin(new LinkSupportControlPropertiesEditorSkin<>(this));
    }

    @Override
    protected void setValues(A node) {
        super.setValues(node);
        setCx(new CxPropertyAccessor<>(node));
        setCy(new CyPropertyAccessor<>(node));
    }

    public final PropertyAccessor<N, A> getCx() {
        return cx.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> cxProperty() {
        return cx;
    }

    public final void setCx(PropertyAccessor<N, A> cx) {
        this.cx.set(cx);
    }

    public final PropertyAccessor<N, A> getCy() {
        return cy.get();
    }

    public final ObjectProperty<PropertyAccessor<N, A>> cyProperty() {
        return cy;
    }

    public final void setCy(PropertyAccessor<N, A> cy) {
        this.cy.set(cy);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LinkSupportControlPropertiesEditorSkin<>(this);
    }
}
