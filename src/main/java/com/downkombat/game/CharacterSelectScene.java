package com.downkombat.game;

import com.downkombat.Main;
import com.downkombat.config.GameConfig;
import com.downkombat.fighters.CharacterType;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CharacterSelectScene {

    private Scene scene;

    private CharacterType[] characters = CharacterType.values();

    private int p1Index = 0;
    private int p2Index = 1;

    private boolean p1Locked = false;
    private boolean p2Locked = false;

    private Text p1Text;
    private Text p2Text;
    private Text infoText;

    public CharacterSelectScene(Main main) {

        Group root = new Group();

        p1Text = new Text();
        p1Text.setFont(new Font(40));
        p1Text.setFill(Color.RED);
        p1Text.setX(200);
        p1Text.setY(300);

        p2Text = new Text();
        p2Text.setFont(new Font(40));
        p2Text.setFill(Color.BLUE);
        p2Text.setX(800);
        p2Text.setY(300);

        infoText = new Text("A/D + SPACE (P1)    ←/→ + ENTER (P2)");
        infoText.setFont(new Font(20));
        infoText.setX(420);
        infoText.setY(600);

        root.getChildren().addAll(p1Text, p2Text, infoText);

        scene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT);

        updateTexts();

        scene.setOnKeyPressed(e -> handleInput(e.getCode(), main));
    }

    private void handleInput(KeyCode key, Main main) {

        // PLAYER 1
        if (!p1Locked) {

            if (key == KeyCode.A) {
                p1Index = (p1Index - 1 + characters.length) % characters.length;
            }

            if (key == KeyCode.D) {
                p1Index = (p1Index + 1) % characters.length;
            }

            if (key == KeyCode.SPACE) {
                p1Locked = true;
            }
        }

        // PLAYER 2
        if (!p2Locked) {

            if (key == KeyCode.LEFT) {
                p2Index = (p2Index - 1 + characters.length) % characters.length;
            }

            if (key == KeyCode.RIGHT) {
                p2Index = (p2Index + 1) % characters.length;
            }

            if (key == KeyCode.ENTER) {
                p2Locked = true;
            }
        }

        updateTexts();

        if (p1Locked && p2Locked) {
            main.showSelectMap(characters[p2Index].name());
        }
    }

    private void updateTexts() {
        p1Text.setText("P1: " + characters[p1Index]);
        p2Text.setText("P2: " + characters[p2Index]);
    }

    public Scene getScene() {
        return scene;
    }
}