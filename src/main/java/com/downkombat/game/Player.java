package com.downkombat.game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {

    private Group node;
    private Rectangle sprite;

    private double speed = 5;

    private int health = 100;
    private int attackRange = 180;

    private boolean facingRight = true;

    private static final double WIDTH = 125;
    private static final double HEIGHT = 250;

    private Color originalColor;
    private long flashEndTime = 0;
    private static final int FLASH_DURATION = 120;

    // cooldown ataque
    private long lastAttackTime = 0;
    private static final long ATTACK_COOLDOWN = 350;

    // hitstun
    private long lastHitTime = 0;
    private static final long HITSTUN = 300;

    public Player(double x, double groundY, Color color) {

        node = new Group();

        sprite = new Rectangle(WIDTH, HEIGHT);
        sprite.setFill(color);
        originalColor = color;

        // centramos sprite respecto al punto del jugador
        sprite.setTranslateX(-WIDTH / 2);
        sprite.setTranslateY(-HEIGHT);

        node.getChildren().add(sprite);

        node.setTranslateX(x);
        node.setTranslateY(groundY);
    }

    public Group getNode() {
        return node;
    }

    public double getX() {
        return node.getTranslateX();
    }

    public void setX(double x) {
        node.setTranslateX(x);
    }

    public boolean canAttack() {

        long now = System.currentTimeMillis();
        return now - lastAttackTime >= ATTACK_COOLDOWN;
    }

    public void registerAttack() {
        lastAttackTime = System.currentTimeMillis();
    }

    public void moveLeft() {

        node.setTranslateX(node.getTranslateX() - speed);
        facingRight = false;
        sprite.setScaleX(-1);
    }

    public void moveRight() {

        node.setTranslateX(node.getTranslateX() + speed);
        facingRight = true;
        sprite.setScaleX(1);
    }

    public boolean isFacing(Player other) {

        double otherX = other.getX();

        if (otherX > getX()) {
            return facingRight;
        } else {
            return !facingRight;
        }
    }

    public boolean isNear(Player other) {

        double distance = Math.abs(getX() - other.getX());
        return distance < attackRange;
    }

    public void damage(int amount) {

        long now = System.currentTimeMillis();

        if (now - lastHitTime < HITSTUN) {
            return;
        }

        lastHitTime = now;

        health -= amount;

        if (health < 0) {
            health = 0;
        }

        // activar flash blanco
        sprite.setFill(Color.WHITE);
        flashEndTime = now + FLASH_DURATION;
    }

    // knockback correcto según posición del atacante
    public void applyKnockback(Player attacker, double force) {

        if (attacker.getX() < this.getX()) {
            node.setTranslateX(node.getTranslateX() + force);
        } else {
            node.setTranslateX(node.getTranslateX() - force);
        }
    }

    public void update() {

        long now = System.currentTimeMillis();

        if (flashEndTime > 0 && now > flashEndTime) {
            sprite.setFill(originalColor);
            flashEndTime = 0;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }
}
