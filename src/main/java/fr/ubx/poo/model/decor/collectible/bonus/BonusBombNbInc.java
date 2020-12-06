package fr.ubx.poo.model.decor.collectible.bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombNbInc extends Bonus {
    @Override
    public String toString() {
        return "BonusBombNbInc";
    }

    @Override
    public void collect(final Player player) {
        player.addBomb();
        setVisibility(false);
    }
}
