package com.downkombat;

import com.downkombat.fighters.CharacterType;
import com.downkombat.game.CharacterSelectScene;
import com.downkombat.game.GameScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        CharacterSelectScene select = new CharacterSelectScene(this);

        stage.setTitle("DOWN KOMBAT");
        stage.setScene(select.getScene());
        stage.show();
    }

    public void startGame(CharacterType p1, CharacterType p2) {

        GameScene game = new GameScene(p1, p2);

        stage.setScene(game.getScene());
    }

    public static void main(String[] args) {
        launch();
    }
}