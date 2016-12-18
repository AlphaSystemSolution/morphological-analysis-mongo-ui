package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.graph.model.LinkSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.LinkSupportControlPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.LinkSupportControlPropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LinkSupportAdapter;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class LinkSupportControlPropertiesEditorSkin<N extends LinkSupport, A extends LinkSupportAdapter<N>>
        extends SkinBase<LinkSupportControlPropertiesEditor<N, A>> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public LinkSupportControlPropertiesEditorSkin(LinkSupportControlPropertiesEditor<N, A> control) {
        super(control);
        getChildren().setAll(new LinkSupportControlPropertiesEditorSkinView<>(control));
    }
}
