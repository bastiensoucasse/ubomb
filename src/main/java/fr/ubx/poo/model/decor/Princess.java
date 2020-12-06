package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class  Princess extends Collectible {
    public Princess() {
        super(true);
    }

    @Override
    public String toString() {
        return "Princess";
    }

    @Override
    public void specializedAction(Player player) {
        player.hasWon();
    }
}
