package com.downkombat.fighters;

import com.downkombat.combat.DefaultPunch;
import com.downkombat.combat.attacks.TransformationAttack;
import com.downkombat.config.GameConfig;
import com.downkombat.combat.attacks.MolarAttack;
import com.downkombat.combat.projectiles.ProjectileManager;
import javafx.scene.paint.Color;

public class FighterFactory {

    public static Fighter create(CharacterType type, double x, ProjectileManager pm) {

        switch (type) {

            case ANTONIO:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.RED,
                        new DefaultPunch(),
                        new TransformationAttack(
                                15000,
                                12,
                                60,
                                Color.DARKRED
                        )
                );

            case SORAYA:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.BLUE,
                        new DefaultPunch(),
                        new MolarAttack(pm)
                );

            default:
                throw new IllegalArgumentException(
                        "Character not implemented: " + type
                );
        }
    }
}

