package com.downkombat.combat.projectiles;

import com.downkombat.config.GameConfig;
import com.downkombat.fighters.Fighter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Projectile {

    private Circle sprite;

    private boolean movingRight;
    private Fighter owner;

    private boolean active = true;

    private static final double SPEED = 12;

    public Projectile(double x, double y, boolean movingRight, Fighter owner) {

        this.movingRight = movingRight;
        this.owner = owner;

        sprite = new Circle(8);
        sprite.setFill(Color.WHITE);

        sprite.setTranslateX(x);
        sprite.setTranslateY(y);
    }

    public Circle getNode() {
        return sprite;
    }

    public boolean isActive() {
        return active;
    }

    public void update(Fighter enemy) {

        if (!active) return;

        if (movingRight) {
            sprite.setTranslateX(sprite.getTranslateX() + SPEED);
        } else {
            sprite.setTranslateX(sprite.getTranslateX() - SPEED);
        }

        checkCollision(enemy);
    }

    private void checkCollision(Fighter enemy) {

        // ignorar al dueño del proyectil
        if (enemy == owner) {
            return;
        }

        double dx = Math.abs(sprite.getTranslateX() - enemy.getX());

        if (dx < GameConfig.ATTACK_RANGE / 3) {

            enemy.damage(6);
            enemy.applyKnockback(owner, GameConfig.KNOCKBACK_FORCE);

            active = false;
        }
    }
}
