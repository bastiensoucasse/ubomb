package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class SpriteBomb extends SpriteGameObject {
    private int sprite_nb = 4;
    private Bomb b;
    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer,null,  bomb);
        updateImage();
    }

    @Override
    public void updateImage() {
        Bomb b = (Bomb) go;
        setImage(ImageFactory.getInstance().getBomb(sprite_nb));
    }

    public void setSprite_nb(int n){
        if(n >= 0 && n<= 4)
            sprite_nb = n;
    }

}
