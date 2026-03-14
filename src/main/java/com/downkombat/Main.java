package com.downkombat;

import com.downkombat.fighters.CharacterType;
import com.downkombat.game.GameScene;
import com.downkombat.ui.select.CharacterSelectController;
import com.downkombat.ui.select.MapsSelectController;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    private String player1;
    private String player2;

    private MediaPlayer backgroundVideo;

    @Override
    public void start(Stage stage) {

        // Load background video ONCE
        Media media = new Media(getClass().getResource("/film/seleccion_personaje.mp4").toExternalForm());
        backgroundVideo = new MediaPlayer(media);
        backgroundVideo.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundVideo.setMute(true);
        backgroundVideo.play();

        this.stage = stage;

        // First screen: Player 1 selection
        CharacterSelectController controller = new CharacterSelectController(this, true);

        stage.setTitle("DOWN KOMBAT - Select Player 1");
        stage.setScene(controller.getScene());
        stage.show();
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

        GameScene game = new GameScene(
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