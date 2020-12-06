package fr.ubx.poo.model.decor.collectible.bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombRangeInc extends Bonus {
    @Override
    public String toString() {
        return "BonusBombRangeDec";
    }

    @Override
    public void collect(final Player player) {
        player.increaseBombsRange();
        setVisibility(false);
    }
}
