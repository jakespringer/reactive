package com.jakespringer.reactive.engine;

import java.util.function.Consumer;

public class OrderedConsumer<T> {
    Consumer<T> consumer;
    OrderedConsumer<T> next;
    OrderedConsumer<T> last;
    
    OrderedConsumer(Consumer<T> consumer) {
        this.consumer = consumer;
    }
    
    OrderedConsumer(Consumer<T> consumer, OrderedConsumer<T> last, OrderedConsumer<T> next) {
        this.consumer = consumer;
        this.last = last;
        this.next = next;
    }
}
