package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Monster extends Character {
    public Monster(final Game game, final Position position, final int lives) {
        super(game, position, Direction.random(), lives);
    }

    public Monster(final Game game, final Position position) {
        this(game, position, 1);
    }

    @Override
    public boolean canMove(final Direction direction) {
        return false;
    }

    @Override
    public void doMove(final Direction direction) {
    }
}
