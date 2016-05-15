package com.jakespringer.reactive.engine;

class OrderedListener {
    Listener listener;
    OrderedListener next;
    OrderedListener last;
    
    OrderedListener(Listener listener) {
        this.listener = listener;
    }
    
    OrderedListener(Listener listener, OrderedListener last, OrderedListener next) {
        this.listener = listener;
        this.last = last;
        this.next = next;
    }
}
