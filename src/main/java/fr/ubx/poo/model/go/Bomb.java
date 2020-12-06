package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Bomb extends GameObject {
    private final int range;

    public Bomb(final Game game, final Position position, final int range) {
        super(game, position);
        this.range = range;
    }

    @Override
    public String toString() {
        return "Bomb";
    }
}
