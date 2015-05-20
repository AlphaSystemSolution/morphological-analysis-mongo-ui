package com.alphasystem.morphologicalanalysis.treebank.ui.support;

import com.alphasystem.morphologicalanalysis.treebank.model.Relationship;
import com.alphasystem.morphologicalanalysis.treebank.model.Text;

import static com.alphasystem.util.AppUtil.isGivenClass;

/**
 * @author sali
 */
public class TreeBankUserObject {

    private Object userObject;

    public TreeBankUserObject(Text text) {
        this.userObject = text;
    }

    public TreeBankUserObject(Relationship relationship) {
        this.userObject = relationship;
    }

    public Object getUserObject() {
        return userObject;
    }

    @Override
    public String toString() {
        String result = super.toString();
        if (isGivenClass(Text.class, userObject)) {
            result = ((Text) userObject).getValue();
        } else if (isGivenClass(Relationship.class, userObject)) {
            Relationship relationship = (Relationship) userObject;
            result = new TreeBankUserObject(relationship.getText()).toString();
        }
        return result;
    }
}
