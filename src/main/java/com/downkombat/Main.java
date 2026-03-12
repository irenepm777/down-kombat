package com.downkombat;

import com.downkombat.game.GameScene;
import com.downkombat.menuinicio.MenuInicio;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Clase principal del juego DOWN KOMBAT.
 */
public class Main extends Application {
	
	private Stage primaryStageRef;

	@Override
	public void start(Stage stage) {
		
		this.primaryStageRef = stage;

	    // Load Google Font BEFORE anything else
	    Font.loadFont(
	        "https://fonts.gstatic.com/s/pressstart2p/v11/e3t4euO8T-267oIAQAu6jDQyK3k.woff2",
	        10
	    );
	    
	   

	    // Start menu directly
	    startMenu(stage);
	}

	public void startMenu(Stage stage) {
	    MenuInicio menu = new MenuInicio();
	    menu.start(stage);
	}

	public static void main(String[] args) {
	    launch();
	}
}