package com.jakespringer.reactive.engine;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Cell<T> extends Stream<T> {
    public T value;
    
    public Cell(T initial, EventStream...updateOn) {
        super(updateOn);
        value = initial;
    }
    
    @Override
    public T get() {
        return value;
    }
    
    public void set(T newValue) {
        value = newValue;
    }
    
    public void edit(UnaryOperator<T> function) {
        value = function.apply(value);
    }
    
    public Removable setWhen(EventStream when, T newValue) {
        return when.subscribe(() -> set(newValue));
    }
    
    public Removable setWhen(EventStream when, Supplier<T> newValueSupplier) {
        return when.subscribe(() -> set(newValueSupplier.get()));
    }
    
    public Removable setWhen(EventStream when, Stream<T> newValueStream) {
        return when.subscribe(() -> set(newValueStream.get()));
    }
    
    public Removable editWhen(EventStream when, UnaryOperator<T> function) {
        return when.subscribe(() -> edit(function));
    }
}
