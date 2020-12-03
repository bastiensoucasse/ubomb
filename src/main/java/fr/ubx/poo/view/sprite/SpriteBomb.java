package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject {
    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer,null,  bomb);
        updateImage();
    }

    @Override
    public void updateImage() {
        Bomb b = (Bomb) go;
        setImage(ImageFactory.getInstance().getBomb());
    }
}
