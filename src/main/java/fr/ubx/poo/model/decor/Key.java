package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Collectible {
    public Key() {
        super(true);
    }

    @Override
    public String toString() {
        return "Key";
    }

    @Override
    public void specializedAction(Player player) {
        player.incKey();
    }
}
