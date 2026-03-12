package com.downkombat.tutorial;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

/**
 * Tutorial screen.
 * - Uses an image background if available, otherwise a simple fallback background.
 * - The back button uses the same visual style and interactive behavior as the menu buttons
 *   in MenuInicio (class "arcade-button" and id "tutorial-button"), so CSS rules apply consistently.
 * - Plays a looping tutorial background music while the tutorial is open.
 * - Stops and disposes the tutorial music when the tutorial closes, then shows the previous stage.
 */
public class Tutorial {

    /**
     * Show the tutorial stage.
     * Pauses/resumes of MenuInicio media should be handled by the caller (MenuInicio).
     */
    public void mostrar(Stage stageAnterior) {
        // Defensive: if caller passed null, avoid NPE and return
        if (stageAnterior == null) {
            System.err.println("Tutorial.mostrar: stageAnterior is null. Aborting.");
            return;
        }

        // Root pane for tutorial
        StackPane root = new StackPane();

        // Try to load tutorial image resource; if missing, use a fallback rectangle
        ImageView imageView = null;
        try {
            var url = getClass().getResource("/images/tutorialr.png");
            if (url != null) {
                Image img = new Image(url.toExternalForm(), true);
                imageView = new ImageView(img);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                // fit binding will be applied after scene creation
            } else {
                System.err.println("Tutorial image not found at /images/tutorialr.png — using fallback background.");
            }
        } catch (Exception ex) {
            System.err.println("Error loading tutorial image: " + ex.getMessage());
            imageView = null;
        }

        // If image loaded, add it as background; otherwise add a simple colored rectangle
        if (imageView != null) {
            root.getChildren().add(imageView);
        } else {
            Rectangle fallback = new Rectangle(1280, 720, Color.web("#0b1220")); // dark fallback
            root.getChildren().add(fallback);
            Text fallbackText = new Text("Tutorial (image not found)");
            fallbackText.setFill(Color.LIGHTGRAY);
            StackPane.setAlignment(fallbackText, Pos.CENTER);
            root.getChildren().add(fallbackText);
        }

        // Tutorial music player (local variable so we can stop/dispose it on close)
        final MediaPlayer[] tutorialPlayer = new MediaPlayer[1];

        // Attempt to load tutorial music from resources/sounds/tutorial_music.mp3
        try {
            var musicUrl = getClass().getResource("/sounds/tutorial.mp3");
            if (musicUrl != null) {
                Media media = new Media(musicUrl.toExternalForm());
                tutorialPlayer[0] = new MediaPlayer(media);
                tutorialPlayer[0].setCycleCount(MediaPlayer.INDEFINITE); // loop
                tutorialPlayer[0].setVolume(0.5); // adjust default volume as desired
                // Start playback when ready
                tutorialPlayer[0].setOnReady(() -> {
                    try {
                        tutorialPlayer[0].play();
                        System.out.println("Tutorial music started");
                    } catch (Exception e) {
                        System.err.println("Error playing tutorial music: " + e.getMessage());
                    }
                });
                // Log errors for debugging
                tutorialPlayer[0].setOnError(() -> System.err.println("Tutorial MediaPlayer error: " + tutorialPlayer[0].getError()));
                media.setOnError(() -> System.err.println("Tutorial Media error: " + media.getError()));
            } else {
                System.err.println("Tutorial music not found at /sounds/tutorialmusic.mp3 (optional).");
            }
        } catch (MediaException me) {
            System.err.println("MediaException loading tutorial music: " + me.getMessage());
        } catch (Exception ex) {
            System.err.println("Exception loading tutorial music: " + ex.getMessage());
        }

        // Create the back button using the same style as MenuInicio buttons
        Button btnMain = new Button("←");
        // Match MenuInicio: class "arcade-button" and id "tutorial-button"
        btnMain.getStyleClass().add("arcade-button");
        btnMain.setId("tutorial-button");
        btnMain.setMinWidth(300);
        btnMain.setMinHeight(70);
        btnMain.setFocusTraversable(false);

        // Shadow button for pixel effect (mouseTransparent so it doesn't block clicks)
        Button btnShadow = new Button("←");
        btnShadow.getStyleClass().add("arcade-button");
        btnShadow.getStyleClass().add("tutorial-button"); // keep consistent classes for CSS if needed
        btnShadow.setMouseTransparent(true);
        btnShadow.setTranslateX(3);
        btnShadow.setTranslateY(3);

        // Preserve the same interactive behavior as MenuInicio buttons:
        // hover: scale + glow; press: translateY
        btnMain.setOnMouseEntered(e -> {
            btnMain.setScaleX(1.15);
            btnMain.setScaleY(1.15);
            btnMain.setEffect(new javafx.scene.effect.Glow(0.8));
        });

        btnMain.setOnMouseExited(e -> {
            btnMain.setScaleX(1.0);
            btnMain.setScaleY(1.0);
            btnMain.setEffect(null);
        });

        btnMain.setOnMousePressed(e -> btnMain.setTranslateY(2));
        btnMain.setOnMouseReleased(e -> btnMain.setTranslateY(0));

        // Stack the shadow and main button so it looks "pixelated"
        StackPane arrowStack = new StackPane(btnShadow, btnMain);
        arrowStack.setAlignment(Pos.TOP_LEFT);
        arrowStack.setTranslateX(20);
        arrowStack.setTranslateY(20);

        // Add the arrow stack on top of the background
        root.getChildren().add(arrowStack);
        StackPane.setAlignment(arrowStack, Pos.TOP_LEFT);

        // Button action: stop tutorial music, show previous stage and close this tutorial stage
        btnMain.setOnAction(e -> {
            try {
                // Stop and dispose tutorial music if playing
                if (tutorialPlayer[0] != null) {
                    try {
                        tutorialPlayer[0].stop();
                        tutorialPlayer[0].dispose();
                    } catch (Exception ex) {
                        System.err.println("Error stopping tutorial music: " + ex.getMessage());
                    } finally {
                        tutorialPlayer[0] = null;
                    }
                }

                // Show the previous stage (MenuInicio). MenuInicio should resume its media on shown.
                stageAnterior.show();

                // Close the tutorial window
                Stage current = (Stage) btnMain.getScene().getWindow();
                current.close();
            } catch (Exception ex) {
                System.err.println("Error closing tutorial: " + ex.getMessage());
            }
        });

        // Create scene and bind image size if imageView exists
        Scene scene = new Scene(root, 1280, 720);

        if (imageView != null) {
            imageView.fitWidthProperty().bind(scene.widthProperty());
            imageView.fitHeightProperty().bind(scene.heightProperty());
        }

        // Load stylesheet safely (no exception if missing)
        try {
            var cssUrl = getClass().getResource("/css/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Tutorial stylesheet not found at /css/style.css (optional).");
            }
        } catch (Exception ex) {
            System.err.println("Error loading tutorial stylesheet: " + ex.getMessage());
        }

        // Create and show tutorial stage
        Stage stage = new Stage();
        stage.setTitle("Tutorial");
        stage.setScene(scene);

        // Ensure tutorial music is stopped/disposed if the user closes the window via the window manager
        stage.setOnCloseRequest(ev -> {
            if (tutorialPlayer[0] != null) {
                try {
                    tutorialPlayer[0].stop();
                    tutorialPlayer[0].dispose();
                } catch (Exception ex) {
                    System.err.println("Error disposing tutorial music on close: " + ex.getMessage());
                } finally {
                    tutorialPlayer[0] = null;
                }
            }
            // Show the previous stage so MenuInicio can resume its media
            try {
                stageAnterior.show();
            } catch (Exception ex) {
                // ignore
            }
        });

        // Hide the previous stage before showing tutorial (MenuInicio should have paused media already)
        stageAnterior.hide();

        stage.show();
    }
}