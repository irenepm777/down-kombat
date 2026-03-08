package com.downkombat.fighters;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.config.GameConfig;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Fighter {

    private Group node;
    private Rectangle sprite;

    private double speed = GameConfig.PLAYER_SPEED;

    private int health = GameConfig.PLAYER_MAX_HEALTH;

    private boolean facingRight = true;

    private SpecialAttack normalAttack;
    private SpecialAttack specialAttack;

    private long lastAttackTime = 0;
    private long lastSpecialTime = 0;

    private Color originalColor;
    private long flashEndTime = 0;

    private static final double WIDTH = 125;
    private static final double HEIGHT = 250;

    public Fighter(double x, double groundY, Color color,
                   SpecialAttack normalAttack,
                   SpecialAttack specialAttack) {

        this.normalAttack = normalAttack;
        this.specialAttack = specialAttack;

        node = new Group();

        sprite = new Rectangle(WIDTH, HEIGHT);
        sprite.setFill(color);

        sprite.setTranslateX(-WIDTH / 2);
        sprite.setTranslateY(-HEIGHT);

        node.getChildren().add(sprite);

        node.setTranslateX(x);
        node.setTranslateY(groundY);

        originalColor = color;
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

    public int getHealth() {
        return health;
    }

    public boolean isDead() {
        return health <= 0;
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

    public boolean isFacing(Fighter other) {

        double otherX = other.getX();

        if (otherX > getX()) {
            return facingRight;
        } else {
            return !facingRight;
        }
    }

    public boolean isNear(Fighter other) {

        double distance = Math.abs(getX() - other.getX());

        return distance < GameConfig.ATTACK_RANGE;
    }

    public boolean canAttack() {

        long now = System.currentTimeMillis();

        return now - lastAttackTime >= GameConfig.ATTACK_COOLDOWN;
    }

    public boolean canSpecial() {

        long now = System.currentTimeMillis();

        return now - lastSpecialTime >= GameConfig.SPECIAL_COOLDOWN;
    }

    public void performAttack(Fighter enemy) {

        lastAttackTime = System.currentTimeMillis();

        normalAttack.execute(this, enemy);
    }

    public void performSpecial(Fighter enemy) {

        lastSpecialTime = System.currentTimeMillis();

        specialAttack.execute(this, enemy);
    }

    public void damage(int amount) {

        if (health <= GameConfig.PLAYER_MAX_HEALTH * GameConfig.CRITICAL_HEALTH_THRESHOLD) {
            amount = (int)(amount * 1.5); // golpes más fuertes
        }


        health -= amount;

        if (health < 0) {
            health = 0;
        }

        sprite.setFill(Color.WHITE);

        flashEndTime = System.currentTimeMillis() + GameConfig.DAMAGE_FLASH;
    }

    public void applyKnockback(Fighter attacker, double force) {

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
}
