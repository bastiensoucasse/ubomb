/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.*;

import fr.ubx.poo.model.go.character.Player;

import static fr.ubx.poo.game.WorldEntity.*;

public class Game {
    private final String worldPath;
    private String prefix;
    private int levels;
    private int level;
    private int lives;
    private final World world;
    private final Player player;

    public Game(String worldPath) {
        // Config init
        this.worldPath = worldPath;
        loadConfig();
        level = 1;

        // World init
        WorldEntity[][] entities = loadEntities();
        world = new World(entities);

        // Player init
        Position playerPosition = null;
        try {
            playerPosition = world.findPlayer();
            player = new Player(this, playerPosition);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public int getLives() {
        return lives;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
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

    private WorldEntity[][] loadEntities() {
        WorldEntity[][] raw = null;
        try (InputStream input = new FileInputStream(new File(worldPath, prefix + level + ".txt"))) {
            List<WorldEntity[]> entities = new ArrayList<WorldEntity[]>();
            int width = 0, length = 0;
            Scanner sc = new Scanner(input);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                WorldEntity[] lineRaw = new WorldEntity[line.length()];
                for (int i = 0; i < line.length(); i++)
                    lineRaw[i] = (WorldEntity) fromCode(line.charAt(i)).orElse(Empty);
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
