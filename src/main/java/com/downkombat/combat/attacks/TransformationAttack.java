package com.downkombat.combat.attacks;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.fighters.Fighter;
import javafx.scene.paint.Color;

public class TransformationAttack implements SpecialAttack {

    private boolean transformed = false;
    private long transformEndTime = 0;

    private final int duration;
    private final int damageBoost;
    private final int knockbackBoost;

    private final Color transformColor;
    private Color originalColor;

    public TransformationAttack(
            int duration,
            int damageBoost,
            int knockbackBoost,
            Color transformColor
    ) {
        this.duration = duration;
        this.damageBoost = damageBoost;
        this.knockbackBoost = knockbackBoost;
        this.transformColor = transformColor;
    }

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        long now = System.currentTimeMillis();

        if (!transformed) {

            transformed = true;
            transformEndTime = now + duration;

            originalColor = attacker.getColor();
            attacker.setColor(transformColor);

            System.out.println("TRANSFORMATION ACTIVATED");
        }

        if (transformed) {

            defender.damage(damageBoost);
            defender.applyKnockback(attacker, knockbackBoost);
        }
    }

    @Override
    public void update(Fighter attacker) {

        if (!transformed) return;

        long now = System.currentTimeMillis();

        if (now > transformEndTime) {

            transformed = false;

            attacker.setColor(originalColor);

            System.out.println("TRANSFORMATION ENDED");
        }
    }
}