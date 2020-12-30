package fr.ubx.poo.game;

import java.io.*;
import java.util.*;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

import static fr.ubx.poo.game.WorldEntity.*;

public class Game {
    private final String worldPath;
    private String prefix;
    private int levels;
    private int lives;
    private final World world;
    private final Player player;

    public Game(String worldPath) {
        this.worldPath = worldPath;
        loadConfig();

        List<WorldEntity[][]> raw = new ArrayList<>();
        for (int level = 0; level < levels; level++)
            raw.add(loadEntities(level));
        world = new World(raw);

        try {
            player = new Player(this, world.findPlayer());
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        createMonsters();
    }

    public int getLevels() {
        return levels;
    }

    public int getLives() {
        return lives;
    }

    public World getWorld() {
        return world;
    }

    public void createMonsters(){
        for(int i=0; i<getWorld().getRaw().size(); i++){
            List<Position> pos_of_monster = getWorld().findMonster(i);
            for (Position pos : pos_of_monster) {
                world.getMonsters().get(i).add(new Monster(this, pos));
            }
        }
    }

    public void createBomb() {
        getWorld().getBombs().get(getWorld().getLevel()).add(new Bomb(this, getPlayer().getPosition(), player.getBombsRange()));

    }

    public Player getPlayer() {
        return player;
    }

    private void loadConfig() {
        try (InputStream input = new FileInputStream(new File(worldPath, "config.properties"))) {
            Properties prop = new Properties();
            prop.load(input);
            prefix = prop.getProperty("prefix", "level");
            levels = Integer.parseInt(prop.getProperty("levels", "3"));
            lives = Integer.parseInt(prop.getProperty("lives", "3"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    private WorldEntity[][] loadEntities(int level) {
        WorldEntity[][] raw = null;
        try (InputStream input = new FileInputStream(new File(worldPath, prefix + (level + 1) + ".txt"))) {
            List<WorldEntity[]> entities = new ArrayList<>();
            int width = 0, length = 0;
            Scanner sc = new Scanner(input);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                WorldEntity[] lineRaw = new WorldEntity[line.length()];
                for (int i = 0; i < line.length(); i++)
                    lineRaw[i] = fromCode(line.charAt(i)).orElse(Empty);
                entities.add(lineRaw);
                if (line.length() > length) length = line.length();
                width++;
            }
            sc.close();
            raw = new WorldEntity[width][length];
            raw = entities.toArray(raw);
        } catch (IOException ex) {
            System.err.println("Error loading entities");
        }
        return raw;
    }
}
