package com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.skin.view;

import com.alphasystem.morphologicalanalysis.graph.model.RelationshipNode;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.control.editor.CommonPropertiesEditor;
import com.alphasystem.morphologicalanalysis.ui.dependencygraph.model.RelationshipNodeAdapter;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author sali
 */
public class RelationshipCommonPropertiesEditorSkinView extends CommonPropertiesEditorSkinView<RelationshipNode, RelationshipNodeAdapter> {

    public RelationshipCommonPropertiesEditorSkinView(CommonPropertiesEditor<RelationshipNode, RelationshipNodeAdapter> control) {
        super(control);
    }

    @Override
    Pair<Integer, Integer> getYSpinnerDefaultMinMaxRange() {
        return getSpinnerMinMaxRange(30, 300);
    }
}
