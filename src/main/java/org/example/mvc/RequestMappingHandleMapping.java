package org.example.mvc;

import java.util.HashMap;
import java.util.Map;
import org.example.mvc.controller.Controller;
import org.example.mvc.controller.ForwardController;
import org.example.mvc.controller.HomeController;
import org.example.mvc.controller.UserCreateController;
import org.example.mvc.controller.UserListController;

public class RequestMappingHandleMapping implements HandelerMapping {

    /**
     * mpa : /users -> UserController
     */
    private Map<HandlerKey, Controller> mappings = new HashMap<>();

    void init(){
        mappings.put(new HandlerKey(RequestMethod.GET,"/users"),new UserListController());
        mappings.put(new HandlerKey(RequestMethod.POST,"/users"),new UserCreateController());
        mappings.put(new HandlerKey(RequestMethod.GET,"/user/form"),new ForwardController("/user/form"));
    }

    public Controller findHandler(HandlerKey handlerKey){
        return mappings.get(handlerKey);
    }

}
