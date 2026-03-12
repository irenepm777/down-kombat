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

/**
 * Main menu application.
 * Background uses a looping video if available; falls back to an image or gradient.
 * Buttons and title are styled via CSS (or inline fallback if CSS is missing).
 *
 * Behavior added:
 * - When Tutorial button is pressed: pause background video and music, open Tutorial.
 * - When the primary stage is shown again (Tutorial closed), resume video and music.
 */
public class MenuInicio extends Application {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    // Music player for background music
    private static MediaPlayer player;

    // MediaPlayer used for the background video
    private MediaPlayer backgroundVideoPlayer;

    // Reference to the primary stage so handlers can show/hide it
    private Stage primaryStageRef;

    @Override
    public void start(Stage primaryStage) {
        // Save primary stage reference for handlers
        this.primaryStageRef = primaryStage;

        StackPane root = new StackPane();

        // Quick test to ensure audio resource is accessible (silent test)
        musicTest();

        // ========== LAYER 1: BACKGROUND ==========
        // Try to load the background video first. If it exists, it will be used.
        MediaView bgVideo = loadBackgroundVideo();
        if (bgVideo != null) {
            // Add the MediaView first so it stays behind UI elements
            root.getChildren().add(bgVideo);
        } else {
            // If video not available, try image fallback; otherwise use gradient
            ImageView bgImage = loadBackgroundImage();
            if (bgImage != null) {
                root.getChildren().add(bgImage);
            } else {
                root.getChildren().add(createArcadeBackground());
            }
        }

        // ========== LAYER 2: MENU UI ==========
        VBox menuBox = createMenuBox();
        Text gameTitle = createGameTitle();
        Button startButton = createMenuButton("START", "start-button");
        Button tutorialButton = createMenuButton("TUTORIAL", "tutorial-button");

        // Attach handlers
        startButton.setOnAction(e -> handleStart());
        tutorialButton.setOnAction(e -> handleTutorial());

        menuBox.getChildren().addAll(gameTitle, startButton, tutorialButton);
        root.getChildren().add(menuBox);

        // Create scene with fixed size to avoid scaling issues
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        // If a MediaView was created, bind its size to the scene so it covers the whole window
        if (bgVideo != null) {
            bgVideo.fitWidthProperty().bind(scene.widthProperty());
            bgVideo.fitHeightProperty().bind(scene.heightProperty());
            bgVideo.setPreserveRatio(false); // false to fill the whole area
            bgVideo.setSmooth(true);
        }

        // Load CSS stylesheet (if present)
        loadStylesheet(scene);

        primaryStage.setTitle("DAWN KOMBAT - MAIN MENU");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Start background music (if available)
        playBackgroundMusic();
    }

    /**
     * Load the background video from resources/video/fondoInicio.mp4.
     * Returns a MediaView if successful, or null if the resource is missing or fails.
     * The MediaPlayer is configured to loop indefinitely.
     */
    private MediaView loadBackgroundVideo() {
        try {
            var url = getClass().getResource("/video/fondoInicio.mp4");
            if (url == null) {
                System.err.println("No resource found: /video/fondoInicio.mp4");
                return null;
            }

            Media media = new Media(url.toExternalForm());
            backgroundVideoPlayer = new MediaPlayer(media);

            // Log errors for debugging
            backgroundVideoPlayer.setOnError(() -> System.err.println("MediaPlayer error: " + backgroundVideoPlayer.getError()));
            media.setOnError(() -> System.err.println("Media error: " + media.getError()));

            // Ensure looping: set cycle count and also seek to start on end as a fallback
            backgroundVideoPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundVideoPlayer.setOnEndOfMedia(() -> backgroundVideoPlayer.seek(Duration.ZERO));

            // When the media is ready, start playback
            backgroundVideoPlayer.setOnReady(() -> {
                backgroundVideoPlayer.setVolume(0.0); // muted by default; change if you want audio
                backgroundVideoPlayer.play();
                System.out.println("Background video ready and playing (looping)");
            });

            MediaView mediaView = new MediaView(backgroundVideoPlayer);
            mediaView.setPreserveRatio(false);
            mediaView.setSmooth(true);

            return mediaView;

        } catch (MediaException me) {
            System.err.println("MediaException while creating Media/MediaPlayer: " + me.getMessage());
            return null;
        } catch (Exception ex) {
            System.err.println("Exception while loading video: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Load an image fallback from resources/images/fondo.png.
     * Returns an ImageView sized to the fixed screen dimensions, or null if missing.
     */
    private ImageView loadBackgroundImage() {
        try {
            var stream = getClass().getResourceAsStream("/images/fondo.png");
            if (stream == null) {
                System.err.println("No resource found: /images/fondo.png");
                return null;
            }

            Image bg = new Image(stream);
            if (bg.isError()) {
                System.err.println("Error loading image resource");
                return null;
            }

            ImageView bgImage = new ImageView(bg);
            bgImage.setFitWidth(SCREEN_WIDTH);
            bgImage.setFitHeight(SCREEN_HEIGHT);
            bgImage.setPreserveRatio(false);
            return bgImage;

        } catch (Exception e) {
            System.err.println("Exception while loading background image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Create a gradient rectangle as a last-resort background.
     */
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

    /**
     * Play background music from resources/sounds/Menu_Inicio.mp3.
     * Stops any previous music player before starting a new one.
     */
    private void playBackgroundMusic() {
        try {
            stopMusic();

            var url = getClass().getResource("/sounds/Menu_Inicio.mp3");
            if (url == null) {
                System.err.println("No resource found: /sounds/Menu_Inicio.mp3");
                return;
            }

            Media media = new Media(url.toExternalForm());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(1.0);
            player.play();

            System.out.println("Background music started");

        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    /**
     * Pause background video and music.
     * Called before opening the Tutorial screen.
     */
    private void pauseBackgroundMedia() {
        try {
            if (backgroundVideoPlayer != null) {
                backgroundVideoPlayer.pause();
            }
            if (player != null) {
                player.pause();
            }
        } catch (Exception e) {
            System.err.println("Error pausing media: " + e.getMessage());
        }
    }

    /**
     * Resume background video and music.
     * Called when the primary stage becomes visible again.
     */
    private void resumeBackgroundMedia() {
        try {
            if (backgroundVideoPlayer != null) {
                backgroundVideoPlayer.play();
            }
            if (player != null) {
                player.play();
            }
        } catch (Exception e) {
            System.err.println("Error resuming media: " + e.getMessage());
        }
    }

    /**
     * Create the menu container with spacing and a subtle glow effect.
     */
    private VBox createMenuBox() {
        VBox menuBox = new VBox(40);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateY(100); // move down so it doesn't overlap the title
        menuBox.setEffect(new Glow(0.2));
        return menuBox;
    }

    /**
     * Create the game title text. The font is loaded from resources if available.
     * The title receives a drop shadow and gold fill by default; CSS can override this.
     */
    private Text createGameTitle() {
        Text title = new Text("DOWN\nKOMBAT");
        title.setId("game-title");

        try {
            Font arcadeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 64);
            if (arcadeFont != null) {
                title.setFont(arcadeFont);
            } else {
                title.setFont(Font.font("Monospace", FontWeight.BOLD, 64));
            }
        } catch (Exception e) {
            title.setFont(Font.font("Monospace", FontWeight.BOLD, 64));
            System.err.println("Error loading arcade font: " + e.getMessage());
        }

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.ORANGE);
        shadow.setRadius(15);
        shadow.setSpread(0.6);
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        title.setEffect(shadow);
        title.setFill(Color.GOLD);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        return title;
    }

    /**
     * Create a menu button. The button keeps its behavior (hover scale, press translate)
     * while allowing CSS to style appearance via the "arcade-button" class and the id.
     */
    private Button createMenuButton(String text, String styleId) {
        Button button = new Button(text);
        button.setId(styleId);
        button.getStyleClass().add("arcade-button");
        button.setMinWidth(300);
        button.setMinHeight(70);

        // Hover effect: scale and glow (kept as requested)
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.15);
            button.setScaleY(1.15);
            button.setEffect(new Glow(0.8));
        });

        // Remove hover effect on exit
        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            button.setEffect(null);
        });

        // Press visual feedback
        button.setOnMousePressed(e -> button.setTranslateY(2));
        button.setOnMouseReleased(e -> button.setTranslateY(0));

        return button;
    }

    /**
     * Silent test to verify the audio resource is accessible.
     * Plays the audio at zero volume and immediately stops it.
     */
    private void musicTest() {
        try {
            var url = getClass().getResource("/sounds/Menu_Inicio.mp3");
            if (url == null) {
                System.err.println("musicTest: resource not found /sounds/Menu_Inicio.mp3");
                return;
            }
            Media media = new Media(url.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.0);
            mediaPlayer.play();
            mediaPlayer.stop();
            mediaPlayer.dispose();
            System.out.println("musicTest: audio resource accessible");
        } catch (Exception e) {
            System.err.println("Error in musicTest: " + e.getMessage());
        }
    }

    /**
     * Load the CSS stylesheet from resources/css/style.css if present.
     * If the stylesheet is missing, the UI will still use inline defaults and effects.
     */
    private void loadStylesheet(Scene scene) {
        try {
            var url = getClass().getResource("/css/style.css");
            if (url == null) {
                System.err.println("CSS not found at /css/style.css");
                return;
            }
            scene.getStylesheets().add(url.toExternalForm());
            System.out.println("CSS loaded: " + url.toExternalForm());
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
        }
    }

    /**
     * Handler to open the tutorial screen.
     * Pauses background media, opens Tutorial, and sets a one-time onShown handler
     * to resume media when the primary stage is shown again.
     */
    private void handleTutorial() {
        System.out.println("Opening Tutorial: pausing background media and launching tutorial screen");

        // Pause background video and music
        pauseBackgroundMedia();

        // Set a one-time onShown handler to resume media when the primary stage is shown again
        primaryStageRef.setOnShown(event -> {
            // Resume media
            resumeBackgroundMedia();
            // Remove this handler so it doesn't run repeatedly
            primaryStageRef.setOnShown(null);
        });

        // Open tutorial and pass the primary stage reference
        com.downkombat.tutorial.Tutorial tutorial = new com.downkombat.tutorial.Tutorial();
        tutorial.mostrar(this.primaryStageRef);
    }

    /**
     * Handler for the Start button. Replace with actual game start logic.
     */
    private void handleStart() {
        System.out.println("GAME STARTED!");
    }

    public static MediaPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(MediaPlayer player) {
        MenuInicio.player = player;
    }

    /**
     * Stop and dispose background video and music when the application stops.
     */
    @Override
    public void stop() {
        if (backgroundVideoPlayer != null) {
            try {
                backgroundVideoPlayer.stop();
                backgroundVideoPlayer.dispose();
            } catch (Exception e) {
                System.err.println("Error releasing background video: " + e.getMessage());
            } finally {
                backgroundVideoPlayer = null;
            }
        }
        stopMusic();
        Platform.exit();
    }

    private void stopMusic() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
        launch(args);
    }
}