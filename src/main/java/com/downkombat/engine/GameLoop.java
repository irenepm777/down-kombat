package com.downkombat.engine;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

    private Runnable update;

    public GameLoop(Runnable update) {
        this.update = update;
    }

    @Override
    public void handle(long now) {
        update.run();
    }
}
