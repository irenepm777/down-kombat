package com.downkombat.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {

    private Rectangle body;
    private double speed = 5;
    private int health = 100;

    public Player(double x, double y, Color color) {

        body = new Rectangle(50, 80);
        body.setFill(color);

        body.setTranslateX(x);
        body.setTranslateY(y);
    }

    public Rectangle getBody() {
        return body;
    }

    public void moveLeft() {
        body.setTranslateX(body.getTranslateX() - speed);
    }

    public void moveRight() {
        body.setTranslateX(body.getTranslateX() + speed);
    }

    public void damage(int amount) {
        health -= amount;
    }

    public int getHealth() {
        return health;
    }
}
