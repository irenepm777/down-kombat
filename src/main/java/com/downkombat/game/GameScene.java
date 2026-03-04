package com.downkombat.game;

import com.downkombat.engine.GameLoop;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import com.downkombat.input.InputHandler;
import javafx.scene.input.KeyCode;


public class GameScene {

    private Scene scene;
    private InputHandler input;

    private Player player1;
    private Player player2;

    public GameScene() {

        Group root = new Group();

        player1 = new Player(200, 400, Color.RED);
        player2 = new Player(700, 400, Color.BLUE);

        root.getChildren().addAll(
                player1.getBody(),
                player2.getBody()
        );

        scene = new Scene(root, 1000, 600);

        input = new InputHandler(scene);

        GameLoop loop = new GameLoop(this::update);
        loop.start();
    }

    public Scene getScene() {
        return scene;
    }

private void update() {

    // Player 1
    if (input.isPressed(KeyCode.A)) {
        player1.moveLeft();
    }

    if (input.isPressed(KeyCode.D)) {
        player1.moveRight();
    }

    // Player 2
    if (input.isPressed(KeyCode.LEFT)) {
        player2.moveLeft();
    }

    if (input.isPressed(KeyCode.RIGHT)) {
        player2.moveRight();
    }
}
}
