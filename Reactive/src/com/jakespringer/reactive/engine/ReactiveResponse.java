package com.jakespringer.reactive.engine;

public abstract class ReactiveResponse implements Listener {
	protected OrderedListener ordering;
	
	public ReactiveResponse() {
	}
	
	// restrict construction
	ReactiveResponse(OrderedListener ordering) {
		this.ordering = ordering;
	}
	
	public ReactiveResponse then(ReactiveResponse response) {
		ordering.next = response.ordering;
		response.ordering.last = ordering;
		return response;
	}
}
