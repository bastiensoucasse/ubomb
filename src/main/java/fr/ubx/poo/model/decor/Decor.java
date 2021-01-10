package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.character.Player;

public abstract class Decor extends Entity {
    private final boolean collectable;
    private boolean walkable;

    public Decor(final boolean collectable, final boolean walkable) {
        this.collectable = collectable;
        this.walkable = walkable;
    }

    public Decor() {
        this(false, false);
    }

    @Override
    public String toString() {
        return "Decor";
    }

    public boolean isCollectable() {
        return collectable;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(final boolean walkable) {
        this.walkable = walkable;
    }

    public boolean canGetThrough() {
        return false;
    }

    public boolean canBeMoved() {
        return false;
    }

    public boolean isOpenable(final Player player) {
        return false;
    }
}
