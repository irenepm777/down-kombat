package com.downkombat.ui.select;

import com.downkombat.Main;
import com.downkombat.ui.FontLoader;

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
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

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

        // ======== CARGAR FUENTE PIXEL ART ========
        FontLoader.loadPixelFont();

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

        // ======== 2. MENU CONTENT ========
        Text title = new Text(selectingPlayer1 ? "Selecciona Jugador 1" : "Selecciona Jugador 2");
        title.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-family: 'Press Start 2P';" +
            "-fx-fill: white;"
        );

        // ======== GRID DE PERSONAJES ========
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        StackPane btn1 = createButton("marco_antonio");
        StackPane btn2 = createButton("marco_dario");
        StackPane btn3 = createButton("marco_juanma");
        StackPane btn4 = createButton("marco_miguel");
        StackPane btn5 = createButton("marco_pepe");
        StackPane btn6 = createButton("marco_soraya");

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

    private StackPane createButton(String characterName) {

        String path = "/frame/" + characterName + ".png";

        java.io.InputStream is = getClass().getResourceAsStream(path);

        if (is == null) {
            System.out.println("ERROR: No se encontró la imagen " + path);
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

        // ======== ANIMACIONES RETRO ========

        // Hover: crece suavemente
        btn.setOnMouseEntered(e -> animateScale(btn, 1.0, 1.15, 120));
        btn.setOnMouseExited(e -> animateScale(btn, 1.15, 1.0, 120));

        // Click: se encoge rápido
        btn.setOnMousePressed(e -> animateScale(btn, 1.15, 0.90, 80));

        // Suelta: rebote suave
        btn.setOnMouseReleased(e -> animateScale(btn, 0.90, 1.15, 120));

        btn.setOnAction(e -> selectCharacter(characterName));

        StackPane slot = new StackPane();
        slot.getChildren().addAll(frame, btn);

        return slot;
    }

    // ======== MÉTODO DE ANIMACIÓN ========
    private void animateScale(Button btn, double from, double to, int ms) {
        ScaleTransition st = new ScaleTransition(Duration.millis(ms), btn);
        st.setFromX(from);
        st.setFromY(from);
        st.setToX(to);
        st.setToY(to);
        st.play();
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
