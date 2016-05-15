package com.jakespringer.reactive.engine;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Cell<T> extends Stream<T> implements Consumer<T>, Supplier<T> {
	private T value;
	
	@Override
	public T get() {
		return value;
	}
	
	public void set(T newValue) {
		value = newValue;
		event();
	}
	
	public Listener eventSet(Cell<T> stream) {
		return () -> Cell.this.set(stream.get());
	}

	@Override
	public void accept(T newValue) {
		set(newValue);
	}
	
	public Stream<T> asStream() {
		return (Stream<T>) this;
	}
}
