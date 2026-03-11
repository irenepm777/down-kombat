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

    private String player1;
    private String player2;

    private Scene scene;

    public CharacterSelectController(Main main, boolean selectingPlayer1) {
        this.main = main;
        this.selectingPlayer1 = selectingPlayer1;

        createScene();
    }

    private void createScene() {

        // ======== 1. VIDEO DE FONDO ========
        Media media = new Media(getClass().getResource("/film/seleccion_personaje.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop infinito
        mediaPlayer.setMute(true); // Sin sonido (opcional)
        mediaPlayer.play();

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(600);
        mediaView.setPreserveRatio(false); // Para cubrir toda la pantalla


        // ======== 2. CONTENIDO DEL MENÚ ========
        Text title = new Text(selectingPlayer1 ? "Selecciona Jugador 1" : "Selecciona Jugador 2");
        title.setStyle("-fx-font-size: 32px; -fx-fill: white;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        Button btn1 = createButton("char1", "red");
        Button btn2 = createButton("char2", "black");
        Button btn3 = createButton("char3", "blue");
        Button btn4 = createButton("char4", "green");
        Button btn5 = createButton("char5", "purple");
        Button btn6 = createButton("char6", "orange");

        grid.add(btn1, 0, 0);
        grid.add(btn2, 1, 0);
        grid.add(btn3, 2, 0);
        grid.add(btn4, 0, 1);
        grid.add(btn5, 1, 1);
        grid.add(btn6, 2, 1);

        VBox content = new VBox(30, title, grid);
        content.setAlignment(Pos.CENTER);


        // ======== 3. STACKPANE PARA SUPERPONER VIDEO + UI ========
        StackPane root = new StackPane();
        root.getChildren().addAll(mediaView, content);

        scene = new Scene(root, 800, 600);
    }

    private Button createButton(String characterName, String color) {

        Button btn = new Button();
        btn.setPrefSize(150, 150);

        btn.setStyle("-fx-background-color: " + color + ";");

        btn.setOnAction(e -> selectCharacter(characterName));

        return btn;
    }

    private void selectCharacter(String characterName) {
        if (selectingPlayer1) {
            player1 = characterName;
            main.showSelectP2();
        } else {
            player2 = characterName;
            main.startGame(player1, player2);
        }
    }

    public Scene getScene() {
        return scene;
    }
}