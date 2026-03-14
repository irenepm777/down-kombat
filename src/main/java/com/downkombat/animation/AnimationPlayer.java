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

    private boolean loop = true;

    public AnimationPlayer(ImageView sprite) {
        this.sprite = sprite;
    }

    public void play(List<Image> frames, long frameDuration) {
        play(frames, frameDuration, true);
    }

    public void play(List<Image> frames, long frameDuration, boolean loop) {

        if (frames == null || frames.isEmpty()) return;

        this.frames = frames;
        this.frameDuration = frameDuration;
        this.loop = loop;

        currentFrame = 0;
        lastFrameTime = System.currentTimeMillis();

        sprite.setImage(frames.get(0));
    }

    public void update() {

        if (frames == null || frames.isEmpty()) return;

        long now = System.currentTimeMillis();

        if (now - lastFrameTime >= frameDuration) {

            currentFrame++;

            if (currentFrame >= frames.size()) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.size() - 1;
                }
            }

            sprite.setImage(frames.get(currentFrame));
            lastFrameTime = now;
        }
    }
}