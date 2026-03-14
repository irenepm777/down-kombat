package com.downkombat.combat.projectiles;

import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarManager {

    private final Group root;

    private final List<Car> cars = new ArrayList<>();

    public CarManager(Group root) {
        this.root = root;
    }

    public void spawn(Car car) {

        cars.add(car);

        root.getChildren().add(car.getNode());
    }

    public void update() {

        Iterator<Car> iterator = cars.iterator();

        while (iterator.hasNext()) {

            Car car = iterator.next();

            car.update();

            if (!car.isActive()) {

                root.getChildren().remove(car.getNode());

                iterator.remove();
            }
        }
    }
}