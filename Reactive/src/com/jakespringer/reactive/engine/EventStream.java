package com.jakespringer.reactive.engine;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.jakespringer.reactive.util.Wrapper;

public class EventStream extends GCTreeNode {
    public EventStream(EventStream... updateOn) {
        Arrays.stream(updateOn).forEach(x -> x.addChild(this));
    }
    
    @Override
    protected void onDestruct() {
    }
    
    @Override
    protected void event() {
        for (GCTreeNode gc : children) {
            gc.event();
        }
    }
    
    public Removable subscribe(Runnable terminal) {
        children.add(new Terminal(terminal));
        return () -> children.remove(terminal);
    }
    
    public <R> Stream<R> reduce(R initial, UnaryOperator<R> function) {
        Cell<R> reduction = new Cell<>(initial);
        reduction.editWhen(this, function);
        return reduction;
    }
    
    public EventStream until(Supplier<Boolean> predicate) {
        return new EventStream(this) {
            @Override
            protected void event() {
                if (predicate.get()) {
                    destroy();
                } else {
                    super.event();
                }
            }
        };
    }
    
    public EventStream until(EventStream trigger) {
        Wrapper<Removable> removableWrapper = new Wrapper<>();
        EventStream streamUntil = distinct();
        Removable removable = trigger.subscribe(() -> {
            streamUntil.destroy();
            removableWrapper.object.remove();
        });
        removableWrapper.object = removable;
        return streamUntil;
    }

    public EventStream untilNot(Supplier<Boolean> antiPredicate) {
        return until(() -> !antiPredicate.get());
    }
    
    public EventStream filter(Supplier<Boolean> predicate) {
        CallStream stream = new CallStream();
        subscribe(() -> {
            if (predicate.get()) {
                stream.event();
            }
        });
        return stream;
    }
    
    public EventStream filterNot(Supplier<Boolean> antiPredicate) {
        return filter(() -> !antiPredicate.get());
    }
        
    public void destroy() {
        removeMe();
    }
    
    public EventStream distinct() {
        return new EventStream(this);
    }
}
