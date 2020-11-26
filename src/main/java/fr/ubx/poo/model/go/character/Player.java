/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import javafx.geometry.Pos;

import java.util.Iterator;
import java.util.Map;

import static fr.ubx.poo.game.WorldEntity.Key;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner;
    private int keys = 0;
    private int nb_bombs = 0;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (!nextPos.inside(game.getWorld().dimension))
            return false;

        Decor d = game.getWorld().get(nextPos);
        return d == null || d instanceof Bonus || d instanceof Key || d instanceof Princess;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {
        if (moveRequested && canMove(direction)) {
            doMove(direction);

            if (game.getWorld().get(getPosition()) instanceof Princess)
                winner = true;

            if (game.getWorld().isThereAMonster(getPosition()))
                lives--;
            if(game.getWorld().get(getPosition()) instanceof Key){
                this.keys++;
            }
            if(game.getWorld().get(getPosition()) instanceof BonusBombNbInc){
                this.nb_bombs++;
            }
            if(game.getWorld().get(getPosition()) instanceof BonusBombNbDec){
                if(nb_bombs > 0)
                    this.nb_bombs--;
            }
            if (lives == 0)
                alive = false;
        }
        moveRequested = false;
    }

    public int getKeys(){return keys;}

    public int getBombs(){return nb_bombs;}

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

}
