package com.downkombat.combat.attacks;

import com.downkombat.audio.SoundManager;
import com.downkombat.combat.SpecialAttack;
import com.downkombat.fighters.Fighter;
import javafx.scene.paint.Color;

public class TransformationAttack implements SpecialAttack {

    private int duration;
    private int damage;
    private int knockback;

    private Color transformColor;

    private String soundPath;

    private boolean transformed = false;
    private long endTime = 0;

    public TransformationAttack(int duration, int damage, int knockback, Color color) {
        this(duration, damage, knockback, color, null);
    }

    public TransformationAttack(int duration, int damage, int knockback, Color color, String soundPath) {
        this.duration = duration;
        this.damage = damage;
        this.knockback = knockback;
        this.transformColor = color;
        this.soundPath = soundPath;
    }

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        if (transformed) return;

        transformed = true;
        endTime = System.currentTimeMillis() + duration;

        attacker.setColor(transformColor);

        if (soundPath != null) {
            SoundManager.play(soundPath);
        }

        System.out.println("TRANSFORMATION ACTIVATED");
    }

    @Override
    public void update(Fighter attacker) {

        if (!transformed) return;

        if (System.currentTimeMillis() > endTime) {

            transformed = false;

            attacker.setColor(attacker.getOriginalColor());

            if (soundPath != null) {
                SoundManager.stop();
            }

            System.out.println("TRANSFORMATION ENDED");
        }
    }
}