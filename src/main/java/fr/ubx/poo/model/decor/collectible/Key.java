package fr.ubx.poo.model.decor.collectible;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Collectible {
    @Override
    public String toString() {
        return "Key";
    }

    @Override
    public void collect(final Player player) {
        player.addKey();
    }
}
