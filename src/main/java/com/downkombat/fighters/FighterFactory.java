package com.downkombat.fighters;

import com.downkombat.combat.DefaultPunch;
import com.downkombat.combat.attacks.TransformationAttack;
import com.downkombat.combat.attacks.MolarAttack;
import com.downkombat.combat.attacks.UberFailAttack;
import com.downkombat.combat.attacks.NepalShieldAttack;
import com.downkombat.combat.projectiles.ProjectileManager;
import com.downkombat.combat.projectiles.CarManager;

import com.downkombat.config.GameConfig;

import javafx.scene.paint.Color;

public class FighterFactory {

    public static Fighter create(
            CharacterType type,
            double x,
            ProjectileManager pm,
            CarManager cm
    ) {

        switch (type) {

            case ANTONIO:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.RED,
                        new DefaultPunch(),
                        new TransformationAttack(
                                15000,
                                15,
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

            case MIGUE:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.GREEN,
                        new DefaultPunch(),
                        new TransformationAttack(
                                15000,
                                15,
                                60,
                                Color.DARKGREEN
                        )
                );

            case DARIO:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.PURPLE,
                        new DefaultPunch(),
                        new TransformationAttack(
                                15000,
                                15,
                                60,
                                Color.MEDIUMPURPLE,
                                "/sounds/dario_golden.m4a"
                        )
                );

            case JUANMA:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.ORANGE,
                        new DefaultPunch(),
                        new UberFailAttack(cm)
                );

            case PEPE:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.YELLOW,
                        new DefaultPunch(),
                        new NepalShieldAttack()
                );

            default:
                throw new IllegalArgumentException(
                        "Character not implemented: " + type
                );
        }
    }
}
