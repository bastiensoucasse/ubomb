package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.collectible.Collectible;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.door.Door;
import fr.ubx.poo.model.decor.door.DoorDestination;
import fr.ubx.poo.model.decor.door.DoorState;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;

import java.util.*;
import java.util.function.BiConsumer;

public class World {
    private final List<WorldEntity[][]> raw;
    private final List<Dimension> dimension = new ArrayList<>();
    private final List<Map<Position, Decor>> grid = new ArrayList<>();
    private final List<List<Monster>> monsters = new ArrayList<>();
    private final List<List<Bomb>> bombs = new ArrayList<>();
    private int level = 0;
    private boolean changed = true;
    private int levelChange = 0;

    public World(List<WorldEntity[][]> raw) {
        this.raw = raw;

        for (int level = 0; level < raw.size(); level++) {
            dimension.add(new Dimension(raw.get(level).length, raw.get(level)[0].length));
            grid.add(WorldBuilder.build(raw.get(level), dimension.get(level)));
            monsters.add(new ArrayList<>());
            bombs.add(new ArrayList<>());
        }
    }

    public Dimension getDimension() {
        return dimension.get(level);
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.get(level).width; x++) {
            for (int y = 0; y < dimension.get(level).height; y++) {
                if (raw.get(level)[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    public Position getOpenedDoorPosition(final DoorDestination destination) throws PositionNotFoundException {
        for (Position p : grid.get(getLevel()).keySet())
            if (get(p) instanceof Door && ((Door) get(p)).getState() == DoorState.OPENED && ((Door) get(p)).getDestination() == destination)
                return p;
        throw new PositionNotFoundException("Door");
    }

    public List<Position> findMonster(int level) {
        List<Position> position_of_monsters = new ArrayList<>();
        for (int x = 0; x < dimension.get(level).width; x++) {
            for (int y = 0; y < dimension.get(level).height; y++) {
                if (raw.get(level)[y][x] == WorldEntity.Monster) {
                    position_of_monsters.add(new Position(x, y));
                }
            }
        }
        return position_of_monsters;
    }

    public boolean isThereAMonster(Position position) {
        if (!position.inside(dimension.get(level))) return false;

        for (Monster m : monsters.get(level))
            if (m.getPosition().equals(position))
                return true;

        return false;
    }

    public boolean isThereABomb(Position position) {
        if (!position.inside(dimension.get(level))) return false;

        for (Bomb b : bombs.get(level)) {
            if (b.getPosition().equals(position))
                return true;
        }

        return false;
    }

    public boolean isThereAMovableBox(Position position1, Direction direction) {
        if (!position1.inside(dimension.get(level))) return false;

        Decor decor1 = get(position1);
        if (decor1 == null) return false;

        if (decor1.canBeMoved()) {
            Position position2 = direction.nextPosition(position1);
            if (!position2.inside(dimension.get(level))) return false;

            Decor decor2 = get(position2);
            if (decor2 == null) return !isThereAMonster(position2) && !isThereABomb(position2);

            if (decor2.canBeMoved()) {
                Position position3 = direction.nextPosition(position2);
                if (!position3.inside(dimension.get(level))) return false;

                Decor decor3 = get(position3);
                if (decor3 == null && !isThereAMonster(position3) && !isThereABomb(position3)) {
                    setDecor(position3, get(position2));
                    deleteDecor(position2);
                    setChanged(true);
                    return true;
                }
            }
        }

        return false;
    }

    public Decor get(Position position) {
        return grid.get(level).get(position);
    }


    public void deleteDecor(Position p) {
        grid.get(level).remove(p);
    }

    //Set the decor into the grid
    public void setDecor(Position position, Decor decor) {
        grid.get(level).put(position, decor);
    }

    public void clear(Position position) {
        grid.get(level).remove(position);
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.get(level).forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.get(level).values();
    }

    public boolean isInside(Position position) {
        return true; // to update
    }

    public boolean isEmpty(Position position) {
        return grid.get(level).get(position) == null;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(final boolean changed) {
        this.changed = changed;
    }

    public void setLevelChange(final int levelChange) {
        this.levelChange = levelChange;
    }

    public void levelUp() {
        level++;
        setLevelChange(1);
    }

    public void levelDown() {
        level--;
        setLevelChange(-1);
    }

    public int getLevelChange() {
        return levelChange;
    }

    public int getLevel() {
        return level;
    }

    public void cleanCollectible() {
        Iterator<Map.Entry<Position, Decor>> itr = grid.get(level).entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Position, Decor> entry = itr.next();
            if (entry.getValue().isCollectable())
                if (!((Collectible) entry.getValue()).getVisibility()) {
                    itr.remove();
                }
        }
    }

    public List<List<Bomb>> getBombs() {
        return bombs;
    }

    public List<List<Monster>> getMonsters() {
        return monsters;
    }

    public List<WorldEntity[][]> getRaw() {
        return raw;
    }

    public void removeBomb(Position position, int lvl) {
        bombs.get(lvl).removeIf(u -> u.getPosition().equals(position));
    }

    public Bomb BombExplosed() {
        for (Bomb b : bombs.get(getLevel())) {
            if (b.hasExplodes())
                return b;
        }
        return null;
    }

}
