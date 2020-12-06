package fr.ubx.poo.model.decor.Bonus;

import fr.ubx.poo.model.go.character.Player;

public class BonusBombNbDec extends Bonus {
    public BonusBombNbDec(boolean walkable) {
        super(walkable);
    }

    @Override
    public void specializedAction(Player player) {
        if(player.getBombs()>1){
            player.decBombs();
            setVisibility(false);
        }
    }

    @Override
    public void pick(Player p) {
    }

    @Override
    public String toString() {
        return "BonusBombNbDec";
    }
}
