package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;

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

        if (!position.inside(game.getWorld().getDimension()))
            return false;
        if(isThereABomb())
            return false;

        Decor decor = game.getWorld().get(position);
        return decor == null || decor.isWalkable();
    }

    @Override
    public void doMove(final Direction direction) {
        Position position = direction.nextPosition(getPosition());
        setPosition(position);
    }

    public void update(final long now) {
        if (now % 60 != 0)
            return;
        
        Direction direction = Direction.random();
        setDirection(direction);

        if (canMove(direction))
            doMove(direction);
    }
}
