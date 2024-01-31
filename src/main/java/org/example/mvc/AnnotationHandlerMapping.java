package org.example.mvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.example.mvc.annotation.Controller;
import org.example.mvc.annotation.RequestMapping;
import org.reflections.Reflections;

public class AnnotationHandlerMapping implements HandelerMapping{

    private final Object[] basePackage;
    private Map<HandlerKey, AnnotationHandler> handlers = new HashMap<>();
    public AnnotationHandlerMapping(Object... basePackage){
        this.basePackage = basePackage;
    }

    public void init(){
        Reflections reflections = new Reflections(basePackage);
        //homeController
        Set<Class<?>> classesWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class);

        classesWithControllerAnnotation.forEach(
            clazz -> Arrays.stream(clazz.getDeclaredMethods()).forEach(declareMethod -> {
                RequestMapping requestMapping = declareMethod.getDeclaredAnnotation(RequestMapping.class);

                Arrays.stream(getRequestMethods(requestMapping))
                    .forEach(requestMethod -> handlers.put(
                        new HandlerKey(requestMethod, requestMapping.value()), new AnnotationHandler(clazz, declareMethod)
                    ));
            })
        );
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        return requestMapping.method();
    }

    @Override
    public Object findHandler(HandlerKey handlerKey) {
        return handlers.get(handlerKey);
    }
}
