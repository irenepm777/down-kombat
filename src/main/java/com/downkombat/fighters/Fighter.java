package com.downkombat.fighters;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.config.GameConfig;
import com.downkombat.animation.AnimationState;
import com.downkombat.animation.AnimationPlayer;
import com.downkombat.animation.AnimationLoader;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.List;

public class Fighter {

    private Group node;
    private ImageView sprite;
    private AnimationPlayer animationPlayer;

    private List<Image> idleFrames;
    private List<Image> walkFrames;

    private double speed = GameConfig.PLAYER_SPEED;
    private int health = GameConfig.PLAYER_MAX_HEALTH;

    private AnimationState currentState = AnimationState.IDLE;

    private boolean facingRight = true;

    private SpecialAttack normalAttack;
    private SpecialAttack specialAttack;

    private long lastAttackTime = 0;
    private long lastSpecialTime = 0;

    private Color originalColor;
    private Color currentColor;

    private long flashEndTime = 0;
    private boolean invulnerable = false;

    private static final double SPRITE_HEIGHT = 600;

    public Fighter(double x, double groundY, Color color,
                   SpecialAttack normalAttack,
                   SpecialAttack specialAttack) {

        this.normalAttack = normalAttack;
        this.specialAttack = specialAttack;

        node = new Group();

        // sprite base
        Image image = new Image(
                Fighter.class.getResource("/sprites/fighters/antonio/antonio.png").toExternalForm()
        );

        sprite = new ImageView(image);

        sprite.setSmooth(false);

        sprite.setFitHeight(SPRITE_HEIGHT);
        sprite.setPreserveRatio(true);

        sprite.setTranslateX(-image.getWidth() / 2);
        sprite.setTranslateY(-image.getHeight());

        node.getChildren().add(sprite);

        node.setTranslateX(x);
        node.setTranslateY(groundY);

        if (x > GameConfig.WIDTH / 2) {
            facingRight = false;
            sprite.setScaleX(-1);
        }

        // colores
        originalColor = color;
        currentColor = color;

        animationPlayer = new AnimationPlayer(sprite);

        try {

            idleFrames = AnimationLoader.load(
                    "/sprites/fighters/antonio/idle",
                    4
            );

            walkFrames = AnimationLoader.load(
                    "/sprites/fighters/antonio/walk",
                    4
            );

            animationPlayer.play(idleFrames, 150);

        } catch (Exception e) {

            System.out.println("Animation load failed, using static sprite.");

        }
    }

    public Group getNode() {
        return node;
    }

    public ImageView getSprite() {
        return sprite;
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

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean value) {
        invulnerable = value;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {

        this.facingRight = facingRight;

        if (facingRight) {
            sprite.setScaleX(1);
        } else {
            sprite.setScaleX(-1);
        }
    }

    public void moveLeft() {

        node.setTranslateX(node.getTranslateX() - speed);

        setFacingRight(false);

        setState(AnimationState.WALK);
    }

    public void moveRight() {

        node.setTranslateX(node.getTranslateX() + speed);

        setFacingRight(true);

        setState(AnimationState.WALK);
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

        if (invulnerable) return;

        if (health <= GameConfig.PLAYER_MAX_HEALTH * GameConfig.CRITICAL_HEALTH_THRESHOLD) {
            amount = (int) (amount * 1.5);
        }

        health -= amount;

        if (health < 0) health = 0;

        flashEndTime = System.currentTimeMillis() + GameConfig.DAMAGE_FLASH;
    }

    public void applyKnockback(Fighter attacker, double force) {

        if (attacker.getX() < this.getX()) {
            node.setTranslateX(node.getTranslateX() + force);
        } else {
            node.setTranslateX(node.getTranslateX() - force);
        }
    }

    public void setState(AnimationState newState) {

        if (currentState == newState) return;

        currentState = newState;

        switch (newState) {

            case IDLE:
                animationPlayer.play(idleFrames, 150);
                break;

            case WALK:
                animationPlayer.play(walkFrames, 120);
                break;
        }
    }

    public AnimationState getState() {
        return currentState;
    }

    public void setColor(Color color) {
        currentColor = color;
    }

    public void resetColor() {
        currentColor = originalColor;
    }

    public Color getColor() {
        return currentColor;
    }

    public Color getOriginalColor() {
        return originalColor;
    }

    public void update() {

        long now = System.currentTimeMillis();

        if (flashEndTime > 0 && now > flashEndTime) {
            flashEndTime = 0;
        }

        if (specialAttack != null) {
            specialAttack.update(this);
        }

        if (animationPlayer != null) {
            animationPlayer.update();
        }

        if (currentState == AnimationState.WALK) {
            setState(AnimationState.IDLE);
        }
    }
}
