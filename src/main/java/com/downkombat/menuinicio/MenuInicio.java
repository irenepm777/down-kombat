package com.downkombat.menuinicio;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MenuInicio extends Application {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static MediaPlayer player;               // música de fondo
    private MediaPlayer backgroundVideoPlayer;       // vídeo de fondo
    private Stage primaryStageRef;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStageRef = primaryStage;

        StackPane root = new StackPane();

        musicTest();

        // Cargar vídeo de fondo (si existe) — si no, imagen o gradiente
        MediaView bgVideo = loadBackgroundVideo();
        if (bgVideo != null) {
            root.getChildren().add(bgVideo);
        } else {
            ImageView bgImage = loadBackgroundImage();
            if (bgImage != null) root.getChildren().add(bgImage);
            else root.getChildren().add(createArcadeBackground());
        }

        VBox menuBox = createMenuBox();
        Text gameTitle = createGameTitle();
        Button startButton = createMenuButton("START", "start-button");
        Button tutorialButton = createMenuButton("TUTORIAL", "tutorial-button");

        startButton.setOnAction(e -> handleStart());
        tutorialButton.setOnAction(e -> handleTutorial());

        menuBox.getChildren().addAll(gameTitle, startButton, tutorialButton);
        root.getChildren().add(menuBox);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Enlazar MediaView al tamaño de la escena para cubrir todo el fondo
        if (bgVideo != null) {
            bgVideo.fitWidthProperty().bind(scene.widthProperty());
            bgVideo.fitHeightProperty().bind(scene.heightProperty());
            bgVideo.setPreserveRatio(false);
            bgVideo.setSmooth(true);
        }

        loadStylesheet(scene);

        primaryStage.setTitle("DAWN KOMBAT - MAIN MENU");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        playBackgroundMusic();
    }

    // Carga y configura MediaView para reproducir en bucle
    private MediaView loadBackgroundVideo() {
        try {
            var url = getClass().getResource("/video/fondoInicio.mp4");
            if (url == null) {
                System.err.println("No se encontró /video/fondoInicio.mp4");
                return null;
            }

            Media media = new Media(url.toExternalForm());
            backgroundVideoPlayer = new MediaPlayer(media);

            // Manejo de errores para depuración
            backgroundVideoPlayer.setOnError(() -> System.err.println("MediaPlayer error: " + backgroundVideoPlayer.getError()));
            media.setOnError(() -> System.err.println("Media error: " + media.getError()));

            // Asegurar bucle: ciclo indefinido y fallback con seek al final
            backgroundVideoPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundVideoPlayer.setOnEndOfMedia(() -> backgroundVideoPlayer.seek(Duration.ZERO));

            // Reproducir cuando esté listo
            backgroundVideoPlayer.setOnReady(() -> {
                backgroundVideoPlayer.setVolume(0.0); // silenciar por defecto si quieres
                backgroundVideoPlayer.play();
                System.out.println("Background video ready and playing (looping)");
            });

            MediaView mediaView = new MediaView(backgroundVideoPlayer);
            mediaView.setPreserveRatio(false);
            mediaView.setSmooth(true);
            return mediaView;

        } catch (MediaException me) {
            System.err.println("Error creando Media/MediaPlayer: " + me.getMessage());
            return null;
        } catch (Exception ex) {
            System.err.println("Error al cargar vídeo: " + ex.getMessage());
            return null;
        }
    }

    private ImageView loadBackgroundImage() {
        try {
            var stream = getClass().getResourceAsStream("/images/fondo.png");
            if (stream == null) return null;
            Image bg = new Image(stream);
            if (bg.isError()) return null;
            ImageView bgImage = new ImageView(bg);
            bgImage.setFitWidth(SCREEN_WIDTH);
            bgImage.setFitHeight(SCREEN_HEIGHT);
            bgImage.setPreserveRatio(false);
            return bgImage;
        } catch (Exception e) {
            System.err.println("Error al cargar imagen de fondo: " + e.getMessage());
            return null;
        }
    }

    private Rectangle createArcadeBackground() {
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.DARKSLATEBLUE),
                new Stop(0.3, Color.DARKBLUE),
                new Stop(0.7, Color.MIDNIGHTBLUE),
                new Stop(1, Color.DARKORANGE)
        );
        Rectangle bg = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT);
        bg.setFill(gradient);
        return bg;
    }

    private void playBackgroundMusic() {
        try {
            stopMusic();
            var url = getClass().getResource("/sounds/Menu_Inicio.mp3");
            if (url == null) return;
            Media media = new Media(url.toExternalForm());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(1.0);
            player.play();
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    private VBox createMenuBox() {
        VBox menuBox = new VBox(40);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateY(100);
        menuBox.setEffect(new Glow(0.2));
        return menuBox;
    }

    private Text createGameTitle() {
        Text title = new Text("DOWN\nKOMBAT");
        title.setId("game-title");
        try {
            Font arcadeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 64);
            if (arcadeFont != null) title.setFont(arcadeFont);
            else title.setFont(Font.font("Monospace", FontWeight.BOLD, 64));
        } catch (Exception e) {
            title.setFont(Font.font("Monospace", FontWeight.BOLD, 64));
        }
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.ORANGE);
        shadow.setRadius(15);
        shadow.setSpread(0.6);
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        title.setEffect(shadow);
        title.setFill(Color.GOLD);
        return title;
    }

    private Button createMenuButton(String text, String styleId) {
        Button button = new Button(text);
        button.setId(styleId);
        button.getStyleClass().add("arcade-button");
        button.setMinWidth(300);
        button.setMinHeight(70);

        button.setOnMouseEntered(e -> {
            button.setScaleX(1.15);
            button.setScaleY(1.15);
            button.setEffect(new Glow(0.8));
        });

        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            button.setEffect(null);
        });

        button.setOnMousePressed(e -> button.setTranslateY(2));
        button.setOnMouseReleased(e -> button.setTranslateY(0));

        return button;
    }

    private void musicTest() {
        try {
            var url = getClass().getResource("/sounds/Menu_Inicio.mp3");
            if (url == null) return;
            Media media = new Media(url.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.0);
            mediaPlayer.play();
            mediaPlayer.stop();
            mediaPlayer.dispose();
        } catch (Exception e) {
            System.err.println("Error en musicTest: " + e.getMessage());
        }
    }

    private void loadStylesheet(Scene scene) {
        try {
            var url = getClass().getResource("/css/style.css");
            if (url == null) return;
            scene.getStylesheets().add(url.toExternalForm());
        } catch (Exception e) {
            System.err.println("Error cargando CSS: " + e.getMessage());
        }
    }

    private void handleTutorial() {
        com.downkombat.tutorial.Tutorial tutorial = new com.downkombat.tutorial.Tutorial();
        tutorial.mostrar(this.primaryStageRef);
    }

    private void handleStart() {
        System.out.println("¡JUEGO INICIADO!");
    }

    public static MediaPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(MediaPlayer player) {
        MenuInicio.player = player;
    }

    @Override
    public void stop() {
        if (backgroundVideoPlayer != null) {
            try {
                backgroundVideoPlayer.stop();
                backgroundVideoPlayer.dispose();
            } catch (Exception e) {
                System.err.println("Error liberando video: " + e.getMessage());
            } finally {
                backgroundVideoPlayer = null;
            }
        }
        stopMusic();
        Platform.exit();
    }

    private void stopMusic() {
        if (player != null) {
            try {
                player.stop();
                player.dispose();
            } catch (Exception e) {
                System.err.println("Error al detener player: " + e.getMessage());
            } finally {
                player = null;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}