package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.spring.support.GraphConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MongoConfig;
import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import com.alphasystem.morphologicalanalysis.spring.support.WordByWordConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static java.lang.Runtime.getRuntime;

/**
 * @author sali
 */
public final class SpringContextHelper {

    private static SpringContextHelper instance;

    static {
        getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                getInstance().getContext().close();
            }
        });
    }

    private final AnnotationConfigApplicationContext context;

    private SpringContextHelper() {
        context = new AnnotationConfigApplicationContext(MongoConfig.class, WordByWordConfig.class, GraphConfig.class,
                MorphologicalAnalysisSpringConfiguration.class);
    }

    public static synchronized SpringContextHelper getInstance() {
        if (instance == null) {
            instance = new SpringContextHelper();
        }
        return instance;
    }

    public AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
