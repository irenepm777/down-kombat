package com.downkombat.ui.select;

import com.downkombat.Main;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

public class CharacterSelectController {

    private Main main;
    private boolean selectingPlayer1;

    private Scene scene;

    public CharacterSelectController(Main main, boolean selectingPlayer1) {
        this.main = main;
        this.selectingPlayer1 = selectingPlayer1;

        createScene();
    }

    private void createScene() {

        // Background video
        Media media = new Media(
                getClass().getResource("/film/seleccion_personaje.mp4").toExternalForm()
        );

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(1280);
        mediaView.setFitHeight(720);
        mediaView.setPreserveRatio(false);

        // Title
        Text title = new Text("choose your fighter");
        title.setStyle("-fx-font-size: 32px; -fx-fill: white;");

        StackPane titleBox = new StackPane(title);
        titleBox.setStyle(
            "-fx-padding: 12px 25px;" +
            "-fx-background-radius: 10;" +
            "-fx-background-color:" +
                "linear-gradient(from 0% 0% to 100% 0%, #4e2f14 0%, #5e3a18 50%, #4e2f14 100%)," +
                "linear-gradient(#6b441e 0%, #3e2610 100%);" +
            "-fx-background-insets: 0, 1;"
        );

        // Character grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        StackPane btn1 = createButton("antonio", "marco_antonio");
        StackPane btn2 = createButton("dario", "marco_dario");
        StackPane btn3 = createButton("juanma", "marco_juanma");
        StackPane btn4 = createButton("migue", "marco_migue");
        StackPane btn5 = createButton("pepe", "marco_pepe");
        StackPane btn6 = createButton("soraya", "marco_soraya");

        grid.add(btn1, 0, 0);
        grid.add(btn2, 1, 0);
        grid.add(btn3, 2, 0);
        grid.add(btn4, 0, 1);
        grid.add(btn5, 1, 1);
        grid.add(btn6, 2, 1);

        VBox content = new VBox(30, titleBox, grid);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(mediaView, content);

        scene = new Scene(root, 1280, 720);
    }

    private StackPane createButton(String fighterId, String frameName) {

        String path = "/frame/" + frameName + ".png";

        System.out.println("Searching image: " + path);
        java.io.InputStream is = getClass().getResourceAsStream(path);
        System.out.println("Result: " + is);

        if (is == null) {
            System.out.println("ERROR: Image not found " + path);
        }

        javafx.scene.image.Image img = new javafx.scene.image.Image(is);
        javafx.scene.image.ImageView frame = new javafx.scene.image.ImageView(img);
        frame.setFitWidth(120);
        frame.setFitHeight(120);

        Button btn = new Button();
        btn.setPrefSize(120, 120);
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-padding: 0;"
        );

        btn.setOnAction(e -> selectCharacter(fighterId));

        StackPane slot = new StackPane();
        slot.getChildren().addAll(frame, btn);

        return slot;
    }

    private void selectCharacter(String characterName) {

        if (selectingPlayer1) {
            main.showSelectP2(characterName);
        } else {
            main.showSelectMap(characterName);
        }
    }

    public Scene getScene() {
        return scene;
    }
}