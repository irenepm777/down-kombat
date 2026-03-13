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
        // Each button now uses a SKIN from /character_sheet/film/frame/
        StackPane btn1 = createButton("marco_antonio");
        StackPane btn2 = createButton("marco_dario");
        StackPane btn3 = createButton("marco_juanma");
        StackPane btn4 = createButton("marco_miguel");
        StackPane btn5 = createButton("marco_pepe");
        StackPane btn6 = createButton("marco_soraya");

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

    private StackPane createButton(String characterName) {

        String path = "/frame/" + characterName + ".png";

        // Debug para ver si JavaFX encuentra la imagen
        System.out.println("Buscando imagen: " + path);
        java.io.InputStream is = getClass().getResourceAsStream(path);
        System.out.println("Resultado: " + is);

        if (is == null) {
            System.out.println("ERROR: No se encontró la imagen " + path);
        }

        // Cargar imagen
        javafx.scene.image.Image img = new javafx.scene.image.Image(is);
        javafx.scene.image.ImageView frame = new javafx.scene.image.ImageView(img);
        frame.setFitWidth(120);
        frame.setFitHeight(120);

        // Botón transparente encima
        Button btn = new Button();
        btn.setPrefSize(120, 120);
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-padding: 0;"
        );

        btn.setOnAction(e -> selectCharacter(characterName));

        // Superponer imagen + botón
        StackPane slot = new StackPane();
        slot.getChildren().addAll(frame, btn);

        return slot;
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
