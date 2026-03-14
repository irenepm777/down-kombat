package com.downkombat.combat.attacks;

import com.downkombat.audio.SoundManager;
import com.downkombat.combat.SpecialAttack;
import com.downkombat.fighters.Fighter;

public class TransformationAttack implements SpecialAttack {

    private final int duration;
    private final int damage;
    private final int knockback;
    private final String soundPath;

    private boolean active = false;
    private long endTime = 0;

    public TransformationAttack(int duration, int damage, int knockback) {
        this(duration, damage, knockback, null);
    }

    public TransformationAttack(int duration, int damage, int knockback, String soundPath) {
        this.duration = duration;
        this.damage = damage;
        this.knockback = knockback;
        this.soundPath = soundPath;
    }

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        if (active) return;

        active = true;

        endTime = System.currentTimeMillis() + duration;

        if (soundPath != null) {
            SoundManager.play(soundPath);
        }

        System.out.println("TRANSFORMATION ACTIVATED");
    }

    @Override
    public void update(Fighter attacker) {

        if (!active) return;

        if (System.currentTimeMillis() > endTime) {

            active = false;

            if (soundPath != null) {
                SoundManager.stop();
            }

            System.out.println("TRANSFORMATION ENDED");
        }
    }
}