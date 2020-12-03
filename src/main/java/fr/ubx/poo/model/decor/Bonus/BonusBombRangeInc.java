package fr.ubx.poo.model.decor.Bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombRangeInc extends Bonus {
    public BonusBombRangeInc(boolean walkable) {
        super(walkable);
    }

    @Override
    public void pick(Player p) {
        p.incRange();
        setVisibility(false);
    }

    @Override
    public String toString() {
        return "BonusBombRangeDec";
    }
}
