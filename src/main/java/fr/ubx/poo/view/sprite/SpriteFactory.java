package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.collectible.Heart;
import fr.ubx.poo.model.decor.collectible.Key;
import fr.ubx.poo.model.decor.collectible.Princess;
import fr.ubx.poo.model.decor.door.*;
import fr.ubx.poo.model.decor.collectible.bonus.BonusBombNbDec;
import fr.ubx.poo.model.decor.collectible.bonus.BonusBombNbInc;
import fr.ubx.poo.model.decor.collectible.bonus.BonusBombRangeDec;
import fr.ubx.poo.model.decor.collectible.bonus.BonusBombRangeInc;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public final class SpriteFactory {

    public static Sprite createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof BonusBombNbDec)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_NB_DEC), position);
        if (decor instanceof BonusBombNbInc)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_NB_INC), position);
        if (decor instanceof BonusBombRangeDec)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_RANGE_DEC), position);
        if (decor instanceof BonusBombRangeInc)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_RANGE_INC), position);
        if (decor instanceof Box)
            return new SpriteDecor(layer, factory.get(BOX), position);
        if (decor instanceof Door && ((Door) decor).getState() == DoorState.CLOSED)
            return new SpriteDecor(layer, factory.get(DOOR_CLOSED), position);
        if (decor instanceof Door && ((Door) decor).getState() == DoorState.OPENED)
            return new SpriteDecor(layer, factory.get(DOOR_OPENED), position);
        if (decor instanceof Heart)
            return new SpriteDecor(layer, factory.get(HEART), position);
        if (decor instanceof Key)
            return new SpriteDecor(layer, factory.get(KEY), position);
        if (decor instanceof Princess)
            return new SpriteDecor(layer, factory.get(PRINCESS), position);
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        throw new RuntimeException("Unsupported sprite for decor " + decor);
    }

    public static Sprite createMonster(Pane layer, Monster monster) {
        return new SpriteMonster(layer, monster);
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }

    public static Sprite createBomb(Pane layer, Bomb bomb) {
        SpriteBomb b =  new SpriteBomb(layer, bomb);
        bomb.addExplosion(b);
        return  b;
    }
}
