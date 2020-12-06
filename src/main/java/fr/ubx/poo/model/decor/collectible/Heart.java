package fr.ubx.poo.model.decor.collectible;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Collectible {
    @Override
    public String toString() {
        return "Heart";
    }

    @Override
    public void collect(final Player player) {
        player.addLife();
    }
}
