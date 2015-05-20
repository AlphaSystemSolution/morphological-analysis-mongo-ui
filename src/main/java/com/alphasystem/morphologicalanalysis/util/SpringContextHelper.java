package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.spring.support.MorphologicalAnalysisSpringConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static java.lang.Runtime.getRuntime;

/**
 * @author sali
 */
public final class SpringContextHelper {

    private static SpringContextHelper instance;

    static {
        getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {
                getInstance().getContext().close();
            }
        });
    }

    public static synchronized SpringContextHelper getInstance() {
        if(instance == null){
            instance = new SpringContextHelper();
        }
        return instance;
    }

    private final AnnotationConfigApplicationContext context;

    private SpringContextHelper(){
        context = new AnnotationConfigApplicationContext(
                MorphologicalAnalysisSpringConfiguration.class);
    }

    public AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
