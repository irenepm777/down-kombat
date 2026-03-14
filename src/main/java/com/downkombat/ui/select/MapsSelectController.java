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

public class MapsSelectController {

    private Main main;
    private boolean selectMap;
    private Scene scene;

    public MapsSelectController(Main main, boolean selectMap) {
        this.main = main;
        this.selectMap = selectMap;

        createScene();
    }

    private void createScene() {

        // ======== 1. BACKGROUND VIDEO ========
        Media media = new Media(getClass().getResource("/film/seleccion_personaje.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setMute(true);
        mediaPlayer.play();

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(600);
        mediaView.setPreserveRatio(false);

        // ======== 2. TITLE ========
        Text title = new Text("Select Map");
        title.setStyle("-fx-font-size: 32px; -fx-fill: white;");

        // ======== 3. GRID OF MAP BUTTONS ========
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        StackPane btn1 = createButton("Betis");
        StackPane btn2 = createButton("fondo_934r");
        StackPane btn3 = createButton("fondo_dentista");
        StackPane btn4 = createButton("fondo_espacio");
        StackPane btn5 = createButton("fondo_insti");
        StackPane btn6 = createButton("fondo_oso");

        grid.add(btn1, 0, 0);
        grid.add(btn2, 1, 0);
        grid.add(btn3, 2, 0);
        grid.add(btn4, 0, 1);
        grid.add(btn5, 1, 1);
        grid.add(btn6, 2, 1);

        VBox content = new VBox(30, title, grid);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.getChildren().addAll(mediaView, content);

        scene = new Scene(root, 800, 600);
    }

    private StackPane createButton(String mapName) {

        String path = "/maps/" + mapName + ".png";

        java.io.InputStream is = getClass().getResourceAsStream(path);

        if (is == null) {
            System.out.println("ERROR: Image not found: " + path);
        }

        javafx.scene.image.Image img = new javafx.scene.image.Image(is);
        javafx.scene.image.ImageView frame = new javafx.scene.image.ImageView(img);
        frame.setFitWidth(120);
        frame.setFitHeight(120);

        Button btn = new Button();
        btn.setPrefSize(120, 120);
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        btn.setOnAction(e -> selectMap(mapName));

        StackPane slot = new StackPane();
        slot.getChildren().addAll(frame, btn);

        return slot;
    }

    private void selectMap(String mapName) {
        if (selectMap) {
            main.startGame(mapName);
        }
    }

    public Scene getScene() {
        return scene;
    }
}