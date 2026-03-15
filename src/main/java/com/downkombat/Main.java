package com.downkombat;

import com.downkombat.fighters.CharacterType;
import com.downkombat.game.GameScene;
import com.downkombat.menuinicio.MenuInicio;
import com.downkombat.ui.select.CharacterSelectController;
import com.downkombat.ui.select.MapsSelectController;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    private String player1;
    private String player2;

    private MediaPlayer backgroundVideo;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // Load retro menu font
        Font.loadFont(
            "https://fonts.gstatic.com/s/pressstart2p/v11/e3t4euO8T-267oIAQAu6jDQyK3k.woff2",
            10
        );

        // Load background video used in menu screens
        Media media = new Media(
            getClass().getResource("/film/seleccion_personaje.mp4").toExternalForm()
        );

        backgroundVideo = new MediaPlayer(media);
        backgroundVideo.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundVideo.setMute(true);
        backgroundVideo.play();

        startMenu();
    }

    public void startMenu() {

        MenuInicio menu = new MenuInicio(this);
        menu.start(stage);
    }

    public MediaPlayer getBackgroundVideo() {
        return backgroundVideo;
    }

    // PLAYER 1 CHARACTER SELECT
    public void showSelectP1() {

        CharacterSelectController controller =
            new CharacterSelectController(this, true);

        stage.setTitle("DOWN KOMBAT - Select Player 1");
        stage.setScene(controller.getScene());
        stage.show();
    }

    // PLAYER 2 CHARACTER SELECT
    public void showSelectP2(String p1) {

        this.player1 = p1;

        CharacterSelectController controller =
            new CharacterSelectController(this, false);

        stage.setTitle("DOWN KOMBAT - Select Player 2");
        stage.setScene(controller.getScene());
    }

    // MAP SELECT
    public void showSelectMap(String p2) {

        this.player2 = p2;

        MapsSelectController controller =
            new MapsSelectController(this, true);

        stage.setTitle("DOWN KOMBAT - Select Map");
        stage.setScene(controller.getScene());
    }

    // START GAME
    public void startGame(String map) {

        GameScene game = new GameScene(
            this,
            CharacterType.valueOf(player1.toUpperCase()),
            CharacterType.valueOf(player2.toUpperCase()),
            map
        );

        stage.setScene(game.getScene());
    }

    public static void main(String[] args) {
        launch();
    }
}