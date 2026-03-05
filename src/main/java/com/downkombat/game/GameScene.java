package com.downkombat.game;

import com.downkombat.engine.GameLoop;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import com.downkombat.input.InputHandler;
import javafx.scene.input.KeyCode;


public class GameScene {

    private boolean gameOver = false;
    private Scene scene;
    private InputHandler input;

    private Player player1;
    private Player player2;

    public GameScene() {

        Group root = new Group();

        player1 = new Player(640 - 200, 720 - 420, Color.RED);
        player2 = new Player(640 + 200, 720 - 420, Color.BLUE);
        root.getChildren().addAll(
                player1.getBody(),
                player2.getBody()
        );

        scene = new Scene(root, 1280, 720);

        input = new InputHandler(scene);

        GameLoop loop = new GameLoop(this::update);
        loop.start();
    }

    public Scene getScene() {
        return scene;
    }

private void update() {

    if (gameOver) {
        return;
    }

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

    // límites de pantalla
    double minX = 0;
    double maxX = 1280 - 125;

    // PLAYER 1
    double p1x = player1.getBody().getTranslateX();

    if (p1x < minX) {
        player1.getBody().setTranslateX(minX);
    }

    if (p1x > maxX) {
        player1.getBody().setTranslateX(maxX);
    }

    // PLAYER 2
    double p2x = player2.getBody().getTranslateX();

    if (p2x < minX) {
        player2.getBody().setTranslateX(minX);
    }

    if (p2x > maxX) {
        player2.getBody().setTranslateX(maxX);
    }
    //QUE MIRE AL SENTIDO AL QUE VA. SI EL OPP MIRA AL MISMO LADO EN PLAN Q LE DAS LA ESPALDA NO LE HACES DAÑO


    // Player 1 ataque
    if (input.isPressed(KeyCode.F)) {
        if (player1.isNear(player2)) {
            player2.damage(1);
            System.out.println("Player 2 vida: " + player2.getHealth());
        }
    }

    // Player 2 ataque
    if (input.isPressed(KeyCode.K)) {
        if (player2.isNear(player1)) {
            player1.damage(1);
            System.out.println("Player 1 vida: " + player1.getHealth());
        }
    }

    if (player1.isDead()) {
        System.out.println("PLAYER 2 GANA");
        gameOver = true;
    }

    if (player2.isDead()) {
        System.out.println("PLAYER 1 GANA");
        gameOver = true;
    }
}
}


// Meter cooldown, barra de vida,
