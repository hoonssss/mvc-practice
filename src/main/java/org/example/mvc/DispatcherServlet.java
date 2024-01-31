package org.example.mvc;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.mvc.controller.Controller;
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

    private HandelerMapping hm;

    @Override
    public void init() throws ServletException {
        RequestMappingHandleMapping rmhm = new RequestMappingHandleMapping();
        rmhm.init();

        hm = rmhm;

        handlerAdapters = List.of(new SimpleControllerHandlerAdapter());
            viewResolvers = Collections.singletonList(new JspViewResolver());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        log.info("DispatcherServletService");

        try {
            //key에 맞는 uri가져옴
            Object handler = hm.findHandler(new HandlerKey(RequestMethod.valueOf(request.getMethod()), request.getRequestURI()));
            //Controller handler에 handleRequest 메서드를 호출하여 요청을 처리하고 뷰 이름을 가져옴

            HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(ha -> ha.supports(handler))
                .findFirst()
                .orElseThrow(
                    () -> new ServletException("No adapter for handler [" + handler + "]"));

            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);//실행

            for (ViewResolver viewResolver : viewResolvers) {
                View view = viewResolver.resolveView(modelAndView.getViewName());
                view.render(modelAndView.getModel(), request,response);
            }
        } catch (Exception e) {
            log.error("excption [{}]", e.getMessage());
        }
    }
}
