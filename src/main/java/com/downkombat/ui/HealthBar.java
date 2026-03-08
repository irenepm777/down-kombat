package com.downkombat.ui;

import com.downkombat.config.GameConfig;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar {

    private static final double WIDTH = 400;
    private static final double HEIGHT = 30;

    private Rectangle background;
    private Rectangle bar;

    private Group container;

    public HealthBar(double x, double y, Color baseColor) {

        background = new Rectangle(WIDTH, HEIGHT);
        background.setFill(Color.DARKGRAY);

        bar = new Rectangle(WIDTH, HEIGHT);
        bar.setFill(baseColor);

        container = new Group(background, bar);

        container.setTranslateX(x);
        container.setTranslateY(y);
    }

    public Group getNode() {
        return container;
    }

    public void update(int health) {

        double percentage = (double) health / GameConfig.PLAYER_MAX_HEALTH;

        // protección por seguridad
        percentage = Math.max(0, Math.min(percentage, 1));

        bar.setWidth(WIDTH * percentage);

        updateColor(percentage);
    }

    private void updateColor(double percentage) {

        if (percentage > 0.5) {

            bar.setFill(Color.LIMEGREEN);

        } else if (percentage > 0.25) {

            bar.setFill(Color.GOLD);

        } else {

            bar.setFill(Color.RED);
        }
    }
}
