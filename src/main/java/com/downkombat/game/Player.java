package com.downkombat.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {

    private Rectangle body;
    private double speed = 5;
    private int health = 100;
    private int attackRange = 180;

    public Player(double x, double y, Color color) {

        body = new Rectangle(125, 400);
        body.setFill(color);

        body.setTranslateX(x);
        body.setTranslateY(720 - 420);
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

        if (health < 0) {
            health = 0;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public boolean isNear(Player other) {

        double distance = Math.abs(
            this.body.getTranslateX() - other.body.getTranslateX()
        );

        return distance < attackRange;
    }
}
