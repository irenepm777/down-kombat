package com.downkombat.combat.projectiles;

import com.downkombat.config.GameConfig;
import com.downkombat.fighters.Fighter;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile {

    private final Group node;
    private final ImageView sprite;

    private final boolean movingRight;
    private final Fighter owner;

    private boolean active = true;

    private static final double SPEED = 12;
    private static final double HITBOX = 20;

    public Projectile(double x, double y, boolean movingRight, Fighter owner) {

        this.movingRight = movingRight;
        this.owner = owner;

        node = new Group();

        Image image = new Image(
                Projectile.class.getResource(
                        "/sprites/projectiles/molar.png"
                ).toExternalForm()
        );

        sprite = new ImageView(image);
        sprite.setFitWidth(40);
        sprite.setPreserveRatio(true);

        if (!movingRight) {
            sprite.setScaleX(-1);
        }

        node.getChildren().add(sprite);

        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    public Group getNode() {
        return node;
    }

    public boolean isActive() {
        return active;
    }

    public void update(Fighter enemy) {

        if (!active) return;

        if (movingRight) {
            node.setTranslateX(node.getTranslateX() + SPEED);
        } else {
            node.setTranslateX(node.getTranslateX() - SPEED);
        }

        checkCollision(enemy);

        if (node.getTranslateX() < -50 || node.getTranslateX() > GameConfig.WIDTH + 50) {
            active = false;
        }
    }

    private void checkCollision(Fighter enemy) {

        if (enemy == owner) return;

        double dx = Math.abs(node.getTranslateX() - enemy.getX());

        if (dx < HITBOX) {

            // aplicar daño del proyectil
            enemy.damage(GameConfig.MOLAR_SPECIAL_DAMAGE / 6);

            // desactivar proyectil tras impactar
            active = false;
        }
    }
}