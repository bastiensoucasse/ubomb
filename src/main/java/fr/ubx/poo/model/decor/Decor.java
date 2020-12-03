package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;

public abstract class Decor extends Entity {
    private final boolean walkable;

    public Decor() { this(false); }

    public Decor(boolean walkable) {
        this.walkable = walkable;
    }

    @Override
    public String toString() {
        return "Decor";
    }

    public boolean isWalkable() {
        return walkable;
    }
}
