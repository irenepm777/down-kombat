package com.downkombat.combat.projectiles;

import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarManager {

    private List<Car> cars = new ArrayList<>();

    private Group root;

    public CarManager(Group root) {
        this.root = root;
    }

    public void spawn(Car car) {

        cars.add(car);
        root.getChildren().add(car.getNode());
    }

    public void update() {

        Iterator<Car> it = cars.iterator();

        while (it.hasNext()) {

            Car car = it.next();

            car.update();

            if (!car.isActive()) {

                root.getChildren().remove(car.getNode());
                it.remove();
            }
        }
    }
}
