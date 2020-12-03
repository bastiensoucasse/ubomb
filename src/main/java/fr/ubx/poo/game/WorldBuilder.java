package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.door.Door;
import fr.ubx.poo.model.decor.door.DoorDestination;
import fr.ubx.poo.model.decor.door.DoorState;
import fr.ubx.poo.model.decor.Bonus.BonusBombNbDec;
import fr.ubx.poo.model.decor.Bonus.BonusBombNbInc;
import fr.ubx.poo.model.decor.Bonus.BonusBombRangeDec;
import fr.ubx.poo.model.decor.Bonus.BonusBombRangeInc;

import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private WorldBuilder() {
    }

    public static Map<Position, Decor> build(WorldEntity[][] raw, Dimension dimension) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                Decor decor = processEntity(raw[y][x]);
                if (decor != null) builder.grid.put(pos, decor);
            }
        }
        return builder.grid;
    }

    private static Decor processEntity(WorldEntity entity) {
        return switch (entity) {
            case BombNumberDec -> new BonusBombNbDec(true);
            case BombNumberInc -> new BonusBombNbInc(true);
            case BombRangeDec -> new BonusBombRangeDec(true);
            case BombRangeInc -> new BonusBombRangeInc(true);
            case Box -> new Box();
            case DoorNextClosed -> new Door(DoorDestination.NEXT, DoorState.CLOSED);
            case DoorNextOpened -> new Door(DoorDestination.NEXT, DoorState.OPENED);
            case DoorPreviousOpened -> new Door(DoorDestination.PREVIOUS, DoorState.OPENED);
            case Heart -> new Heart();
            case Key -> new Key();
            case Princess -> new Princess();
            case Stone -> new Stone();
            case Tree -> new Tree();
            default -> null;
        };
    }
}
