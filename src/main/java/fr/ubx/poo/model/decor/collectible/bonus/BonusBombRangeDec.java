package fr.ubx.poo.model.decor.collectible.bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombRangeDec extends Bonus {
    @Override
    public String toString() {
        return "BonusBombRangeDec";
    }

    @Override
    public void collect(final Player player) {
        if (player.getBombsRange() > 1) {
            player.decreaseBombsRange();
            setVisibility(false);
        }
    }
}
