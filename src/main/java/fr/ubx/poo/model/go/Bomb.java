package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Bomb extends GameObject{
    private int range;
    public Bomb(Game game, Position position) {
        super(game, position);
        range = 1;
    }

    public void incRange() {
        range++;
    }

    public void decRange(){
        range--;
    }

    public int getRange() {
        return range;
    }
}
