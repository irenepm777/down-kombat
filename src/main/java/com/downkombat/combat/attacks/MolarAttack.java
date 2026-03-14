package com.downkombat.combat.attacks;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.combat.projectiles.Projectile;
import com.downkombat.combat.projectiles.ProjectileManager;
import com.downkombat.fighters.Fighter;

public class MolarAttack implements SpecialAttack {

    private final ProjectileManager projectileManager;

    private int shotsRemaining = 0;
    private long nextShotTime = 0;

    private boolean direction;
    private boolean active = false;

    private static final double MOUTH_OFFSET_X = 60;
    private static final double MOUTH_OFFSET_Y = 420;

    private static final int SHOT_DELAY = 80;
    private static final int BURST_SIZE = 6;

    public MolarAttack(ProjectileManager projectileManager) {
        this.projectileManager = projectileManager;
    }

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        shotsRemaining = BURST_SIZE;
        nextShotTime = System.currentTimeMillis();

        direction = attacker.isFacingRight();
        active = true;
    }

    @Override
    public void update(Fighter attacker) {

        if (!active) return;

        if (shotsRemaining <= 0) {
            active = false;
            return;
        }

        long now = System.currentTimeMillis();

        if (now >= nextShotTime) {

            double spawnX = attacker.getX();

            if (direction) {
                spawnX += MOUTH_OFFSET_X;
            } else {
                spawnX -= MOUTH_OFFSET_X;
            }

            double spawnY = attacker.getNode().getTranslateY() - MOUTH_OFFSET_Y;

            Projectile molar = new Projectile(
                    spawnX,
                    spawnY,
                    direction,
                    attacker
            );

            projectileManager.spawn(molar);

            shotsRemaining--;
            nextShotTime = now + SHOT_DELAY;
        }
    }
}