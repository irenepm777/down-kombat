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

    private final String fighterId;

    private final Group node;
    private final ImageView sprite;
    private final AnimationPlayer animationPlayer;

    private Map<AnimationState, List<Image>> normalAnimations;
    private Map<AnimationState, List<Image>> specialAnimations;
    private List<Image> specialFrames;

    private boolean transformed = false;

    private final double speed = GameConfig.PLAYER_SPEED;
    private int health = GameConfig.PLAYER_MAX_HEALTH;

    private AnimationState currentState = AnimationState.IDLE;

    private boolean facingRight = true;
    private boolean invulnerable = false;
    private boolean movedThisFrame = false;

    private final SpecialAttack normalAttack;
    private final SpecialAttack specialAttack;

    private long lastAttackTime = 0;
    private long lastSpecialTime = 0;

    private long attackStateEndTime = 0;
    private long hitStateEndTime = 0;
    private long specialStateEndTime = 0;

    private final Color originalColor;

    private static final double SPRITE_HEIGHT = 600;

    private static final long IDLE_FRAME_DURATION = 220;
    private static final long WALK_FRAME_DURATION = 180;
    private static final long ATTACK_FRAME_DURATION = 140;
    private static final long HIT_FRAME_DURATION = 160;
    private static final long SPECIAL_FRAME_DURATION = 200;

    private static final long ATTACK_ANIMATION_DURATION = 500;
    private static final long HIT_ANIMATION_DURATION = 400;
    private static final long SPECIAL_ANIMATION_DURATION = 500;

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
        this.originalColor = color;

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

            specialAnimations = AnimationLoader.load(
                    "/sprites/fighters/" + fighterId + "/special"
            );

            specialFrames = AnimationLoader.loadSpecial(
                    "/sprites/fighters/" + fighterId + "/special"
            );

            if (specialAnimations != null && specialAnimations.isEmpty()) {
                specialAnimations = null;
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

        if (state == AnimationState.SPECIAL && specialFrames != null && !specialFrames.isEmpty()) {

            animationPlayer.play(specialFrames, SPECIAL_FRAME_DURATION, false);
            return;
        }

        Map<AnimationState, List<Image>> animations = currentAnimations();
        if (animations == null) return;

        List<Image> frames = animations.get(state);
        if (frames == null || frames.isEmpty()) return;

        switch (state) {
            case IDLE -> animationPlayer.play(frames, IDLE_FRAME_DURATION, true);
            case WALK -> animationPlayer.play(frames, WALK_FRAME_DURATION, true);
            case ATTACK -> animationPlayer.play(frames, ATTACK_FRAME_DURATION, false);
            case HIT -> animationPlayer.play(frames, HIT_FRAME_DURATION, false);
            case SPECIAL -> animationPlayer.play(frames, SPECIAL_FRAME_DURATION, false);
        }
    }

    public void performAttack(Fighter enemy) {

        if (!canAttack()) return;

        lastAttackTime = System.currentTimeMillis();

        normalAttack.execute(this, enemy);

        attackStateEndTime = System.currentTimeMillis() + ATTACK_ANIMATION_DURATION;

        setState(AnimationState.ATTACK);
    }

    public void performSpecial(Fighter enemy) {

        if (!canSpecial()) return;

        lastSpecialTime = System.currentTimeMillis();

        specialAttack.execute(this, enemy);

        if (!fighterId.equals("juanma")) {

            setState(AnimationState.SPECIAL);

            specialStateEndTime =
                    System.currentTimeMillis() + SPECIAL_ANIMATION_DURATION;
        }
    }

    public void triggerSpecialAnimation() {

        setState(AnimationState.SPECIAL);

        specialStateEndTime =
                System.currentTimeMillis() + SPECIAL_ANIMATION_DURATION;
    }

    public void damage(int amount) {

        if (invulnerable) return;

        health -= amount;

        if (health < 0) health = 0;

        // IMPORTANTE: no interrumpir la animación SPECIAL (atropello de Juanma)
        if (currentState != AnimationState.SPECIAL) {

            hitStateEndTime = System.currentTimeMillis() + HIT_ANIMATION_DURATION;

            setState(AnimationState.HIT);
        }
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

    public Group getNode() { return node; }

    public double getX() { return node.getTranslateX(); }

    public boolean isFacingRight() { return facingRight; }

    public boolean isDead() { return health <= 0; }

    public int getHealth() { return health; }

    public boolean canAttack() {

        return System.currentTimeMillis() - lastAttackTime
                >= GameConfig.ATTACK_COOLDOWN;
    }

    public boolean canSpecial() {

        return System.currentTimeMillis() - lastSpecialTime
                >= GameConfig.SPECIAL_COOLDOWN;
    }
}