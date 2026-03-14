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
import java.util.Map;

public class Fighter {

    private String fighterId;

    private Group node;
    private ImageView sprite;
    private AnimationPlayer animationPlayer;

    private Map<AnimationState, List<Image>> normalAnimations;
    private Map<AnimationState, List<Image>> specialAnimations;

    private List<Image> specialFrames;

    private boolean transformed = false;

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

    private long attackStateEndTime = 0;
    private long hitStateEndTime = 0;
    private long specialStateEndTime = 0;

    private static final double SPRITE_HEIGHT = 600;

    private static final long ATTACK_ANIMATION_DURATION = 220;
    private static final long HIT_ANIMATION_DURATION = 180;
    private static final long SPECIAL_ANIMATION_DURATION = 300;

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
        sprite.setFitHeight(SPRITE_HEIGHT);
        sprite.setPreserveRatio(true);

        sprite.setTranslateX(-SPRITE_HEIGHT / 4);
        sprite.setTranslateY(-SPRITE_HEIGHT);

        node.getChildren().add(sprite);

        node.setTranslateX(x);
        node.setTranslateY(groundY);

        if (x > GameConfig.WIDTH / 2) {
            facingRight = false;
            sprite.setScaleX(-1);
        }

        animationPlayer = new AnimationPlayer(sprite);

        try {

            normalAnimations = AnimationLoader.load(
                    "/sprites/fighters/" + fighterId
            );

            specialFrames = AnimationLoader.loadSpecial(
                    "/sprites/fighters/" + fighterId + "/special"
            );

            specialAnimations = AnimationLoader.load(
                    "/sprites/fighters/" + fighterId + "/special"
            );

            if (specialAnimations == null || specialAnimations.isEmpty()) {
                specialFrames = AnimationLoader.loadSpecial(
                        "/sprites/fighters/" + fighterId + "/special"
                );
            }

            playAnimation(AnimationState.IDLE);

        } catch (Exception e) {

            System.out.println("Animation load failed for " + fighterId);

        }
    }

    private Map<AnimationState, List<Image>> currentAnimations() {

        if (transformed && specialAnimations != null) {
            return specialAnimations;
        }

        return normalAnimations;
    }

    private void playAnimation(AnimationState state) {

        Map<AnimationState, List<Image>> animations = currentAnimations();
        List<Image> frames = animations.get(state);

        if (frames != null && !frames.isEmpty()) {

            int speed = switch (state) {

                case WALK -> 120;
                case ATTACK -> 80;
                case HIT -> 100;
                default -> 150;

            };

            animationPlayer.play(frames, speed);
        }
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

        if (specialFrames != null && !specialFrames.isEmpty()) {

            animationPlayer.play(specialFrames, 90);

            specialStateEndTime =
                    System.currentTimeMillis() + SPECIAL_ANIMATION_DURATION;

            currentState = AnimationState.SPECIAL;
            return;
        }

        if (specialAnimations != null && !specialAnimations.isEmpty()) {

            transformed = !transformed;
            playAnimation(AnimationState.IDLE);
        }
    }

    public void damage(int amount) {

        if (invulnerable) return;

        health -= amount;

        if (health < 0) health = 0;

        hitStateEndTime = System.currentTimeMillis() + HIT_ANIMATION_DURATION;

        setState(AnimationState.HIT);
    }

    public void moveLeft() {

        node.setTranslateX(node.getTranslateX() - speed);

        movedThisFrame = true;

        facingRight = false;

        sprite.setScaleX(-1);
    }

    public void moveRight() {

        node.setTranslateX(node.getTranslateX() + speed);

        movedThisFrame = true;

        facingRight = true;

        sprite.setScaleX(1);
    }

    public void setState(AnimationState newState) {

        if (currentState == newState) return;

        currentState = newState;

        playAnimation(newState);
    }

    public void setX(double x) {
        node.setTranslateX(x);
    }

    public int getHealth() {
        return health;
    }

    public boolean canAttack() {
        long now = System.currentTimeMillis();
        return now - lastAttackTime >= GameConfig.ATTACK_COOLDOWN;
    }

    public boolean canSpecial() {
        long now = System.currentTimeMillis();
        return now - lastSpecialTime >= GameConfig.SPECIAL_COOLDOWN;
    }

    public boolean isNear(Fighter other) {
        double distance = Math.abs(getX() - other.getX());
        return distance < GameConfig.ATTACK_RANGE;
    }

    public boolean isFacing(Fighter other) {

        double otherX = other.getX();

        if (otherX > getX()) {
            return facingRight;
        } else {
            return !facingRight;
        }
    }

    public void applyKnockback(Fighter attacker, int force) {

        if (attacker.getX() < this.getX()) {
            node.setTranslateX(node.getTranslateX() + force);
        } else {
            node.setTranslateX(node.getTranslateX() - force);
        }
    }

    public void setColor(Color color) {

        sprite.setStyle(
                "-fx-effect: dropshadow(gaussian, " +
                        "rgba(" +
                        (int)(color.getRed()*255) + "," +
                        (int)(color.getGreen()*255) + "," +
                        (int)(color.getBlue()*255) + ",0.8)," +
                        "20,0.7,0,0)"
        );
    }

    public void resetColor() {
        sprite.setStyle(null);
    }

    public Color getOriginalColor() {
        return Color.WHITE;
    }

    public void setInvulnerable(boolean value) {
        invulnerable = value;
    }

    public void update() {

        long now = System.currentTimeMillis();

        if (specialAttack != null) {
            specialAttack.update(this);
        }

        if (currentState == AnimationState.HIT && now >= hitStateEndTime) {

            setState(AnimationState.IDLE);

        } else if (currentState == AnimationState.ATTACK && now >= attackStateEndTime) {

            setState(AnimationState.IDLE);

        } else if (currentState == AnimationState.SPECIAL && now >= specialStateEndTime) {

            setState(AnimationState.IDLE);

        } else if (currentState != AnimationState.ATTACK &&
                   currentState != AnimationState.HIT &&
                   currentState != AnimationState.SPECIAL) {

            if (movedThisFrame) {
                setState(AnimationState.WALK);
            } else {
                setState(AnimationState.IDLE);
            }
        }

        animationPlayer.update();

        movedThisFrame = false;
    }

    public Group getNode() {
        return node;
    }

    public double getX() {
        return node.getTranslateX();
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public boolean isDead() {
        return health <= 0;
    }
}