package com.downkombat.combat.projectiles;

import com.downkombat.config.GameConfig;
import com.downkombat.fighters.Fighter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Car {

    private Rectangle sprite;

    private Fighter target;

    private boolean active = true;

    private static final double SPEED = 18;

    public Car(double startX, double y, Fighter target) {

        this.target = target;

        sprite = new Rectangle(120, 40);
        sprite.setFill(Color.YELLOW);

        sprite.setTranslateX(startX);
        sprite.setTranslateY(y);
    }

    public Rectangle getNode() {
        return sprite;
    }

    public boolean isActive() {
        return active;
    }

    public void update() {

        if (!active) return;

        sprite.setTranslateX(sprite.getTranslateX() + SPEED);

        checkCollision();

        if (sprite.getTranslateX() > GameConfig.WIDTH + 200) {
            active = false;
        }
    }

    private void checkCollision() {

        double dx = Math.abs(sprite.getTranslateX() - target.getX());

        if (dx < 60) {

            target.damage(20);

            target.setColor(Color.GRAY); // sprite atropellado

            active = false;
        }
    }
}
