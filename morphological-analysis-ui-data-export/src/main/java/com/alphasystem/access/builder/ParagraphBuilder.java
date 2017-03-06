package com.alphasystem.access.builder;

import com.alphasystem.access.model.TokenAdapter;
import com.alphasystem.openxml.builder.wml.PBuilder;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import org.docx4j.wml.PPr;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.alphasystem.util.IdGenerator.nextId;

/**
 * @author sali
 */
@Component
public class ParagraphBuilder extends Builder {

    @Override
    public Object[] build(List<List<TokenAdapter>> lists) {
        String id = nextId();
        final PPr pPr = WmlBuilderFactory.getPPrBuilder().withPStyle("ArabicNormal").getObject();
        final PBuilder pBuilder = WmlBuilderFactory.getPBuilder().withPPr(pPr).withRsidP(id).withRsidR(id).withRsidRDefault(id);
        lists.forEach(tokens -> pBuilder.addContent(buildParagraphRuns(tokens)));

        Object[] result = new Object[2];
        result[0] = pBuilder.getObject();
        result[1] = WmlAdapter.getEmptyPara();
        return result;
    }

}
