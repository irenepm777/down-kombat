package com.downkombat.ui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar {

    private Rectangle background;
    private Rectangle bar;

    private static final double WIDTH = 400;
    private static final double HEIGHT = 30;

    public HealthBar(double x, double y, Color color) {

        background = new Rectangle(WIDTH, HEIGHT);
        background.setFill(Color.DARKGRAY);

        bar = new Rectangle(WIDTH, HEIGHT);
        bar.setFill(color);

        Group group = new Group(background, bar);

        group.setTranslateX(x);
        group.setTranslateY(y);

        container = group;
    }

    private Group container;

    public Group getNode() {
        return container;
    }

    public void update(int health) {

        double percentage = health / 100.0;

        bar.setWidth(WIDTH * percentage);
    }
}
