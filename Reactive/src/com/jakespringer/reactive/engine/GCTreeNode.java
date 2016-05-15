package com.jakespringer.reactive.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class GCTreeNode implements Removable {
    protected List<GCTreeNode> parents = new ArrayList<>();
    protected List<GCTreeNode> children = new ArrayList<>();
    
    protected void removeChildren() {
        children.forEach(x -> x.removeMeFrom(GCTreeNode.this));
        children.clear();
    }
    
    protected void detachParents() {
        parents.forEach(x -> x.children.remove(GCTreeNode.this));
        parents.clear();
    }
    
    protected void removeMe() {
        removeChildren();
        detachParents();
    }
    
    protected void removeMeFrom(GCTreeNode parent) {
        parents.remove(parent);
        parent.children.remove(this);
        if (parents.isEmpty()) {
            removeMe();
        }
    }
    
    protected void addParent(GCTreeNode node) {
        if (!parents.contains(node)) {
            parents.add(node);
        }
        if (!node.children.contains(this)) {
            node.children.add(this);
        }
    }
    
    protected void addChild(GCTreeNode node) {
        if (!node.parents.contains(this)) {
            node.parents.add(this);
        }
        if (!children.contains(node)) {
            children.add(node);
        }
    }
    
    @Override
    public void remove() {
        removeMe();
    }
    
    abstract void onDestruct();
    
    abstract void event();
}
