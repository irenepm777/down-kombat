package com.downkombat.combat.projectiles;

import com.downkombat.config.GameConfig;
import com.downkombat.fighters.Fighter;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Car {

    private final Group node;
    private final ImageView sprite;

    private final boolean movingRight;
    private final Fighter juanma;

    private boolean active = true;
    private boolean collisionTriggered = false;

    private static final double SPEED = 12;
    private static final int DAMAGE = 40;

    public Car(double x, double y, boolean movingRight, Fighter juanma) {

        this.movingRight = movingRight;
        this.juanma = juanma;

        node = new Group();

        Image image = new Image(
                Car.class.getResource("/sprites/projectiles/uber.png").toExternalForm()
        );

        sprite = new ImageView(image);

        // tamaño del Uber
        sprite.setFitWidth(750);
        sprite.setPreserveRatio(true);

        // ajustar posición visual del sprite
        sprite.setTranslateX(-120);
        sprite.setTranslateY(-80);

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

    public void update() {

        if (!active) return;

        // mover coche
        if (movingRight) {
            node.setTranslateX(node.getTranslateX() + SPEED);
        } else {
            node.setTranslateX(node.getTranslateX() - SPEED);
        }

        // detectar atropello
        if (!collisionTriggered) {

            double carCenter = node.getTranslateX() + 110;
            double dx = Math.abs(carCenter - juanma.getX());

            if (dx < 70) {

                collisionTriggered = true;

                System.out.println("JUANMA ATROPELLADO");

                // activar animación especial de Juanma
                juanma.triggerSpecialAnimation();

                // aplicar daño
                juanma.damage(DAMAGE);
            }
        }

        // eliminar coche fuera de pantalla
        if (node.getTranslateX() < -400 ||
            node.getTranslateX() > GameConfig.WIDTH + 400) {

            active = false;
        }
    }
}