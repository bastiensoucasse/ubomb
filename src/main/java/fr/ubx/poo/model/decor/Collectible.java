package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;
import javafx.beans.binding.BooleanExpression;

public abstract class Collectible extends Decor{

    public Collectible(boolean walkable) {
        super(true);
    }

    public abstract void specializedAction(Player player);

    @Override
    public Boolean canBePicked() {
        return true;
    }
}
