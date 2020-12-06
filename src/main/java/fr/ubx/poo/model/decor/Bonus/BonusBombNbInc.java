package fr.ubx.poo.model.decor.Bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombNbInc extends Bonus {
    public BonusBombNbInc(boolean walkable) {
        super(walkable);
    }

    @Override
    public void specializedAction(Player player) {
        player.addBomb();
        setVisibility(false);
    }

    @Override
    public void pick(Player p) {
    }

    @Override
    public String toString() {
        return "BonusBombNbInc";
    }
}
