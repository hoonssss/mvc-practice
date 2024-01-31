package org.example.mvc;

public interface HandelerMapping {
    Object findHandler(HandlerKey handlerKey);//어노테이션으로 만들기 위해
}
