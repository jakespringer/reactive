package com.jakespringer.reactive.engine;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.jakespringer.reactive.util.Wrapper;

public abstract class Stream<T> extends EventStream {
    public Stream(EventStream... updateOn) {
        super(updateOn);
    }
    
    public abstract T get();
    
    public Stream<T> combine(Stream<T> first, @SuppressWarnings("unchecked") Stream<T>... others) {
        // will set the default value to whatever this is, rather than
        // whatever was last sent
        Cell<T> combined = new Cell<>(get());
        first.send(combined::set);
        Arrays.stream(others).forEach(x -> x.send(combined::set));
        return combined;
    }
    
    public <R> Stream<R> map(Function<T, R> function) {
        final Stream<T> thus = this;
        return new Stream<R>(this) {
            @Override
            public R get() {
                return function.apply(thus.get());
            }
        };
    }
    
    public Stream<T> until(Supplier<Boolean> predicate) {
        final Stream<T> thus = this;
        return new Stream<T>(this) {
            @Override
            protected void event() {
                if (predicate.get()) {
                    destroy();
                } else {
                    super.event();
                }
            }

            @Override
            public T get() {
                return thus.get();
            }
        };
    }
    
    public Stream<T> until(EventStream trigger) {
        Wrapper<Removable> removableWrapper = new Wrapper<>();
        Stream<T> streamUntil = distinct();
        Removable removable = trigger.subscribe(() -> {
            streamUntil.destroy();
            removableWrapper.object.remove();
        });
        removableWrapper.object = removable;
        return streamUntil;
    }
    
    public Stream<T> untilNot(Supplier<Boolean> antiPredicate) {
        return until(() -> !antiPredicate.get());
    }
    
    public Removable send(Consumer<T> consumer) {
        return subscribe(() -> consumer.accept(get()));
    }
    
    public Stream<T> distinct() {
        final Stream<T> thus = this;
        return new Stream<T>(this) {
            @Override
            public T get() {
                return thus.get();
            }
        };
    }
}
