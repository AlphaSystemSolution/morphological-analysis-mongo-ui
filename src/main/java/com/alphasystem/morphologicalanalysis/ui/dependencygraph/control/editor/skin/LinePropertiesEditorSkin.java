package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin;

import com.alphasystem.morphologicalanalysis.graph.model.LineSupport;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.LinePropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view.LinePropertiesEditorSkinView;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.LineSupportAdapter;
import javafx.scene.control.SkinBase;

/**
 * @author sali
 */
public class LinePropertiesEditorSkin<N extends LineSupport, A extends LineSupportAdapter<N>> extends SkinBase<LinePropertiesEditor<N, A>> {

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public LinePropertiesEditorSkin(LinePropertiesEditor<N, A> control) {
        super(control);
        getChildren().setAll(new LinePropertiesEditorSkinView<>(getSkinnable()));
    }

}
