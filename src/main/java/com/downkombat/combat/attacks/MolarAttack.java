package com.downkombat.combat.attacks;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.combat.projectiles.Projectile;
import com.downkombat.combat.projectiles.ProjectileManager;
import com.downkombat.fighters.Fighter;
import javafx.scene.paint.Color;

public class MolarAttack implements SpecialAttack {

    private ProjectileManager projectileManager;

    private int shotsRemaining = 0;
    private long nextShotTime = 0;

    private boolean direction;

    public MolarAttack(ProjectileManager projectileManager) {
        this.projectileManager = projectileManager;
    }

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        // iniciar ráfaga de muelas
        shotsRemaining = 6;
        nextShotTime = System.currentTimeMillis();

        // guardar dirección real del personaje
        direction = attacker.isFacingRight();

        // animación: Soraya abre la boca
        attacker.setColor(Color.PINK);
    }

    @Override
    public void update(Fighter attacker) {

        if (shotsRemaining <= 0) {
            // restaurar color cuando termina la ráfaga
            attacker.setColor(attacker.getOriginalColor());
            return;
        }

        long now = System.currentTimeMillis();

        if (now >= nextShotTime) {

            // spawn delante del personaje
            double spawnX = attacker.getX();

            if (direction) {
                spawnX += 70;
            } else {
                spawnX -= 70;
            }

            Projectile molar = new Projectile(
                    spawnX,
                    attacker.getNode().getTranslateY() - 120,
                    direction,
                    attacker
            );

            projectileManager.spawn(molar);

            shotsRemaining--;
            nextShotTime = now + 80;
        }
    }
}