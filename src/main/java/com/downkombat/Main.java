package com.downkombat;

import com.downkombat.game.GameScene;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal del juego DOWN KOMBAT.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {

        GameScene game = new GameScene();

        stage.setTitle("DOWN KOMBAT");
        stage.setScene(game.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
