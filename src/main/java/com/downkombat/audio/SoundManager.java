package com.downkombat.audio;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    private static MediaPlayer player;

    public static void play(String path) {

        Platform.runLater(() -> { // Ensure this runs on the JavaFX Application Thread

            try {

                stop();

                System.out.println("Loading sound: " + path);

                URL resource = SoundManager.class.getResource(path);

                if (resource == null) {
                    System.out.println("ERROR: Sound not found -> " + path);
                    return;
                }

                String uri = resource.toExternalForm();

                System.out.println("Resolved URI: " + uri);

                Media media = new Media(uri);

                player = new MediaPlayer(media);

                player.setVolume(1.0);

                player.setOnReady(() -> {

                    System.out.println("AUDIO READY → PLAYING");

                    player.play();
                });

                player.setOnError(() -> {

                    System.out.println("MEDIA ERROR:");
                    System.out.println(player.getError());
                });

            } catch (Exception e) {

                System.out.println("SOUND EXCEPTION:");
                e.printStackTrace();
            }

        });
    }

    public static void pause() {

        if (player != null) {
            player.pause();
        }
    }

    public static void stop() {

        if (player != null) {

            player.stop();
            player.dispose();
            player = null;
        }
    }
}