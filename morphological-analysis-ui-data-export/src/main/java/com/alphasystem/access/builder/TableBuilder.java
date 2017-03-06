package com.alphasystem.access.builder;

import com.alphasystem.access.model.TokenAdapter;
import com.alphasystem.openxml.builder.wml.PPrBuilder;
import com.alphasystem.openxml.builder.wml.TableAdapter;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STVerticalJc;
import org.docx4j.wml.TcPr;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sali
 */
@Component
public class TableBuilder extends Builder {

    @Override
    public Object[] build(List<List<TokenAdapter>> lists) {
        TableAdapter tableAdapter = new TableAdapter(20.0, 11.0, 11.0, 11.0, 8.0, 7.0, 15.0, 11.0, 6.0).startTable("TableGrid1");
        tableAdapter.startRow().addColumn(0, WmlAdapter.getParagraph("Meaning")).addColumn(1, WmlAdapter.getParagraph("Masdr"))
                .addColumn(2, WmlAdapter.getParagraph("Present")).addColumn(3, WmlAdapter.getParagraph("Past"))
                .addColumn(4, WmlAdapter.getParagraph("Family")).addColumn(5, WmlAdapter.getParagraph("Root"))
                .addColumn(6, WmlAdapter.getParagraph("Kind Of Word")).addColumn(7, WmlAdapter.getParagraph("Word"))
                .addColumn(8, WmlAdapter.getParagraph("#")).endRow();


        int index = 1;
        for (List<TokenAdapter> tokenAdapters : lists) {
            for (TokenAdapter tokenAdapter : tokenAdapters) {
                if (tokenAdapter.isHighlight()) {
                    tableAdapter.startRow().addColumn(0, null, getTcPr(), getParagraph("Translation", ""))
                            .addColumn(1, null, getTcPr(), getParagraph("ArabicNormal", ""))
                            .addColumn(2, null, getTcPr(), getParagraph("ArabicNormal", ""))
                            .addColumn(3, null, getTcPr(), getParagraph("ArabicNormal", ""))
                            .addColumn(4, null, getTcPr(), getParagraph("Translation", ""))
                            .addColumn(5, null, getTcPr(), getParagraph("ArabicNormal", ""))
                            .addColumn(6, null, getTcPr(), getParagraph("ArabicNormal", ""))
                            .addColumn(7, null, getTcPr(), getParagraph("ArabicNormal", tokenAdapter.getToken().tokenWord().toUnicode()))
                            .addColumn(6, null, getTcPr(), getParagraph("Translation", String.valueOf(index)))
                            .endRow();
                    index++;
                }
            }
        }


        Object[] results = new Object[2];
        results[0] = tableAdapter.getTable();
        results[1] = WmlAdapter.getEmptyPara();
        return results;
    }

    private TcPr getTcPr() {
        return WmlBuilderFactory.getTcPrBuilder().withVAlign(STVerticalJc.CENTER).getObject();
    }

    private P getParagraph(String style, String text) {
        final PPrBuilder pPrBuilder = WmlBuilderFactory.getPPrBuilder().withPStyle(style).withJc(JcEnumeration.CENTER);
        final R run = WmlBuilderFactory.getRBuilder().addContent(WmlAdapter.getText(text, "preserve")).getObject();
        return WmlBuilderFactory.getPBuilder().withPPr(pPrBuilder.getObject()).addContent(run).getObject();
    }
}
