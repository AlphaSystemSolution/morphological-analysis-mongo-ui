package com.alphasystem.access.builder;

import com.alphasystem.access.model.TokenAdapter;
import com.alphasystem.openxml.builder.wml.PBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.util.IdGenerator.nextId;

/**
 * @author sali
 */
@Component
public class ListParagraphBuilder extends Builder {

    @Override
    public Object[] build(List<List<TokenAdapter>> lists) {
        List<P> list = new ArrayList<>();

        lists.forEach(tokenAdapters -> list.add(buildParagraph(tokenAdapters)));
        // list.add(WmlAdapter.getEmptyPara());

        return list.toArray(new P[list.size()]);
    }

    private P buildParagraph(List<TokenAdapter> tokenAdapters) {
        String id = nextId();
        final PPr pPr = WmlBuilderFactory.getPPrBuilder().withPStyle("ArabicListParagraph").getObject();
        final PBuilder pBuilder = WmlBuilderFactory.getPBuilder().withPPr(pPr).withRsidP(id).withRsidR(id).withRsidRDefault(id);
        pBuilder.addContent(buildParagraphRuns(tokenAdapters));
        return pBuilder.getObject();
    }

}
