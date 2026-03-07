package com.downkombat.game;

import com.downkombat.config.GameConfig;
import com.downkombat.engine.GameLoop;
import com.downkombat.fighters.CharacterType;
import com.downkombat.fighters.Fighter;
import com.downkombat.fighters.FighterFactory;
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

    private Fighter fighter1;
    private Fighter fighter2;

    private FightManager fightManager;

    private HealthBar healthBarP1;
    private HealthBar healthBarP2;

    private GameState gameState = GameState.FIGHT;

    private Text winText;

    private long freezeEndTime = 0;

    public GameScene() {

        root = new Group();

        spawnFighters();

        fightManager = new FightManager(fighter1, fighter2);

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

    private void spawnFighters() {

        fighter1 = FighterFactory.create(CharacterType.ANTONIO, 640 - 200);
        fighter2 = FighterFactory.create(CharacterType.SORAYA, 640 + 200);

        root.getChildren().addAll(
                fighter1.getNode(),
                fighter2.getNode()
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
        if (input.isPressed(KeyCode.A)) fighter1.moveLeft();
        if (input.isPressed(KeyCode.D)) fighter1.moveRight();

        // MOVIMIENTO PLAYER 2
        if (input.isPressed(KeyCode.LEFT)) fighter2.moveLeft();
        if (input.isPressed(KeyCode.RIGHT)) fighter2.moveRight();

        // LIMITES DE PANTALLA

        if (fighter1.getX() < 60) fighter1.setX(60);
        if (fighter1.getX() > GameConfig.WIDTH - 60) fighter1.setX(GameConfig.WIDTH - 60);

        if (fighter2.getX() < 60) fighter2.setX(60);
        if (fighter2.getX() > GameConfig.WIDTH - 60) fighter2.setX(GameConfig.WIDTH - 60);

        // UPDATE EFECTOS VISUALES

        fighter1.update();
        fighter2.update();

        // ATAQUES NORMALES

        if (input.isPressed(KeyCode.F)) {

            if (fightManager.tryAttack(fighter1, fighter2)) {

                freezeEndTime = System.currentTimeMillis() + GameConfig.HIT_FREEZE;

                System.out.println("Player 2 vida: " + fighter2.getHealth());
            }
        }

        if (input.isPressed(KeyCode.K)) {

            if (fightManager.tryAttack(fighter2, fighter1)) {

                freezeEndTime = System.currentTimeMillis() + GameConfig.HIT_FREEZE;

                System.out.println("Player 1 vida: " + fighter1.getHealth());
            }
        }

        // ATAQUES ESPECIALES

        if (input.isPressed(KeyCode.G)) {

            if (fightManager.trySpecial(fighter1, fighter2)) {

                freezeEndTime = System.currentTimeMillis() + GameConfig.HIT_FREEZE;
            }
        }

        if (input.isPressed(KeyCode.L)) {

            if (fightManager.trySpecial(fighter2, fighter1)) {

                freezeEndTime = System.currentTimeMillis() + GameConfig.HIT_FREEZE;
            }
        }

        // UI

        healthBarP1.update(fighter1.getHealth());
        healthBarP2.update(fighter2.getHealth());

        // FIN DEL COMBATE

        if (fighter1.isDead()) {

            winText.setText("PLAYER 2 WINS\nPress R to restart");
            winText.setVisible(true);
            gameState = GameState.GAME_OVER;
        }

        if (fighter2.isDead()) {

            winText.setText("PLAYER 1 WINS\nPress R to restart");
            winText.setVisible(true);
            gameState = GameState.GAME_OVER;
        }
    }

    private void resetFight() {

        root.getChildren().remove(fighter1.getNode());
        root.getChildren().remove(fighter2.getNode());

        spawnFighters();

        fightManager = new FightManager(fighter1, fighter2);

        healthBarP1.update(GameConfig.PLAYER_MAX_HEALTH);
        healthBarP2.update(GameConfig.PLAYER_MAX_HEALTH);

        winText.setVisible(false);

        gameState = GameState.FIGHT;
    }
}
