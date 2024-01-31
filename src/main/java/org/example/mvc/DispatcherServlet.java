package org.example.mvc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.mvc.view.JspViewResolver;
import org.example.mvc.view.ModelAndView;
import org.example.mvc.view.View;
import org.example.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServlet -> tomcat이 실행 WebServlet -> 모든 경로를 입력하더라도 DispatcherServlet Class 실행
 */
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<ViewResolver> viewResolvers;

    private List<HandlerAdapter> handlerAdapters;

    private List<HandelerMapping> handelerMappings;

    @Override
    public void init() throws ServletException {
        RequestMappingHandleMapping rmhm = new RequestMappingHandleMapping();
        rmhm.init();

        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("org.example");
        ahm.init();
        handelerMappings = List.of(rmhm, ahm);
        handlerAdapters = List.of(new SimpleControllerHandlerAdapter(), new AnnotationHandlerAdapter());
        viewResolvers = Collections.singletonList(new JspViewResolver());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        log.info("DispatcherServletService");
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        try {
            //key에 맞는 uri가져옴
            Object handler = handelerMappings
                .stream()
                .filter(hm -> hm.findHandler(new HandlerKey(requestMethod, requestURI)) != null)
                .map(hm -> hm.findHandler(new HandlerKey(requestMethod, requestURI)))
                .findFirst()
                .orElseThrow(() -> new ServletException("No handler for [" + requestMethod + ", " + requestURI + "]"));

            HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(ha -> ha.supports(handler))
                .findFirst()
                .orElseThrow(
                    () -> new ServletException("No adapter for handler [" + handler + "]"));

            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);//실행

            for (ViewResolver viewResolver : viewResolvers) {
                View view = viewResolver.resolveView(modelAndView.getViewName());
                view.render(modelAndView.getModel(), request, response);
            }
        } catch (Exception e) {
            log.error("excption [{}]", e.getMessage());
        }
    }
}
