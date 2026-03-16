package com.downkombat.game;

import com.downkombat.Main;
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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;

public class GameScene {

    private Scene scene;
    private Group root;
    private InputHandler input;

    private Fighter player1;
    private Fighter player2;

    private CharacterType p1Type;
    private CharacterType p2Type;

    private HealthBar healthBarP1;
    private HealthBar healthBarP2;

    private GameState gameState = GameState.FIGHT;

    private Text winText;

    private long freezeEndTime = 0;

    private ProjectileManager projectileManager;
    private CarManager carManager;

    private ImageView background;

    private Rectangle darkOverlay;
    private VBox endMenu;
    private Button restartButton;
    private Button selectButton;

    private Main main;

    public GameScene(Main main, CharacterType p1Type, CharacterType p2Type, String map) {

        this.main = main;
        this.p1Type = p1Type;
        this.p2Type = p2Type;

        root = new Group();

        loadMap(map);

        projectileManager = new ProjectileManager(root);
        carManager = new CarManager(root);

        spawnPlayers();

        healthBarP1 = new HealthBar(40, 40, Color.RED);
        healthBarP2 = new HealthBar(840, 40, Color.BLUE);

        winText = new Text();
        winText.setFont(new Font(72));
        winText.setFill(Color.WHITE);
        winText.setX(GameConfig.WIDTH / 2 - 260);
        winText.setY(GameConfig.HEIGHT / 2 - 80);
        winText.setVisible(false);

        createEndMenu();

        root.getChildren().addAll(
                healthBarP1.getNode(),
                healthBarP2.getNode(),
                winText
        );

        scene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

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

    private void loadMap(String map) {

        try {

            String path = "/maps/" + map + ".png";

            Image bg = new Image(
                    getClass().getResourceAsStream(path)
            );

            background = new ImageView(bg);

            background.setFitWidth(GameConfig.WIDTH);
            background.setFitHeight(GameConfig.HEIGHT);

            root.getChildren().add(background);

            background.toBack();

        } catch (Exception e) {

            System.out.println("Map not found: " + map);

        }
    }

    private void spawnPlayers() {

        player1 = FighterFactory.create(
                p1Type,
                640 - 200,
                projectileManager,
                carManager
        );

        player2 = FighterFactory.create(
                p2Type,
                640 + 200,
                projectileManager,
                carManager
        );

        root.getChildren().addAll(
                player1.getNode(),
                player2.getNode()
        );
    }

    private void createEndMenu() {

        darkOverlay = new Rectangle(
                GameConfig.WIDTH,
                GameConfig.HEIGHT,
                Color.rgb(0,0,0,0.6)
        );

        darkOverlay.setVisible(false);

        restartButton = new Button("RESTART");
        selectButton = new Button("MENU");

        restartButton.setPrefWidth(220);
        selectButton.setPrefWidth(220);

        restartButton.setStyle("-fx-font-size:18;");
        selectButton.setStyle("-fx-font-size:18;");

        restartButton.setOnAction(e -> resetFight());

        selectButton.setOnAction(e -> main.startMenu());

        endMenu = new VBox(20, restartButton, selectButton);
        endMenu.setAlignment(Pos.CENTER);

        endMenu.setTranslateX(GameConfig.WIDTH / 2 - 100);
        endMenu.setTranslateY(GameConfig.HEIGHT / 2);

        endMenu.setVisible(false);

        root.getChildren().addAll(darkOverlay, endMenu);
    }

    private void update() {

        long now = System.currentTimeMillis();

        if (now < freezeEndTime) return;

        if (gameState == GameState.GAME_OVER) {
            return;
        }

        if (input.isPressed(KeyCode.A)) player1.moveLeft();
        if (input.isPressed(KeyCode.D)) player1.moveRight();

        if (input.isPressed(KeyCode.LEFT)) player2.moveLeft();
        if (input.isPressed(KeyCode.RIGHT)) player2.moveRight();

        clampToScreen(player1);
        clampToScreen(player2);

        player1.update();
        player2.update();

        double distance = Math.abs(player1.getX() - player2.getX());

        if (input.isPressed(KeyCode.F)
                && distance < GameConfig.ATTACK_RANGE
                && player1.canAttack()) {

            player1.performAttack(player2);

            freezeEndTime =
                    System.currentTimeMillis() + GameConfig.HIT_FREEZE;
        }

        if (input.isPressed(KeyCode.K)
                && distance < GameConfig.ATTACK_RANGE
                && player2.canAttack()) {

            player2.performAttack(player1);

            freezeEndTime =
                    System.currentTimeMillis() + GameConfig.HIT_FREEZE;
        }

        if (input.isPressed(KeyCode.G) && player1.canSpecial()) {
            player1.performSpecial(player2);
        }

        if (input.isPressed(KeyCode.L) && player2.canSpecial()) {
            player2.performSpecial(player1);
        }

        projectileManager.update(player1);
        projectileManager.update(player2);

        carManager.update();

        healthBarP1.update(player1.getHealth());
        healthBarP2.update(player2.getHealth());

        if (player1.isDead()) {
            showGameOver("PLAYER 2 WINS");
        }

        if (player2.isDead()) {
            showGameOver("PLAYER 1 WINS");
        }
    }

    private void showGameOver(String text) {

        winText.setText(text);

        // APLICAR LA CLASE CSS DEL ARCHIVO style.css
        if (!winText.getStyleClass().contains("win-text")) {
            winText.getStyleClass().add("win-text");
        }

        winText.setVisible(true);
        darkOverlay.setVisible(true);
        endMenu.setVisible(true);

        gameState = GameState.GAME_OVER;
    }

    private void clampToScreen(Fighter player) {

        if (player.getX() < 60)
            player.getNode().setTranslateX(60);

        if (player.getX() > GameConfig.WIDTH - 60)
            player.getNode().setTranslateX(GameConfig.WIDTH - 60);
    }

    private void resetFight() {

        root.getChildren().remove(player1.getNode());
        root.getChildren().remove(player2.getNode());

        spawnPlayers();

        healthBarP1.update(GameConfig.PLAYER_MAX_HEALTH);
        healthBarP2.update(GameConfig.PLAYER_MAX_HEALTH);

        winText.setVisible(false);
        darkOverlay.setVisible(false);
        endMenu.setVisible(false);

        gameState = GameState.FIGHT;
    }
}
