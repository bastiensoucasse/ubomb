/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;


public final class SpriteFactory {

    public static Sprite createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        if(decor instanceof Box)
            return new SpriteDecor(layer, factory.get(BOX), position);
        if (decor instanceof BonusBombNbDec)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_NB_DEC), position);
        if (decor instanceof BonusBombNbInc)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_NB_INC), position);
        if (decor instanceof BonusBombRangeDec)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_RANGE_DEC), position);
        if (decor instanceof BonusBombRangeInc)
            return new SpriteDecor(layer, factory.get(BONUS_BOMB_RANGE_INC), position);
        if (decor instanceof Princess)
            return new SpriteDecor(layer, factory.get(PRINCESS), position);
        if(decor instanceof Key)
            return new SpriteDecor(layer, factory.get(KEY), position);
        throw new RuntimeException("Unsupported sprite for decor " + decor);
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }

    public static Sprite createMonster(Pane layer, Monster monster){
        return new SpriteMonster(layer, monster);
    }
}
