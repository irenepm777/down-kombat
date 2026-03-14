package com.downkombat.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static MediaPlayer player;

    public static void play(String path) {

        try {

            Media media = new Media(
                    SoundManager.class.getResource(path).toExternalForm()
            );

            player = new MediaPlayer(media);

            player.play();

        } catch (Exception e) {
            System.out.println("Sound failed: " + path);
        }
    }

    public static void stop() {

        if (player != null) {
            player.stop();
        }
    }
}