package com.downkombat.game;

import com.downkombat.config.GameConfig;
import com.downkombat.engine.GameLoop;
import com.downkombat.input.InputHandler;
import com.downkombat.ui.HealthBar;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameScene {

    private Scene scene;
    private Group root;
    private InputHandler input;

    private Player player1;
    private Player player2;

    private HealthBar healthBarP1;
    private HealthBar healthBarP2;

    private GameState gameState = GameState.FIGHT;

    private Text winText;

    private long freezeEndTime = 0;

    public GameScene() {

        root = new Group();

        spawnPlayers();

        healthBarP1 = new HealthBar(40, 40, Color.RED);
        healthBarP2 = new HealthBar(840, 40, Color.BLUE);

        winText = new Text();
        winText.setFont(new Font(60));
        winText.setFill(Color.WHITE);
        winText.setX(400);
        winText.setY(360);
        winText.setVisible(false);

        root.getChildren().addAll(
                healthBarP1.getNode(),
                healthBarP2.getNode(),
                winText
        );

        scene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT);

        input = new InputHandler(scene);

        GameLoop loop = new GameLoop(this::update);
        loop.start();
    }

    public Scene getScene() {
        return scene;
    }

    private void spawnPlayers() {

        player1 = new Player(640 - 200, GameConfig.GROUND_Y, Color.RED);
        player2 = new Player(640 + 200, GameConfig.GROUND_Y, Color.BLUE);

        root.getChildren().addAll(
                player1.getNode(),
                player2.getNode()
        );
    }

    private void update() {

        long now = System.currentTimeMillis();

        if (now < freezeEndTime) {
            return;
        }

        if (gameState == GameState.GAME_OVER) {

            if (input.isPressed(KeyCode.R)) {
                resetFight();
            }

            return;
        }

        // MOVIMIENTO PLAYER 1
        if (input.isPressed(KeyCode.A)) player1.moveLeft();
        if (input.isPressed(KeyCode.D)) player1.moveRight();

        // MOVIMIENTO PLAYER 2
        if (input.isPressed(KeyCode.LEFT)) player2.moveLeft();
        if (input.isPressed(KeyCode.RIGHT)) player2.moveRight();

        // LIMITES PANTALLA
        if (player1.getX() < 60) player1.setX(60);
        if (player1.getX() > GameConfig.WIDTH - 60) player1.setX(GameConfig.WIDTH - 60);

        if (player2.getX() < 60) player2.setX(60);
        if (player2.getX() > GameConfig.WIDTH - 60) player2.setX(GameConfig.WIDTH - 60);

        // UPDATE PLAYER EFFECTS
        player1.update();
        player2.update();

        // ATAQUE PLAYER 1
        if (input.isPressed(KeyCode.F)) {

            if (player1.canAttack() && player1.isNear(player2) && player1.isFacing(player2)) {

                player1.registerAttack();

                player2.damage(GameConfig.ATTACK_DAMAGE);
                player2.applyKnockback(player1, GameConfig.KNOCKBACK_FORCE);

                freezeEndTime = System.currentTimeMillis() + GameConfig.HIT_FREEZE;

                System.out.println("Player 2 vida: " + player2.getHealth());
            }
        }

        // ATAQUE PLAYER 2
        if (input.isPressed(KeyCode.K)) {

            if (player2.canAttack() && player2.isNear(player1) && player2.isFacing(player1)) {

                player2.registerAttack();

                player1.damage(GameConfig.ATTACK_DAMAGE);
                player1.applyKnockback(player2, GameConfig.KNOCKBACK_FORCE);

                freezeEndTime = System.currentTimeMillis() + GameConfig.HIT_FREEZE;

                System.out.println("Player 1 vida: " + player1.getHealth());
            }
        }

        // UI
        healthBarP1.update(player1.getHealth());
        healthBarP2.update(player2.getHealth());

        // FIN COMBATE
        if (player1.isDead()) {

            winText.setText("PLAYER 2 WINS\nPress R to restart");
            winText.setVisible(true);
            gameState = GameState.GAME_OVER;

        }

        if (player2.isDead()) {

            winText.setText("PLAYER 1 WINS\nPress R to restart");
            winText.setVisible(true);
            gameState = GameState.GAME_OVER;

        }
    }

    private void resetFight() {

        root.getChildren().remove(player1.getNode());
        root.getChildren().remove(player2.getNode());

        spawnPlayers();

        healthBarP1.update(GameConfig.PLAYER_MAX_HEALTH);
        healthBarP2.update(GameConfig.PLAYER_MAX_HEALTH);

        winText.setVisible(false);

        gameState = GameState.FIGHT;
    }
}
