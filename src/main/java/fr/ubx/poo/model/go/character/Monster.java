package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject implements Movable {
    private Direction direction;
    private int lives = 1;
    public Monster(Game game, Position position) {
        super(game, position);
        direction = Direction.N;
    }

    @Override
    public boolean canMove(Direction direction) {
        return false;
    }

    @Override
    public void doMove(Direction direction) {

    }

    public Direction getDirection() {
        return direction;
    }
}
