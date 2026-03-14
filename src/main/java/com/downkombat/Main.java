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

        // Load arcade font used by menu UI
        Font.loadFont(
                "https://fonts.gstatic.com/s/pressstart2p/v11/e3t4euO8T-267oIAQAu6jDQyK3k.woff2",
                10
        );

        // Load background video once so UI screens can reuse it
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

    public void showSelectP2(String p1) {

        this.player1 = p1;

        CharacterSelectController controller = new CharacterSelectController(this, false);

        stage.setTitle("DOWN KOMBAT - Select Player 2");
        stage.setScene(controller.getScene());
    }

    public void showSelectMap(String p2) {

        this.player2 = p2;

        MapsSelectController controller = new MapsSelectController(this, true);

        stage.setTitle("DOWN KOMBAT - Select Map");
        stage.setScene(controller.getScene());
    }

    public void startGame(String map) {

        // Convert UI ids like "marco_antonio" → "ANTONIO"
        String p1 = player1.replace("marco_", "").toUpperCase();
        String p2 = player2.replace("marco_", "").toUpperCase();

        CharacterType fighter1 = CharacterType.valueOf(p1);
        CharacterType fighter2 = CharacterType.valueOf(p2);

        GameScene game = new GameScene(
                fighter1,
                fighter2,
                map
        );

        stage.setScene(game.getScene());
    }

    public static void main(String[] args) {
        launch();
    }
}