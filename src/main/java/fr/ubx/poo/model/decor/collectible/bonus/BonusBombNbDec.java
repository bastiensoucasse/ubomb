package fr.ubx.poo.model.decor.collectible.bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombNbDec extends Bonus {
    @Override
    public String toString() {
        return "BonusBombNbDec";
    }

    @Override
    public void collect(final Player player) {
        if (player.getBombs() > 1) {
            player.removeBomb();
            setVisibility(false);
        }
    }
}
