package com.jackson.threadLocal;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RequestHolder {

    private final static ThreadLocal<Object> requestHolder = new ThreadLocal<>();
    public static void add(Object object) {
        requestHolder.set(object);
    }

    public static Object getId() {
        return requestHolder.get();
    }
//
//    public static void remove() {
//        requestHolder.remove();
//    }
}
