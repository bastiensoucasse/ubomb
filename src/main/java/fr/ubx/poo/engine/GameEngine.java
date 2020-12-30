package fr.ubx.poo.engine;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.door.DoorDestination;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteBomb;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.model.go.character.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private final List<SpriteBomb> sbomb = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private final Stage stage;
    private Sprite spritePlayer;
    private long lastTime = -1;

    public GameEngine(final String windowTitle, final Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.stage = stage;
        this.player = game.getPlayer();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().getDimension().height;
        int width = game.getWorld().getDimension().width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        stage.setTitle(windowTitle);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));

        List<List<Monster>> monsters = game.getWorld().getMonsters();
        for (Monster m : monsters.get(game.getWorld().getLevel())) {
            sprites.add(SpriteFactory.createMonster(layer, m));
        }
        spritePlayer = SpriteFactory.createPlayer(layer, player);
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isKey()) {
            player.requestOpen();
        }
        if (input.isBomb()) {
            Bomb b = player.dropBomb();
            if (b != null) {
                game.createBomb();
            }
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }

    private void loadLevel(final boolean back) {
        stage.close();
        initialize();

        try {
            if (back)
                player.setPosition(game.getWorld().getOpenedDoorPosition(DoorDestination.NEXT));
            else
                player.setPosition(game.getWorld().getOpenedDoorPosition(DoorDestination.PREVIOUS));
        } catch (PositionNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Called each frame
    private void update(long now) {
        // Update the player
        player.update(now);

        // Stuff to do every second
        if (now >= lastTime + 1000000000 || lastTime == -1) {
            // Last time we entered this loop is now
            lastTime = now;

            // Update all the monsters
            game.getWorld().getMonsters().get(game.getWorld().getLevel()).forEach(m -> m.update(now));
            game.getWorld().setChanged(true);

            // Check for any monster attack
            if (game.getWorld().isThereAMonster(player.getPosition()))
                player.removeLife();
        }

        Bomb b = game.getWorld().BombExplosed();

        if (b != null) {
            b.setExplosion(false);
            destruction(b);
        }

        if (game.getWorld().isChanged()) {
            redrawTheSprites();
            game.getWorld().setChanged(false);
        }
        if (player.hasDroppedABomb()) {
            drawBombs();
            player.setBombDropped(false);
        }
        if (game.getWorld().getLevelChange() == 1) {
            loadLevel(false);
            game.getWorld().setLevelChange(0);
        }
        if (game.getWorld().getLevelChange() == -1) {
            loadLevel(true);
            game.getWorld().setLevelChange(0);
        }
        if (!player.isAlive()) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
    }

    private void destruction(Bomb b) {
        destruct_recursive(Direction.N, Direction.N.nextPosition(b.getPosition()), b.getRange());
        destruct_recursive(Direction.S, Direction.S.nextPosition(b.getPosition()), b.getRange());
        destruct_recursive(Direction.W, Direction.W.nextPosition(b.getPosition()), b.getRange());
        destruct_recursive(Direction.E, Direction.E.nextPosition(b.getPosition()), b.getRange());
    }

    private void destruct_recursive(Direction d, Position pos, int i) {
        if (i == 0)
            return;
        Decor de = game.getWorld().get(pos);

        if (de != null && (de.canBeMoved())) {
            game.getWorld().deleteDecor(pos);
            return;
        }
        if (de != null && !de.canBeMoved())
            return;

        if (pos.equals(player.getPosition())) {
            player.removeLife();
        }
        //MONSTER ANIHILATION
        Iterator<Monster> itr = game.getWorld().getMonsters().get(game.getWorld().getLevel()).iterator();
        while (itr.hasNext()) {
            Monster u = itr.next();
            if (u.getPosition().equals(pos)) {
                u.removeLife();
                if (u.getLives() == 0)
                    itr.remove();
            }
        }
        //FOR THE BOXES && BONUS
        if (de != null && de.isCollectable())
            game.getWorld().deleteDecor(pos);
        redrawTheSprites();
        destruct_recursive(d, d.nextPosition(pos), i - 1);
    }

    private void redrawTheSprites() {
        sprites.forEach(Sprite::remove);
        sprites.clear();

        game.getWorld().updateWorld();
        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        for (Monster m : game.getWorld().getMonsters().get(game.getWorld().getLevel())) {
            sprites.add(SpriteFactory.createMonster(layer, m));
        }
        sprites.addAll(sbomb);
    }

    private void drawBombs() {
        for (Bomb b : game.getWorld().getBombs().get(game.getWorld().getLevel())) {
            if (!b.isDropped()) {
                Sprite sb = SpriteFactory.createBomb(layer, b);
                sbomb.add((SpriteBomb) sb);
                sprites.add(sb);
                createTimer(sb);
                b.setDropped();
            }
        }
    }

    //-------------- WIP -------------------
    void createTimer(Sprite b) {
        SpriteBomb sb = (SpriteBomb) b;
        Timer timer = new Timer("Bomb timer");
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task performed on " + new Date());
                for (int i = 4; i >= 0; i--) {
                    sb.setSprite_nb(i);
                    sb.updateImage();
                    if (i == 0) {
                        for (Bomb b : game.getWorld().getBombs().get(game.getWorld().getLevel())) {
                            if (b.getPosition().equals(sb.getPosition())) {
                                b.setExplosion(true);
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                        if (i == 0) {
                            Platform.runLater(() -> {
                                sbomb.remove(sb);
                                redrawTheSprites();
                            });
                            game.getWorld().removeBomb(sb.getPosition());
                            timer.cancel();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.schedule(timertask, new Date());
    }

    private void render() {
        sprites.forEach(Sprite::render);
        spritePlayer.render();

        statusBar.update(game);
    }

    public void start() {
        gameLoop.start();
    }
}
