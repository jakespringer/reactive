package com.jakespringer.reactive.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Stream<T> implements Listener, Notifier {
    private List<ReactiveResponse> toRemove = new ArrayList<>();
    private List<OrderedListener> listeners = new ArrayList<>();
    private List<Stream<?>> children = new ArrayList<>();
    private List<Stream<?>> parents = new ArrayList<>();
    
    public Stream(Notifier... notifiers) {
        for (Notifier n : notifiers) {
            toRemove.add(n.subscribe(this.asListener()));
        }
    }
    
    public Stream(Stream<?>... streams) {
        for (Stream<?> s : streams) {
            s.children.add(this);
            parents.add(s);
        }
    }
    
    int __debugGetNumChildren() {
        return children.size() + listeners.size();
    }
    
    public Listener asListener() {
        return (Listener) this;
    }
    
    public Notifier asNotifier() {
        return (Notifier) this;
    }
    
    public Stream<T> combine(Stream<T> other) {
        Cell<T> cell = new Cell<T>() {
            private int killCount = 0;
            
            @Override
            public void kill() {
                // possible memory leak: when the first parent is killed, there
                // is a still a reference to it in the closure stored in
                // toRemove
                if (++killCount >= 2) super.kill();
            }
        };
        
        toRemove.add(other.send(cell));
        toRemove.add(this.send(cell));
        cell.asStream().parents.add(other);
        cell.asStream().parents.add(this);
        
        long threadId = Thread.currentThread().getId();
        if (Entity.threadEntityStacks.containsKey(threadId)) {
            Stack<Entity> entityStack = Entity.threadEntityStacks.get(threadId);
            if (!entityStack.isEmpty()) {
                entityStack.peek().children.add(cell);
            }
        }
        
        return cell;
    }
    
    public void event() {
        for (Stream<?> s : children) {
            s.event();
        }
        
        for (OrderedListener l : listeners) {
            if (l.last == null) {
                OrderedListener current = l;
                do {
                    current.listener.event();
                } while ((current = current.next) != null);
            }
        }
    }
    
    public Stream<T> filter(Predicate<T> predicate) {
        final Stream<T> thus = this;
        
        Stream<T> stream = new Stream<T>(this) {
            T value;
            
            @Override
            public void event() {
                T toCheck = thus.get();
                
                if (predicate.test(toCheck)) {
                    value = toCheck;
                    super.event();
                }
            }
            
            @Override
            public T get() {
                return value;
            }
        };
        
        long threadId = Thread.currentThread().getId();
        if (Entity.threadEntityStacks.containsKey(threadId)) {
            Stack<Entity> entityStack = Entity.threadEntityStacks.get(threadId);
            if (!entityStack.isEmpty()) {
                entityStack.peek().children.add(stream);
            }
        }
        
        return stream;
    }
    
    public Stream<T> filterElse(Predicate<T> predicate, Consumer<T> callElse) {
        final Stream<T> thus = this;
        
        Stream<T> stream = new Stream<T>(this) {
            T value;
            
            @Override
            public void event() {
                T toCheck = thus.get();
                
                if (predicate.test(toCheck)) {
                    value = toCheck;
                    super.event();
                } else {
                    callElse.accept(toCheck);
                }
            }
            
            @Override
            public T get() {
                return value;
            }
        };
        
        long threadId = Thread.currentThread().getId();
        if (Entity.threadEntityStacks.containsKey(threadId)) {
            Stack<Entity> entityStack = Entity.threadEntityStacks.get(threadId);
            if (!entityStack.isEmpty()) {
                entityStack.peek().children.add(stream);
            }
        }
        
        return stream;
    }
    
    public abstract T get();
    
    public void kill() {
        for (Listener l : toRemove) {
            l.event();
        }
        
        for (Stream<?> s : children) {
            s.kill();
        }
        
        for (Stream<?> p : parents) {
            p.children.remove(this);
        }
        
        toRemove.clear();
        listeners.clear();
        children.clear();
        parents.clear();
    }
    
    public <R> Stream<R> map(Function<T, R> mapping) {
        final Stream<T> thus = this;
        
        Stream<R> stream = new Stream<R>(this) {
            R value;
            
            @Override
            public void event() {
                value = mapping.apply(thus.get());
                super.event();
            }
            
            @Override
            public R get() {
                return value;
            }
        };
        
        long threadId = Thread.currentThread().getId();
        if (Entity.threadEntityStacks.containsKey(threadId)) {
            Stack<Entity> entityStack = Entity.threadEntityStacks.get(threadId);
            if (!entityStack.isEmpty()) {
                entityStack.peek().children.add(stream);
            }
        }
        
        return stream;
    }
    
    protected void registerKillAction(ReactiveResponse whenRemoved) {
        toRemove.add(whenRemoved);
    }
    
    public ReactiveResponse send(Consumer<T> consumer) {
        return subscribe(() -> consumer.accept(get()));
    }
    
    public ReactiveResponse subscribe(Listener listener) {
        OrderedListener orderedListener = new OrderedListener(listener);
        ReactiveResponse rr = new ReactiveResponse(orderedListener) {
            public void event() {
                listeners.remove(orderedListener);
                toRemove.remove(this);
                ordering.listener = null;
                if (ordering.last != null) {
                    ordering.last.next = ordering.next;
                } else if (ordering.next != null) {
                    ordering.next.last = null;
                }
                if (ordering.next != null) {
                    ordering.next.last = ordering.last;
                } else if (ordering.last != null) {
                    ordering.last.next = null;
                }
            }
        };
        
        listeners.add(orderedListener);
        toRemove.add(rr);
        
        long threadId = Thread.currentThread().getId();
        if (Entity.threadEntityStacks.containsKey(threadId)) {
            Stack<Entity> entityStack = Entity.threadEntityStacks.get(threadId);
            if (!entityStack.isEmpty()) {
                entityStack.peek().responses.add(rr);
            }
        }
        
        return rr;
    }
}
