/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.DoorNextClosed;
import fr.ubx.poo.model.decor.DoorNextOpened;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;
    private boolean hasChanged = true;

    public World(WorldEntity[][] raw) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    public Position findDoorPrevOpened() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorPrevOpened) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("DoorPrevOpened");
    }

    public List<Position> findMonster() {
        List<Position> position_of_monsters = new ArrayList<Position>();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    position_of_monsters.add(new Position(x, y));
                }
            }
        }
        return position_of_monsters;
    }

    public boolean isThereAMonster(Position pos) {
        if(!pos.inside(dimension))
            return false;
        return raw[pos.y][pos.x] == WorldEntity.Monster;
    }

    public Decor get(Position position) {
        return grid.get(position);
    }

    public void deleteDecor(Position p) {
        grid.remove(p);
    }

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }

    public void clear(Position position) {
        grid.remove(position);
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        return true; // to update
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }

    public boolean hasChanged() {
        return hasChanged;
    }

    public void setChange(boolean b) {
        hasChanged = b;
    }

    public void openDoor(Position p) {
        if (!(get(p) instanceof DoorNextClosed)) return;
        deleteDecor(p);
        set(p, new DoorNextOpened());
        setChange(true);
    }
}
