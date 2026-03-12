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

        // ======== 1. BACKGROUND VIDEO ========
        // Loads the video file from resources and prepares it as a looping background.
        Media media = new Media(getClass().getResource("/film/seleccion_personaje.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Infinite loop
        mediaPlayer.setMute(true); // No sound (optional)
        mediaPlayer.play();

        // Displays the video stretched to fill the window.
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(600);
        mediaView.setPreserveRatio(false); // Forces full-screen coverage


        // ======== 2. MENU CONTENT ========
        // Title changes depending on whether Player 1 or Player 2 is selecting.
        Text title = new Text(selectingPlayer1 ? "Selecciona Jugador 1" : "Selecciona Jugador 2");
        title.setStyle("-fx-font-size: 32px; -fx-fill: white;");

        // Grid layout for character buttons.
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Creates six character selection buttons.
        Button btn1 = createButton("char1", "red");
        Button btn2 = createButton("char2", "black");
        Button btn3 = createButton("char3", "blue");
        Button btn4 = createButton("char4", "green");
        Button btn5 = createButton("char5", "purple");
        Button btn6 = createButton("char6", "orange");

        // Adds buttons to the grid.
        grid.add(btn1, 0, 0);
        grid.add(btn2, 1, 0);
        grid.add(btn3, 2, 0);
        grid.add(btn4, 0, 1);
        grid.add(btn5, 1, 1);
        grid.add(btn6, 2, 1);

        // Vertical layout containing the title and the grid.
        VBox content = new VBox(30, title, grid);
        content.setAlignment(Pos.CENTER);


        // ======== 3. STACKPANE TO OVERLAY VIDEO + UI ========
        // StackPane allows placing the UI on top of the background video.
        StackPane root = new StackPane();
        root.getChildren().addAll(mediaView, content);

        // Creates the final scene.
        scene = new Scene(root, 800, 600);
    }

    private Button createButton(String characterName, String color) {

        // Creates a square button with a solid color representing a character.
        Button btn = new Button();
        btn.setPrefSize(90, 90);

        btn.setStyle("-fx-background-color: " + color + ";");

        // When clicked, the button selects the character.
        btn.setOnAction(e -> selectCharacter(characterName));

        return btn;
    }

    private void selectCharacter(String characterName) {
        // Saves the selected character depending on which player is choosing.
        if (selectingPlayer1) {
            player1 = characterName;
            main.showSelectP2(); // Moves to Player 2 selection screen.
        } else {
            player2 = characterName;
            main.startGame(player1, player2); // Starts the game with both characters.
        }
    }

    public Scene getScene() {
        return scene; // Returns the fully built scene.
    }
}
