package com.jakespringer.reactive.engine;

public class Terminal extends GCTreeNode {
    private Runnable runnable;
    
    public Terminal(Runnable run) {
        runnable = run;
    }
    
    @Override
    public void event() {
        runnable.run();
    }
    
    @Override
    protected void onDestruct() {
    }
}
