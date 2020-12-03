package fr.ubx.poo.model.decor.Bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombRangeDec extends Bonus {
    public BonusBombRangeDec(boolean walkable) {
        super(walkable);
    }

    @Override
    public void pick(Player p) {
        if(p.decRange())
            setVisibility(false);
    }

    @Override
    public String toString() {
        return "BonusBombRangeDec";
    }
}
