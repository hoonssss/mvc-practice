package org.example.mvc.view;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//jsp view, redirect view
public interface View {

    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;


}
