package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.collectible.bonus.Bonus;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;

import java.util.*;
import java.util.function.BiConsumer;

public class World {
    private final List<WorldEntity[][]> raw;
    private final List<Dimension> dimension = new ArrayList<>();
    private final List<Map<Position, Decor>> grid = new ArrayList<>();
    private List<List<Monster>> monsters = new ArrayList<>();
    private List<List<Bomb>> bombs = new ArrayList<>();
    private int level = 0;
    private boolean changed = true;
    private int levelChange = 0;

    public World(List<WorldEntity[][]> raw) {
        this.raw = raw;

        for (int level = 0; level < raw.size(); level++) {
            dimension.add(new Dimension(raw.get(level).length, raw.get(level)[0].length));
            grid.add(WorldBuilder.build(raw.get(level), dimension.get(level)));
            bombs.add(new ArrayList<>());
            monsters.add(new ArrayList<>());
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

    public Position findDoorPrevOpened() throws PositionNotFoundException {
        for (int x = 0; x < dimension.get(level).width; x++) {
            for (int y = 0; y < dimension.get(level).height; y++) {
                if (raw.get(level)[y][x] == WorldEntity.DoorPreviousOpened) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Door");
    }

    public Position findDoorNextOpened() throws PositionNotFoundException {
        for (int x = 0; x < dimension.get(level).width; x++) {
            for (int y = 0; y < dimension.get(level).height; y++) {
                if (raw.get(level)[y][x] == WorldEntity.DoorNextOpened) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Door");
    }

    public List<Position> findMonster(int level) {
        List<Position> position_of_monsters = new ArrayList<Position>();
        for (int x = 0; x < dimension.get(level).width; x++) {
            for (int y = 0; y < dimension.get(level).height; y++) {
                if (raw.get(level)[y][x] == WorldEntity.Monster) {
                    position_of_monsters.add(new Position(x, y));
                }
            }
        }
        return position_of_monsters;
    }

    public boolean isThereAMonster(Position pos) {
        if(!pos.inside(dimension.get(level)))
            return false;
        return raw.get(level)[pos.y][pos.x] == WorldEntity.Monster;
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

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void setLevelChange(int levelChange) {
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

    public void updateWorld() {
        Iterator<Map.Entry<Position,Decor>> itr = grid.get(level).entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Position,Decor> entry = itr.next();
            if(entry.getValue() instanceof Bonus)
                if(!((Bonus) entry.getValue()).getVisibility()){
                    itr.remove();
                }
        }
    }

    public List<List<Bomb>> getBombs() {
        return bombs;
    }

    public List<List<Monster>> getMonster() {
        return monsters;
    }

    public List<WorldEntity[][]> getRaw() {
        return raw;
    }
}
