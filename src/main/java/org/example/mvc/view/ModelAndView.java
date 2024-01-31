package org.example.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    public Map<String,Object> model = new HashMap<>();
    private Object view;

    public ModelAndView(String viewName) {
        this.view = viewName;
    }

    public Map<String, Object> getModel(){
        return Collections.unmodifiableMap(model);//불변
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }
}
