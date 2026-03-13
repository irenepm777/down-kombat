package com.downkombat.fighters;

import com.downkombat.animation.AnimationLoader;
import com.downkombat.animation.AnimationPlayer;
import com.downkombat.animation.AnimationState;
import com.downkombat.combat.SpecialAttack;
import com.downkombat.config.GameConfig;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.List;

public class Fighter {

    private String fighterId;

    private Group node;
    private ImageView sprite;
    private AnimationPlayer animationPlayer;

    private List<Image> idleFrames;
    private List<Image> walkFrames;
    private List<Image> attackFrames;
    private List<Image> hitFrames;

    private double speed = GameConfig.PLAYER_SPEED;
    private int health = GameConfig.PLAYER_MAX_HEALTH;

    private AnimationState currentState = AnimationState.IDLE;

    private boolean facingRight = true;
    private boolean invulnerable = false;
    private boolean movedThisFrame = false;

    private SpecialAttack normalAttack;
    private SpecialAttack specialAttack;

    private long lastAttackTime = 0;
    private long lastSpecialTime = 0;
    private long flashEndTime = 0;

    private long attackStateEndTime = 0;
    private long hitStateEndTime = 0;

    private Color originalColor;
    private Color currentColor;

    private static final double SPRITE_HEIGHT = 600;
    private static final long ATTACK_ANIMATION_DURATION = 220;
    private static final long HIT_ANIMATION_DURATION = 180;

    public Fighter(
            String fighterId,
            double x,
            double groundY,
            Color color,
            SpecialAttack normalAttack,
            SpecialAttack specialAttack
    ) {
        this.fighterId = fighterId;
        this.normalAttack = normalAttack;
        this.specialAttack = specialAttack;

        node = new Group();

        Image image = new Image(
                Fighter.class.getResource(
                        "/sprites/fighters/" + fighterId + "/idle/idle_1.png"
                ).toExternalForm()
        );

        sprite = new ImageView(image);
        sprite.setSmooth(false);
        sprite.setCache(true);
        sprite.setFitHeight(SPRITE_HEIGHT);
        sprite.setPreserveRatio(true);

        sprite.setTranslateX(-sprite.getFitWidth() / 2);
        sprite.setTranslateY(-SPRITE_HEIGHT);

        node.getChildren().add(sprite);

        node.setTranslateX(x);
        node.setTranslateY(groundY);

        if (x > GameConfig.WIDTH / 2) {
            facingRight = false;
            sprite.setScaleX(-1);
        }

        originalColor = color;
        currentColor = color;

        animationPlayer = new AnimationPlayer(sprite);

        try {
            var animations = AnimationLoader.load(
                    "/sprites/fighters/" + fighterId
            );

            idleFrames = animations.get(AnimationState.IDLE);
            walkFrames = animations.get(AnimationState.WALK);
            attackFrames = animations.get(AnimationState.ATTACK);
            hitFrames = animations.get(AnimationState.HIT);

            if (idleFrames != null && !idleFrames.isEmpty()) {
                animationPlayer.play(idleFrames, 150);
            }
        } catch (Exception e) {
            System.out.println("Animation load failed for " + fighterId);
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
        movedThisFrame = true;
        setFacingRight(false);
    }

    public void moveRight() {
        node.setTranslateX(node.getTranslateX() + speed);
        movedThisFrame = true;
        setFacingRight(true);
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

        attackStateEndTime = System.currentTimeMillis() + ATTACK_ANIMATION_DURATION;
        setState(AnimationState.ATTACK);
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
        hitStateEndTime = System.currentTimeMillis() + HIT_ANIMATION_DURATION;
        setState(AnimationState.HIT);
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
                if (idleFrames != null && !idleFrames.isEmpty()) {
                    animationPlayer.play(idleFrames, 150);
                }
                break;

            case WALK:
                if (walkFrames != null && !walkFrames.isEmpty()) {
                    animationPlayer.play(walkFrames, 120);
                }
                break;

            case ATTACK:
                if (attackFrames != null && !attackFrames.isEmpty()) {
                    animationPlayer.play(attackFrames, 80);
                }
                break;

            case HIT:
                if (hitFrames != null && !hitFrames.isEmpty()) {
                    animationPlayer.play(hitFrames, 100);
                }
                break;

            case SPECIAL:
                break;
        }
    }

    public AnimationState getState() {
        return currentState;
    }

    public void setColor(Color color) {
        sprite.setOpacity(1);
        sprite.setStyle("-fx-effect: dropshadow(gaussian, " + toRgbString(color) + ", 20, 0.7, 0, 0);");
    }

    public void resetColor() {
        sprite.setStyle(null);
    }

    public Color getOriginalColor() {
        return originalColor;
    }

    private String toRgbString(Color color) {
        return "rgba(" +
                (int) (color.getRed() * 255) + "," +
                (int) (color.getGreen() * 255) + "," +
                (int) (color.getBlue() * 255) + ",0.8)";
    }

    public void update() {
        long now = System.currentTimeMillis();

        if (flashEndTime > 0 && now > flashEndTime) {
            flashEndTime = 0;
        }

        if (specialAttack != null) {
            specialAttack.update(this);
        }

        if (currentState == AnimationState.HIT && now >= hitStateEndTime) {
            setState(AnimationState.IDLE);
        } else if (currentState == AnimationState.ATTACK && now >= attackStateEndTime) {
            setState(AnimationState.IDLE);
        } else if (currentState != AnimationState.ATTACK && currentState != AnimationState.HIT) {
            if (movedThisFrame) {
                setState(AnimationState.WALK);
            } else {
                setState(AnimationState.IDLE);
            }
        }

        animationPlayer.update();
        movedThisFrame = false;
    }
}