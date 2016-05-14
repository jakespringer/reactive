package com.jakespringer.reactive.engine;

public interface Notifier {
	public ReactiveResponse subscribe(Listener listener);
}
