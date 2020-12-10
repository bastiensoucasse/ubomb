package fr.ubx.poo.engine;

import fr.ubx.poo.game.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        loadLevel(false, true);
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
                statusBar.update(game);
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
            if (b != null){
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

    private void loadLevel() {
        loadLevel(false, false);
    }

    private void loadLevel(boolean back) {
        loadLevel(back, false);
    }

    private void loadLevel(boolean back, boolean skipPos) {
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().getDimension().height;
        int width = game.getWorld().getDimension().width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.show();
        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));

        List<List<Monster>> monsters = game.getWorld().getMonster();
        for (Monster m : monsters.get(game.getWorld().getLevel())) {
            sprites.add(SpriteFactory.createMonster(layer, m));
        }
        spritePlayer = SpriteFactory.createPlayer(layer, player);

        if (!skipPos) {
            try {
                if (back)
                    game.getPlayer().setPosition(game.getWorld().findDoorNextOpened());
                else
                    game.getPlayer().setPosition(game.getWorld().findDoorPrevOpened());
            } catch (PositionNotFoundException e) {
                System.err.println("Position not found : " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void update(long now) {
        player.update(now);
        if (game.getWorld().isChanged()) {
            redrawTheSprites();
            game.getWorld().setChanged(false);
        }
        if (game.getWorld().getLevelChange() == 1) {
            loadLevel();
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

    private void redrawTheSprites() {
        sprites.forEach(Sprite::remove);
        sprites.clear();

        game.getWorld().updateWorld();
        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        for(Monster m : game.getWorld().getMonster().get(game.getWorld().getLevel())){
            sprites.add(SpriteFactory.createMonster(layer, m));
        }

        for(Bomb b : game.getWorld().getBombs().get(game.getWorld().getLevel())){
            sprites.add(SpriteFactory.createBomb(layer, b));
        }
    }

    //-------------- WIP -------------------
    void createTimer(int i) {
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                //sprites.add(SpriteFactory.createBomb(layer, bombs.get(i)));
                SpriteBomb b = (SpriteBomb) sprites.get(sprites.size() - 1);
                b.setSprite_nb(3);
                b.updateImage();
            }
        };

        Timer timer = new Timer("Bomb timer");
        timer.schedule(timertask, 0, 1000);
    }

    private void render() {
        sprites.forEach(Sprite::render);
        spritePlayer.render();
    }

    public void start() {
        gameLoop.start();
    }
}
