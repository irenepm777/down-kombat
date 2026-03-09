package com.downkombat.game;

import com.downkombat.config.GameConfig;
import com.downkombat.engine.GameLoop;

import com.downkombat.fighters.CharacterType;
import com.downkombat.fighters.Fighter;
import com.downkombat.fighters.FighterFactory;

import com.downkombat.input.InputHandler;
import com.downkombat.ui.HealthBar;

import com.downkombat.combat.projectiles.ProjectileManager;
import com.downkombat.combat.projectiles.CarManager;

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

    private Fighter player1;
    private Fighter player2;

    private HealthBar healthBarP1;
    private HealthBar healthBarP2;

    private GameState gameState = GameState.FIGHT;

    private Text winText;

    private long freezeEndTime = 0;

    // managers
    private ProjectileManager projectileManager;
    private CarManager carManager;

    public GameScene() {

        root = new Group();

        projectileManager = new ProjectileManager(root);
        carManager = new CarManager(root);

        spawnPlayers();

        healthBarP1 = new HealthBar(40, 40, Color.RED);
        healthBarP2 = new HealthBar(840, 40, Color.BLUE);

        winText = new Text();
        winText.setFont(new Font(60));
        winText.setFill(Color.BLACK);
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

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }

    public CarManager getCarManager() {
        return carManager;
    }

    private void spawnPlayers() {

        player1 = FighterFactory.create(
                CharacterType.ANTONIO,
                640 - 200,
                projectileManager,
                carManager
        );

        player2 = FighterFactory.create(
                CharacterType.SORAYA,
                640 + 200,
                projectileManager,
                carManager
        );

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

        // movimiento
        if (input.isPressed(KeyCode.A)) player1.moveLeft();
        if (input.isPressed(KeyCode.D)) player1.moveRight();

        if (input.isPressed(KeyCode.LEFT)) player2.moveLeft();
        if (input.isPressed(KeyCode.RIGHT)) player2.moveRight();

        // límites pantalla
        if (player1.getX() < 60) player1.setX(60);
        if (player1.getX() > GameConfig.WIDTH - 60) player1.setX(GameConfig.WIDTH - 60);

        if (player2.getX() < 60) player2.setX(60);
        if (player2.getX() > GameConfig.WIDTH - 60) player2.setX(GameConfig.WIDTH - 60);

        // update fighters
        player1.update();
        player2.update();

        // ATAQUES NORMALES
        if (input.isPressed(KeyCode.F)) {

            if (player1.canAttack() && player1.isNear(player2) && player1.isFacing(player2)) {

                player1.performAttack(player2);

                freezeEndTime = System.currentTimeMillis() + GameConfig.HIT_FREEZE;
            }
        }

        if (input.isPressed(KeyCode.K)) {

            if (player2.canAttack() && player2.isNear(player1) && player2.isFacing(player1)) {

                player2.performAttack(player1);

                freezeEndTime = System.currentTimeMillis() + GameConfig.HIT_FREEZE;
            }
        }

        // ATAQUES ESPECIALES
        if (input.isPressed(KeyCode.G)) {

            if (player1.canSpecial()) {
                player1.performSpecial(player2);
            }
        }

        if (input.isPressed(KeyCode.L)) {

            if (player2.canSpecial()) {
                player2.performSpecial(player1);
            }
        }

        // UPDATE PROYECTILES
        projectileManager.update(player1);
        projectileManager.update(player2);

        // UPDATE COCHES
        carManager.update();

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