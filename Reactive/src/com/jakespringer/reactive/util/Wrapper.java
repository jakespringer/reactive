package com.jakespringer.reactive.util;

public class Wrapper<T> {
    public T object;
    
    public Wrapper() {
        object = null;
    }
    
    public Wrapper(T obj) {
        object = obj;
    }
}
