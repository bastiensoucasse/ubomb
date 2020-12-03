package fr.ubx.poo.model.decor.Bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombNbDec extends Bonus {
    public BonusBombNbDec(boolean walkable) {
        super(walkable);
    }

    @Override
    public void pick(Player p) {
        if(p.getBombs()>1){
            p.decBombs();
            setVisibility(false);
        }
    }

    @Override
    public String toString() {
        return "BonusBombNbDec";
    }
}
