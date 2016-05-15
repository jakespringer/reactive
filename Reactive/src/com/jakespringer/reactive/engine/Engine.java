package com.jakespringer.reactive.engine;

import com.jakespringer.reactive.time.Time;

public class Engine {
    public static final Stream<Double> step = new Cell<>();
    
    private static boolean running = false;
    
    public static void run() {
        running = true;
        long lastTime = System.nanoTime();
        long sysTime = lastTime;
        long deltaTime = 0L;
        while (running) {
            sysTime = System.nanoTime();
            deltaTime = Math.min(sysTime - lastTime, 300000000L);
            lastTime = sysTime;
            
            ((Cell<Double>) step).set((double) deltaTime / (double) Time.NANOSECONDS_PER_SECOND);
            
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        }
    }
}
