package com.jakespringer.reactive.util;

import com.jakespringer.reactive.engine.Stream;

public class Util {
    public static Stream<Double> integrate(Stream<Double> function) {
        Wrapper<Double> lastValue = new Wrapper<>(0.0);
        return function.map(x -> {
            lastValue.object += x;
            return lastValue.object;
        });
    }
    
    public static Stream<Double> differentiate(Stream<Double> function) {
        Wrapper<Double> lastValue = new Wrapper<>(function.get());
        return function.map(x -> {
            double derivative = x - lastValue.object;
            lastValue.object = x;
            return derivative;
        });
    }
}
