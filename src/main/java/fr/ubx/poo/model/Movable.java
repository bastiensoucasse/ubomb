package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;

public interface Movable {
    boolean canMove(final Direction direction);

    void doMove(final Direction direction);
}
