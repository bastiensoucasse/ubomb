package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteBomb;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends GameObject {
    private List<SpriteBomb> explosions;
    private final int range;
    private boolean dropped;
    private boolean hasExplosed=false;

    public Bomb(final Game game, final Position position, final int range) {
        super(game, position);
        this.range = range;
        this.explosions = new ArrayList<>();
    }

    public SpriteBomb getSprite(){
        return explosions.get(0);
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

    public boolean hasExplodes(){
        return hasExplosed;
    }
    public void setExplosion(boolean b){
        hasExplosed = b;
    }

    public int getRange() {
        return range;
    }

    public void addExplosion(Sprite b) {
        explosions.add((SpriteBomb) b);
    }

    public List<SpriteBomb> getExplosions(){
        return explosions;
    }

    public void setSprite(SpriteBomb newBomb) {
        explosions.add(0, newBomb);
    }
}
