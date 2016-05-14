package com.jakespringer.reactive.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public abstract class Entity {
	static Map<Long, Stack<Entity>> threadEntityStacks = new HashMap<>();
	
	protected List<Stream<?>> children = new ArrayList<>();
	protected List<ReactiveResponse> responses = new ArrayList<>();
	
	protected void beginEntity() {
		long threadId = Thread.currentThread().getId();
		Stack<Entity> currentStack;
		if (threadEntityStacks.containsKey(threadId)) {
			currentStack = threadEntityStacks.get(threadId);
		} else {
			currentStack = new Stack<>();
			threadEntityStacks.put(threadId, currentStack);
		}
		currentStack.push(this);
	}
	
	protected void endEntity() {
		long threadId = Thread.currentThread().getId();
		if (threadEntityStacks.containsKey(threadId)) {
			threadEntityStacks.get(threadId).pop();
		}
	}
	
	public static <T extends Entity> T construct(T entityObject) {
		entityObject.beginEntity();
		entityObject.onConstruct();
		entityObject.endEntity();
		return entityObject;
	}
	
	public static <T extends Entity> void destroy(T entityObject) {
		entityObject.onDestroy();
		
		for (Stream<?> s : entityObject.children) {
			s.kill();
		}
		
		for (ReactiveResponse r : entityObject.responses) {
			r.event();
		}
	}
	
	protected abstract void onConstruct();
	
	protected abstract void onDestroy();
}
