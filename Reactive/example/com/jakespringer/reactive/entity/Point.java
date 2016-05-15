package com.jakespringer.reactive.entity;

import java.util.Arrays;
import java.util.List;

import com.jakespringer.reactive.engine.Cell;
import com.jakespringer.reactive.engine.Engine;
import com.jakespringer.reactive.engine.Entity;
import com.jakespringer.reactive.engine.Removable;

public class Point extends Entity {    
    @Override
    public List<Removable> construct() {
        return Arrays.asList(
            new Cell<Double>(0.0).setWhen(Engine.step, () -> Math.cos(Engine.step.get())), // x
            new Cell<Double>(0.0).setWhen(Engine.step, () -> Math.sin(Engine.step.get()))  // y
        );
    } 
}
