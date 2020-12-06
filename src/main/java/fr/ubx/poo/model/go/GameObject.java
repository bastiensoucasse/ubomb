package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.Entity;

public abstract class GameObject extends Entity {
    protected final Game game;
    private Position position;

    public GameObject(final Game game, final Position position) {
        this.game = game;
        this.position = position;
    }

    @Override
    public String toString() {
        return "GameObject";
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }
}
