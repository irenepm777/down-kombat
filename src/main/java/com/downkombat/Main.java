package com.downkombat;

import com.downkombat.fighters.CharacterType;
import com.downkombat.game.GameScene;
import com.downkombat.menuinicio.MenuInicio;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;

        Font.loadFont(
            "https://fonts.gstatic.com/s/pressstart2p/v11/e3t4euO8T-267oIAQAu6jDQyK3k.woff2",
            10
        );

        startMenu();
    }

    public void startMenu() {

        MenuInicio menu = new MenuInicio(this);
        menu.start(stage);
    }

    public void startGame(CharacterType p1, CharacterType p2) {

        GameScene game = new GameScene(p1, p2);

        stage.setScene(game.getScene());
    }

    public static void main(String[] args) {
        launch();
    }
}