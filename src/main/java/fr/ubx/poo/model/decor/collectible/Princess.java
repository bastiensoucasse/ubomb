package fr.ubx.poo.model.decor.collectible;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Collectible {
    @Override
    public String toString() {
        return "Princess";
    }

    @Override
    public void collect(final Player player) {
        player.win();
    }
}
