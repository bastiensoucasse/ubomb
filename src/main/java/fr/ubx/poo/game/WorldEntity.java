/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import java.util.Arrays;
import java.util.Optional;

public enum WorldEntity {
    Empty('_'),
    Box('B'),
    Heart('H'),
    Key('K'),
    Monster('M'),
    DoorPrevOpened('V'),
    DoorNextOpened('N'),
    DoorNextClosed('n'),
    Player('P'),
    Stone('S'),
    Tree('T'),
    Princess('W'),
    BombRangeInc('>'),
    BombRangeDec('<'),
    BombNumberInc('+'),
    BombNumberDec('-'),
        ;


    private char getCode() {
        return code;
    }

    private final char code;

    WorldEntity(char code) {
        this.code = code;
    }

    public static Optional<WorldEntity> fromCode(char code) {
        return Arrays.stream(values())
                .filter(e->e.acceptCode(code))
                .findFirst();
    }

    private boolean acceptCode(char code) {
        return this.code == code;
    }

    @Override
    public String toString() {
        return ""+code;
    }
}
