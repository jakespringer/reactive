package com.jakespringer.reactive.engine;

public class CallStream extends EventStream {
    public CallStream(EventStream...onUpdate) {
        super(onUpdate);
    }
    
    @Override
    public void event() {
        super.event();
    }
}
