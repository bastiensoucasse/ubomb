package fr.ubx.poo.model.decor.Bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombNbInc extends Bonus {
    public BonusBombNbInc(boolean walkable) {
        super(walkable);
    }

    @Override
    public void pick(Player p) {
        p.addBomb();
        setVisibility(false);
    }

    @Override
    public String toString() {
        return "BonusBombNbInc";
    }
}
