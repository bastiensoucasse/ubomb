package fr.ubx.poo.model.decor.Bonus;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;

public abstract class Bonus extends Decor {
    private boolean isVisible;
    Bonus(boolean walkable){
        super(walkable);
        isVisible=true;
    }

    public abstract void pick(Player p);

    public void setVisibility(boolean b){
        isVisible=b;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    @Override
    public String toString() {
        return "Bonus";
    }
}
