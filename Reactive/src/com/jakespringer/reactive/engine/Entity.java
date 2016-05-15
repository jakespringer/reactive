package com.jakespringer.reactive.engine;

import java.util.List;

public abstract class Entity {
    public final EventStream destroyed = new CallStream();
    protected List<Removable> toDestroy;
    
    public abstract List<Removable> construct();
    
    public static <T extends Entity> void destroy(T entity) {
        ((CallStream) entity.destroyed).event();
        if (entity.toDestroy != null) {
            entity.toDestroy.forEach(x -> x.remove());
            entity.toDestroy.clear();
        }
    }
    
    public static <T extends Entity> T create(T entity) {
        entity.toDestroy = entity.construct();
        return entity;
    }
}
