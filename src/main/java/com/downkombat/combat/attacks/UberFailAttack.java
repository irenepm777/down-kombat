package com.downkombat.combat.attacks;

import com.downkombat.combat.SpecialAttack;
import com.downkombat.combat.projectiles.Car;
import com.downkombat.combat.projectiles.CarManager;
import com.downkombat.config.GameConfig;
import com.downkombat.fighters.Fighter;

public class UberFailAttack implements SpecialAttack {

    private CarManager carManager;

    public UberFailAttack(CarManager carManager) {
        this.carManager = carManager;
    }

    @Override
    public void execute(Fighter attacker, Fighter defender) {

        System.out.println("JUANMA LLAMA UN UBER");

        Car car = new Car(
                -150,
                GameConfig.GROUND_Y - 20,
                attacker
        );

        carManager.spawn(car);
    }

    @Override
    public void update(Fighter attacker) {

    }
}
