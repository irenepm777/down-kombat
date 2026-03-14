package com.downkombat.menuinicio;

import com.downkombat.Main;
import com.downkombat.ui.select.CharacterSelectController;

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

    private static MediaPlayer musicPlayer;
    private MediaPlayer backgroundVideoPlayer;

    private Stage primaryStageRef;
    private Main main;

    public MenuInicio(Main main) {
        this.main = main;
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStageRef = primaryStage;

        StackPane root = new StackPane();

        MediaView bgVideo = loadBackgroundVideo();

        if (bgVideo != null) {
            root.getChildren().add(bgVideo);
        } else {
            ImageView bgImage = loadBackgroundImage();

            if (bgImage != null) {
                root.getChildren().add(bgImage);
            } else {
                root.getChildren().add(createFallbackBackground());
            }
        }

        VBox menuBox = createMenuBox();

        Text title = createGameTitle();

        Button startButton = createMenuButton("START", "start-button");
        Button tutorialButton = createMenuButton("TUTORIAL", "tutorial-button");

        startButton.setOnAction(e -> handleStart());
        tutorialButton.setOnAction(e -> handleTutorial());

        menuBox.getChildren().addAll(title, startButton, tutorialButton);

        root.getChildren().add(menuBox);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        if (bgVideo != null) {
            bgVideo.fitWidthProperty().bind(scene.widthProperty());
            bgVideo.fitHeightProperty().bind(scene.heightProperty());
            bgVideo.setPreserveRatio(false);
        }

        loadStylesheet(scene);

        primaryStage.setTitle("DOWN KOMBAT - MAIN MENU");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        playBackgroundMusic();
    }

    private void handleStart() {

        System.out.println("Opening character select");

        pauseMedia();

        // Start with PLAYER 1 selection
        CharacterSelectController controller =
                new CharacterSelectController(main, true);

        primaryStageRef.setTitle("DOWN KOMBAT - Select Player 1");
        primaryStageRef.setScene(controller.getScene());
    }

    private void handleTutorial() {

        System.out.println("Opening Tutorial");

        pauseMedia();

        primaryStageRef.setOnShown(e -> {
            resumeMedia();
            primaryStageRef.setOnShown(null);
        });

        com.downkombat.tutorial.Tutorial tutorial =
                new com.downkombat.tutorial.Tutorial();

        tutorial.mostrar(primaryStageRef);
    }

    private void pauseMedia() {

        if (backgroundVideoPlayer != null)
            backgroundVideoPlayer.pause();

        if (musicPlayer != null)
            musicPlayer.pause();
    }

    private void resumeMedia() {

        if (backgroundVideoPlayer != null)
            backgroundVideoPlayer.play();

        if (musicPlayer != null)
            musicPlayer.play();
    }

    private void stopMusic() {

        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    private MediaView loadBackgroundVideo() {

        try {

            var url = getClass().getResource("/video/fondoInicio.mp4");

            if (url == null)
                return null;

            Media media = new Media(url.toExternalForm());

            backgroundVideoPlayer = new MediaPlayer(media);

            backgroundVideoPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            backgroundVideoPlayer.setOnEndOfMedia(
                    () -> backgroundVideoPlayer.seek(Duration.ZERO)
            );

            backgroundVideoPlayer.setOnReady(() -> {
                backgroundVideoPlayer.setVolume(0.0);
                backgroundVideoPlayer.play();
            });

            return new MediaView(backgroundVideoPlayer);

        } catch (MediaException e) {

            return null;
        }
    }

    private ImageView loadBackgroundImage() {

        try {

            var stream = getClass().getResourceAsStream("/images/fondo.png");

            if (stream == null)
                return null;

            Image bg = new Image(stream);

            ImageView view = new ImageView(bg);

            view.setFitWidth(SCREEN_WIDTH);
            view.setFitHeight(SCREEN_HEIGHT);
            view.setPreserveRatio(false);

            return view;

        } catch (Exception e) {

            return null;
        }
    }

    private Rectangle createFallbackBackground() {

        LinearGradient gradient = new LinearGradient(
                0,0,1,1,true,CycleMethod.NO_CYCLE,
                new Stop(0, Color.DARKSLATEBLUE),
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

            if (url == null)
                return;

            Media media = new Media(url.toExternalForm());

            musicPlayer = new MediaPlayer(media);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.play();

        } catch (Exception e) {

            System.err.println("Music error");
        }
    }

    private VBox createMenuBox() {

        VBox box = new VBox(40);

        box.setId("menu-box");
        box.setAlignment(Pos.CENTER);
        box.setTranslateY(100);
        box.setEffect(new Glow(0.2));

        return box;
    }

    private Text createGameTitle() {

        Text title = new Text("DOWN\nKOMBAT");

        title.setId("game-title");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 64));

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.ORANGE);
        shadow.setRadius(15);

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

            button.setScaleX(1);
            button.setScaleY(1);

            button.setEffect(null);
        });

        button.setOnMousePressed(e -> button.setTranslateY(2));
        button.setOnMouseReleased(e -> button.setTranslateY(0));

        return button;
    }

    private void loadStylesheet(Scene scene) {

        var url = getClass().getResource("/css/style.css");

        if (url != null)
            scene.getStylesheets().add(url.toExternalForm());
    }

    @Override
    public void stop() {

        if (backgroundVideoPlayer != null) {

            backgroundVideoPlayer.stop();
            backgroundVideoPlayer.dispose();
        }

        stopMusic();

        Platform.exit();
    }
}