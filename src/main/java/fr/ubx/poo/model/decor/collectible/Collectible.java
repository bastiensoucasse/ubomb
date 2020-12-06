package fr.ubx.poo.model.decor.collectible;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;

public abstract class Collectible extends Decor {
    private boolean visible;

    public Collectible() {
        super(true, true);
        this.visible = true;
    }

    @Override
    public String toString() {
        return "Collectible";
    }

    public boolean getVisibility() {
        return visible;
    }

    public void setVisibility(final boolean visible) {
        this.visible = visible;
    }

    public abstract void collect(final Player player);
}
