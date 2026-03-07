package com.downkombat.fighters;

import com.downkombat.combat.DefaultPunch;
import com.downkombat.combat.attacks.AntonioAttack;
import com.downkombat.config.GameConfig;
import javafx.scene.paint.Color;

public class FighterFactory {

    public static Fighter create(CharacterType type, double x) {

        switch (type) {

            case ANTONIO:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.RED,
                        new DefaultPunch(),
                        new AntonioAttack()
                );

            case SORAYA:
                return new Fighter(
                        x,
                        GameConfig.GROUND_Y,
                        Color.BLUE,
                        new DefaultPunch(),
                        new DefaultPunch()
                );

            default:
                throw new IllegalArgumentException("Character not implemented: " + type);
        }
    }
}
