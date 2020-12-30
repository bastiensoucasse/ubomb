package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.door.Door;

public class Monster extends Character {
    public Monster(final Game game, final Position position, final int lives) {
        super(game, position, Direction.random(), lives);
    }

    public Monster(final Game game, final Position position) {
        this(game, position, 1);
    }

    @Override
    public boolean canMove(final Direction direction) {
        Position position = direction.nextPosition(getPosition());

        if (!position.inside(game.getWorld().getDimension()) || game.getWorld().isThereAMonster(position) || game.getWorld().isThereABomb(position))
            return false;

        Decor decor = game.getWorld().get(position);
        return decor == null || (decor.isWalkable() && !(decor instanceof Door));
    }

    @Override
    public void doMove(final Direction direction) {
        Position position = direction.nextPosition(getPosition());
        setPosition(position);
    }

    public void update(final long now) {
        Direction direction = Direction.random();
        setDirection(direction);

        if (canMove(direction))
            doMove(direction);
    }
}
