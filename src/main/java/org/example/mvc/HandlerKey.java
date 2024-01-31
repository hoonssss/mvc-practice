package org.example.mvc;

import java.util.Objects;

public class HandlerKey {

    private final RequestMethod requestmethod;
    private final String url;

    public HandlerKey(RequestMethod requestMethod, String urlPath) {
        this.requestmethod = requestMethod;
        this.url = urlPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandlerKey that = (HandlerKey) o;
        return requestmethod == that.requestmethod && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestmethod, url);
    }
}
