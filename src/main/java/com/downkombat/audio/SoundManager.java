package com.downkombat.audio;

import javafx.scene.media.AudioClip;

public class SoundManager {

    public static void play(String path) {

        AudioClip sound = new AudioClip(
                SoundManager.class.getResource(path).toExternalForm()
        );

        sound.play();
    }

}
