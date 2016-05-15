package com.jakespringer.reactive.engine;

import com.jakespringer.reactive.time.Time;
import com.jakespringer.reactive.util.Util;

public class Engine {
    private static long startTime = System.nanoTime();
    
    public static final Stream<Double> time = new Stream<Double>() {
        @Override
        public Double get() {
            return (double)(System.nanoTime() - startTime) / (double) Time.NANOSECONDS_PER_SECOND;
        }
    };
    
    public static final Stream<Double> step = Util.differentiate(time);
}
