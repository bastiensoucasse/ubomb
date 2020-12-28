package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public abstract class Character extends GameObject implements Movable {
    private Direction direction;
    private int lives;

    public Character(final Game game, final Position position, final Direction direction, final int lives) {
        super(game, position);
        this.direction = direction;
        this.lives = lives;
    }

    @Override
    public String toString() {
        return "Character";
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    public int getLives() {
        return lives;
    }

    public void addLife() {
        lives++;
    }

    public void removeLife() {
        System.out.println("Monster attack: " + getLives() + " lives remaining.");
        lives--;
    }

    public boolean isAlive() {
        return lives > 0;
    }
}
