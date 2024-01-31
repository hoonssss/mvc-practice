package org.example.mvc.view;

//view name을 받아서 결정
public interface ViewResolver {

    View resolveView(String name);

}
