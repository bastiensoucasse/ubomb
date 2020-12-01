package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.*;

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
            case BombNumberDec -> new BonusBombNbDec();
            case BombNumberInc -> new BonusBombNbInc();
            case BombRangeDec -> new BonusBombRangeDec();
            case BombRangeInc -> new BonusBombRangeInc();
            case Box -> new Box();
            case DoorNextClosed -> new DoorNextClosed();
            case DoorNextOpened -> new DoorNextOpened();
            case DoorPrevOpened -> new DoorPrevOpened();
            case Heart -> new Heart();
            case Key -> new Key();
            case Princess -> new Princess();
            case Stone -> new Stone();
            case Tree -> new Tree();
            default -> null;
        };
    }
}
