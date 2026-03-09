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
    private static final double HITBOX = 20; // tamaño de colisión real

    public Projectile(double x, double y, boolean movingRight, Fighter owner) {

        this.movingRight = movingRight;
        this.owner = owner;

        sprite = new Circle(12);
        sprite.setFill(Color.WHITE);
        sprite.setStroke(Color.BLACK);
        sprite.setStrokeWidth(2);

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

        // mover proyectil
        if (movingRight) {
            sprite.setTranslateX(sprite.getTranslateX() + SPEED);
        } else {
            sprite.setTranslateX(sprite.getTranslateX() - SPEED);
        }

        checkCollision(enemy);

        // destruir si sale de la pantalla
        if (sprite.getTranslateX() < -50 || sprite.getTranslateX() > GameConfig.WIDTH + 50) {
            active = false;
        }
    }

    private void checkCollision(Fighter enemy) {

        // ignorar al dueño del proyectil
        if (enemy == owner) {
            return;
        }

        double dx = Math.abs(sprite.getTranslateX() - enemy.getX());

        if (dx < HITBOX) {

            enemy.damage(GameConfig.MOLAR_SPECIAL_DAMAGE / 6);
            enemy.applyKnockback(owner, GameConfig.KNOCKBACK_FORCE);

            active = false;
        }
    }
}
