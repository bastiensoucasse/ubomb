package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import javafx.geometry.Pos;

public class Bomb extends GameObject {
    private final int range;
    private boolean dropped;
    private boolean hasExplosed=false;

    public Bomb(final Game game, final Position position, final int range) {
        super(game, position);
        this.range = range;
    }

    @Override
    public String toString() {
        return "Bomb";
    }

    public boolean isDropped() {
        return dropped;
    }

    public void setDropped() {
        dropped = true;
    }

    public boolean hasExplosed(){
        return hasExplosed;
    }
    public void setExplosion(boolean b){
        hasExplosed = b;
    }

    public int getRange() {
        return range;
    }
}
