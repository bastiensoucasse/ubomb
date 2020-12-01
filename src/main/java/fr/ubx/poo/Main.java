package fr.ubx.poo;

import fr.ubx.poo.engine.GameEngine;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ImageFactory.getInstance().load();
        String path = getClass().getResource("/sample").getFile();
        Game game = new Game(path);
        GameEngine engine = new GameEngine("UBomb", game, stage);
        engine.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
