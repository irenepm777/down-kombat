package com.downkombat.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class InputHandler {

    private Set<KeyCode> pressedKeys = new HashSet<>();

    public InputHandler(Scene scene) {

        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));

        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
    }

    public boolean isPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }
}
