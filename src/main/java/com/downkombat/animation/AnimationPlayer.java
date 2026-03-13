package com.downkombat.animation;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class AnimationPlayer {

    private final ImageView sprite;

    private List<Image> frames;

    private int currentFrame = 0;

    private long frameDuration = 100;
    private long lastFrameTime = 0;

    public AnimationPlayer(ImageView sprite) {
        this.sprite = sprite;
    }

    public void play(List<Image> frames, long frameDuration) {

        if (frames == null || frames.isEmpty()) return;

        this.frames = frames;
        this.frameDuration = frameDuration;

        currentFrame = 0;
        sprite.setImage(frames.get(0));
    }

    public void update() {

        if (frames == null) return;

        long now = System.currentTimeMillis();

        if (now - lastFrameTime >= frameDuration) {

            currentFrame++;

            if (currentFrame >= frames.size()) {
                currentFrame = 0;
            }

            sprite.setImage(frames.get(currentFrame));

            lastFrameTime = now;
        }
    }
}
