package com.downkombat.fighters;

import com.downkombat.animation.AnimationLoader;
import com.downkombat.animation.AnimationPlayer;
import com.downkombat.animation.AnimationState;
import com.downkombat.combat.SpecialAttack;
import com.downkombat.config.GameConfig;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class Fighter {

    // Unique identifier used to load the correct assets
    private final String fighterId;

    // JavaFX node representing the fighter in the scene
    private final Group node;

    // Sprite displayed on screen
    private final ImageView sprite;

    // Handles animation frame playback
    private final AnimationPlayer animationPlayer;

    // Normal animation set
    private Map<AnimationState, List<Image>> normalAnimations;

    // Special animation set (used when transformed)
    private Map<AnimationState, List<Image>> specialAnimations;

    // Special attack animation frames
    private List<Image> specialFrames;

    // Indicates if the fighter is currently transformed
    private boolean transformed = false;

    // Movement speed
    private final double speed = GameConfig.PLAYER_SPEED;

    // Current health
    private int health = GameConfig.PLAYER_MAX_HEALTH;

    // Current animation state
    private AnimationState currentState = AnimationState.IDLE;

    // Orientation
    private boolean facingRight = true;

    // Used for shield or temporary invulnerability
    private boolean invulnerable = false;

    // Tracks movement per frame
    private boolean movedThisFrame = false;

    // Attacks
    private final SpecialAttack normalAttack;
    private final SpecialAttack specialAttack;

    // Cooldown tracking
    private long lastAttackTime = 0;
    private long lastSpecialTime = 0;

    // Animation timers
    private long attackStateEndTime = 0;
    private long hitStateEndTime = 0;
    private long specialStateEndTime = 0;

    // Original color (kept for potential visual effects)
    private final Color originalColor;

    // Sprite size
    private static final double SPRITE_HEIGHT = 600;

    // Animation frame durations
    private static final long IDLE_FRAME_DURATION = 220;
    private static final long WALK_FRAME_DURATION = 180;
    private static final long ATTACK_FRAME_DURATION = 140;
    private static final long HIT_FRAME_DURATION = 160;
    private static final long SPECIAL_FRAME_DURATION = 200;

    // Animation total durations
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

        // Load initial sprite
        String spritePath =
                "/sprites/fighters/" + fighterId + "/idle/idle_1.png";

        System.out.println("Loading sprite: " + spritePath);

        URL url = Fighter.class.getResource(spritePath);
        Image image;

        // Prevent crash if sprite is missing
        if (url == null) {

            System.out.println("Missing sprite: " + spritePath);

            URL fallbackUrl = Fighter.class.getResource("/sprites/fallback.png");

            if (fallbackUrl != null) {
                image = new Image(fallbackUrl.toExternalForm());
            } else {
                System.out.println("Missing fallback sprite: /sprites/fallback.png");
                image = new WritableImage(1, 1);
            }

        } else {
            image = new Image(url.toExternalForm());
        }

        sprite = new ImageView(image);

        sprite.setSmooth(false);
        sprite.setFitHeight(SPRITE_HEIGHT);
        sprite.setPreserveRatio(true);

        // Align sprite with the ground
        sprite.setTranslateX(-SPRITE_HEIGHT / 4);
        sprite.setTranslateY(-SPRITE_HEIGHT);

        node.getChildren().add(sprite);

        node.setTranslateX(x);
        node.setTranslateY(groundY);

        // Flip sprite if spawned on the right side
        if (x > GameConfig.WIDTH / 2) {
            facingRight = false;
            sprite.setScaleX(-1);
        }

        animationPlayer = new AnimationPlayer(sprite);

        try {

            // Load normal animations
            normalAnimations = AnimationLoader.load(
                    "/sprites/fighters/" + fighterId
            );

            // Load transformed animations
            specialAnimations = AnimationLoader.load(
                    "/sprites/fighters/" + fighterId + "/special"
            );

            // Load special attack animation
            specialFrames = AnimationLoader.loadSpecial(
                    "/sprites/fighters/" + fighterId + "/special"
            );

            if (specialAnimations != null && specialAnimations.isEmpty()) {
                specialAnimations = null;
            }

            playAnimation(AnimationState.IDLE);

        } catch (Exception e) {
            System.out.println("Animation load failed for " + fighterId);
            e.printStackTrace();
        }
    }

    /**
     * Returns the current animation set depending on transformation state.
     */
    private Map<AnimationState, List<Image>> currentAnimations() {

        if (transformed && specialAnimations != null) {
            return specialAnimations;
        }

        return normalAnimations;
    }

    /**
     * Plays the animation corresponding to the given state.
     */
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

    /**
     * Performs a normal attack if cooldown allows it.
     */
    public void performAttack(Fighter enemy) {

        if (!canAttack()) return;

        lastAttackTime = System.currentTimeMillis();

        normalAttack.execute(this, enemy);

        attackStateEndTime = System.currentTimeMillis() + ATTACK_ANIMATION_DURATION;

        setState(AnimationState.ATTACK);
    }

    /**
     * Executes special ability.
     */
    public void performSpecial(Fighter enemy) {

        if (!canSpecial()) return;

        lastSpecialTime = System.currentTimeMillis();

        specialAttack.execute(this, enemy);

        if (!fighterId.equals("juanma")) {
            setState(AnimationState.SPECIAL);
            specialStateEndTime = System.currentTimeMillis() + SPECIAL_ANIMATION_DURATION;
        }
    }

    /**
     * Forces special animation (used by transformation attacks).
     */
    public void triggerSpecialAnimation() {

        setState(AnimationState.SPECIAL);

        specialStateEndTime =
                System.currentTimeMillis() + SPECIAL_ANIMATION_DURATION;
    }

    /**
     * Applies damage if the fighter is not invulnerable.
     */
    public void damage(int amount) {

        if (invulnerable) return;

        health -= amount;

        if (health < 0) health = 0;

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

    /**
     * Update method called each frame by the game loop.
     */
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

    /**
     * Enables or disables transformation state.
     */
    public void setTransformed(boolean value) {
        transformed = value;
    }

    public boolean isTransformed() {
        return transformed;
    }

    /**
     * Enables or disables temporary invulnerability (used by shields).
     */
    public void setInvulnerable(boolean value) {
        invulnerable = value;
    }

    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime
                >= GameConfig.ATTACK_COOLDOWN;
    }

    public boolean canSpecial() {
        return System.currentTimeMillis() - lastSpecialTime
                >= GameConfig.SPECIAL_COOLDOWN;
    }
}