package com.downkombat.tutorial;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Tutorial screen.
 * - Uses an image background if available, otherwise a simple fallback background.
 * - Provides a visible "back" button (Unicode arrow) that closes the tutorial and shows the previous stage.
 * - Loads CSS safely (no exception if stylesheet missing).
 */
public class Tutorial {

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
                // We will bind fitWidth/fitHeight to the scene later
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
            // Optional: add a small explanatory text so user knows tutorial loaded
            Text fallbackText = new Text("Tutorial (image not found)");
            fallbackText.setFill(Color.LIGHTGRAY);
            StackPane.setAlignment(fallbackText, Pos.CENTER);
            root.getChildren().add(fallbackText);
        }

        // Create the back button using a Unicode arrow (no external asset required)
        Button btnMain = new Button("←");
        btnMain.getStyleClass().add("btn-volver-pixel-main"); // CSS class (optional)
        btnMain.setFocusTraversable(false);

        // Shadow button for pixel effect (mouseTransparent so it doesn't block clicks)
        Button btnShadow = new Button("←");
        btnShadow.getStyleClass().add("btn-volver-pixel-shadow");
        btnShadow.setMouseTransparent(true);
        btnShadow.setTranslateX(3);
        btnShadow.setTranslateY(3);

        // Stack the shadow and main button so it looks "pixelated"
        VBox arrowBox = new VBox();
        arrowBox.setAlignment(Pos.TOP_LEFT);
        arrowBox.setTranslateX(20);
        arrowBox.setTranslateY(20);
        StackPane arrowStack = new StackPane(btnShadow, btnMain);
        arrowStack.setAlignment(Pos.TOP_LEFT);
        arrowBox.getChildren().add(arrowStack);

        // Add the arrow stack on top of the background
        root.getChildren().add(arrowBox);
        StackPane.setAlignment(arrowBox, Pos.TOP_LEFT);

        // Button action: show previous stage and close this tutorial stage
        btnMain.setOnAction(e -> {
            try {
                // Show the previous stage (MenuInicio). This will trigger any onShown handlers there.
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
            var cssUrl = getClass().getResource("/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Tutorial stylesheet not found at /css/styles.css (optional).");
            }
        } catch (Exception ex) {
            System.err.println("Error loading tutorial stylesheet: " + ex.getMessage());
        }

        // Create and show tutorial stage
        Stage stage = new Stage();
        stage.setTitle("Tutorial");
        stage.setScene(scene);

        // Hide the previous stage before showing tutorial so background media is not visible
        // (MenuInicio should have paused media already; if not, ensure it does before calling mostrar)
        stageAnterior.hide();

        stage.show();
    }
}