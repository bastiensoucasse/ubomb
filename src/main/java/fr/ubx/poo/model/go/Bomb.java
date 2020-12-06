package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Bomb extends GameObject {
    public Bomb(final Game game, final Position position) {
        super(game, position);
    }

    @Override
    public String toString() {
        return "Bomb";
    }
}
