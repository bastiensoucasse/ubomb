package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Collectible {
    public Heart() {
        super(true);
    }

    @Override
    public String toString() {
        return "Heart";
    }

    @Override
    public void specializedAction(Player player) {
        player.incLife();
    }
}
