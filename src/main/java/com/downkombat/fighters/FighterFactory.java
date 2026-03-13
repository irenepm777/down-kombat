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
                        "antonio",
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
                        "soraya",
                        x,
                        GameConfig.GROUND_Y,
                        Color.BLUE,
                        new DefaultPunch(),
                        new MolarAttack(pm)
                );

            case MIGUE:
                return new Fighter(
                        "migue",
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
                        "dario",
                        x,
                        GameConfig.GROUND_Y,
                        Color.PURPLE,
                        new DefaultPunch(),
                        new TransformationAttack(
                                15000,
                                15,
                                60,
                                Color.MEDIUMPURPLE,
                                "/sounds/dario_golden.mp3"
                        )
                );

            case JUANMA:
                return new Fighter(
                        "juanma",
                        x,
                        GameConfig.GROUND_Y,
                        Color.ORANGE,
                        new DefaultPunch(),
                        new UberFailAttack(cm)
                );

            case PEPE:
                return new Fighter(
                        "pepe",
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