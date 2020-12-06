package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Box extends Decor {
    @Override
    public String toString() {
        return "Box";
    }

    @Override
    public Boolean canBePicked() {
        return false;
    }

}
