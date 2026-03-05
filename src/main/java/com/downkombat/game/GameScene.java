package com.downkombat.game;

import com.downkombat.engine.GameLoop;
import com.downkombat.input.InputHandler;
import com.downkombat.ui.HealthBar;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameScene {

    private Scene scene;
    private InputHandler input;

    private Player player1;
    private Player player2;

    private HealthBar healthBarP1;
    private HealthBar healthBarP2;

    private GameState gameState = GameState.FIGHT;

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private static final int GROUND_Y = 650;

    public GameScene() {

        Group root = new Group();

        player1 = new Player(640 - 200, GROUND_Y, Color.RED);
        player2 = new Player(640 + 200, GROUND_Y, Color.BLUE);

        healthBarP1 = new HealthBar(40, 40, Color.RED);
        healthBarP2 = new HealthBar(840, 40, Color.BLUE);

        root.getChildren().addAll(
                player1.getNode(),
                player2.getNode(),
                healthBarP1.getNode(),
                healthBarP2.getNode()
        );

        scene = new Scene(root, WIDTH, HEIGHT);

        input = new InputHandler(scene);

        GameLoop loop = new GameLoop(this::update);
        loop.start();
    }

    public Scene getScene() {
        return scene;
    }

    private void update() {

        if (gameState != GameState.FIGHT) {
            return;
        }

        // MOVIMIENTO PLAYER 1
        if (input.isPressed(KeyCode.A)) {
            player1.moveLeft();
        }

        if (input.isPressed(KeyCode.D)) {
            player1.moveRight();
        }

        // MOVIMIENTO PLAYER 2
        if (input.isPressed(KeyCode.LEFT)) {
            player2.moveLeft();
        }

        if (input.isPressed(KeyCode.RIGHT)) {
            player2.moveRight();
        }

        // LIMITES PANTALLA

        if (player1.getX() < 60) {
            player1.setX(60);
        }

        if (player1.getX() > WIDTH - 60) {
            player1.setX(WIDTH - 60);
        }

        if (player2.getX() < 60) {
            player2.setX(60);
        }

        if (player2.getX() > WIDTH - 60) {
            player2.setX(WIDTH - 60);
        }

        // ATAQUES

        if (input.isPressed(KeyCode.F)) {

            if (player1.canAttack() && player1.isNear(player2) && player1.isFacing(player2)) {

                player1.registerAttack();

                player2.damage(5);
                player2.applyKnockback(player1, 20);

                System.out.println("Player 2 vida: " + player2.getHealth());
            }
        }

        if (input.isPressed(KeyCode.K)) {

            if (player2.canAttack() && player2.isNear(player1) && player2.isFacing(player1)) {

                player2.registerAttack();

                player1.damage(5);
                player1.applyKnockback(player2, 20);

                System.out.println("Player 1 vida: " + player1.getHealth());
            }
        }

        // UPDATE UI

        healthBarP1.update(player1.getHealth());
        healthBarP2.update(player2.getHealth());

        // FIN COMBATE

        if (player1.isDead()) {

            System.out.println("PLAYER 2 GANA");
            gameState = GameState.GAME_OVER;

        }

        if (player2.isDead()) {

            System.out.println("PLAYER 1 GANA");
            gameState = GameState.GAME_OVER;

        }
    }
}
