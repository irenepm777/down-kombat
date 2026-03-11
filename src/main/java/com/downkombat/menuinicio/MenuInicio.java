package com.downkombat.menuinicio;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;



public class MenuInicio extends Application {

	private static final int SCREEN_WIDTH = 1280;
	private static final int SCREEN_HEIGHT = 720;
	 private static MediaPlayer player;


	@Override
	public void start(Stage primaryStage) {
		StackPane root = new StackPane();
		
		
		
		musicTest(); // Prueba de música para verificar que se carga correctamente

		// ========== CAPA 1: FONDO (primero = detrás) ==========
		ImageView bgImage = loadBackgroundImage();
		if (bgImage != null) {
			root.getChildren().add(bgImage);
		} else {
			// Fallback: gradiente si no hay imagen
			Rectangle background = createArcadeBackground();
			root.getChildren().add(background);
		}

		// ========== CAPA 2: MENÚ (encima de todo) ==========
		VBox menuBox = createMenuBox(); // Caja para el menú con fondo semitransparente y efectos
		Text gameTitle = createGameTitle(); // Título con fuente arcade y efectos
		Button startButton = createMenuButton("START", "start-button");
		Button tutorialButton = createMenuButton("TUTORIAL", "tutorial-button");

		startButton.setOnAction(e -> handleStart());
		tutorialButton.setOnAction(e -> handleTutorial());

		menuBox.getChildren().addAll(gameTitle, startButton, tutorialButton);
		root.getChildren().add(menuBox);

		// Crear escena
		Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT); // Tamaño fijo para evitar problemas de escalado
		loadStylesheet(scene);

		primaryStage.setTitle("DAWN KOMBAT - MAIN MENU");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

		playBackgroundMusic();
		System.out.println("Música de fondo cargada correctamente");

	}

	// ========== MÉTODOS DE CARGA DE IMÁGENES ==========

	private ImageView loadBackgroundImage() {
		try {
			// Usar getResourceAsStream() para mayor compatibilidad
			Image bg = new Image(getClass().getResourceAsStream("/images/fondo.png"));

			if (bg.isError()) {
				System.err.println("Error: La imagen fondo.png existe pero no se pudo cargar");
				return null;
			}

			ImageView bgImage = new ImageView(bg);
			bgImage.setFitWidth(SCREEN_WIDTH);
			bgImage.setFitHeight(SCREEN_HEIGHT);
			bgImage.setPreserveRatio(false);

			System.out.println("Fondo cargado correctamente: " + bg.getWidth() + "x" + bg.getHeight());
			return bgImage;

		} catch (Exception e) {
			System.err.println("Error al cargar fondo: " + e.getMessage());
			return null;
		}
	}

	// ========== RESTO DE MÉTODOS ==========

	private Rectangle createArcadeBackground() {
		LinearGradient gradient = new LinearGradient( // Gradiente de fondo con colores arcade
				0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
				new Stop(0, Color.DARKSLATEBLUE), // Azul oscuro en la esquina superior izquierda
				new Stop(0.3, Color.DARKBLUE), // Azul más brillante hacia el centro
				new Stop(0.7, Color.MIDNIGHTBLUE),
				new Stop(1, Color.DARKORANGE)
				);

		Rectangle bg = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT);
		bg.setFill(gradient);
		return bg;
	}

	// Reproductor de música de fondo (opcional, requiere archivo de audio y manejo de recursos)
	private void playBackgroundMusic() {
	    try {
	        stop(); // Stop previous music if any

	        Media media = new Media(
	            getClass().getResource("/sounds/Menu_Inicio.mp3").toExternalForm()
	        );

	        player = new MediaPlayer(media); // Use global player
	        player.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
	        player.setVolume(1.0);
	        player.play();

	        System.out.println("Background music started");

	    } catch (Exception e) {
	        System.err.println("Error loading background music: " + e.getMessage());
	    }
	}
	private VBox createMenuBox() {
		VBox menuBox = new VBox(40);
		menuBox.setAlignment(Pos.CENTER);
		menuBox.setTranslateY(100); // Bajar para no tapar el logo

		Glow glow = new Glow(0.2); // Brillo suave para resaltar el menú sin competir con el título
		menuBox.setEffect(glow); // Efecto de brillo para el menú

		return menuBox;
	}

	private Text createGameTitle() {
		Text title = new Text("DOWN\nKOMBAT");
		title.setId("game-title");

		try { // Intentar cargar la fuente arcade personalizada
			Font arcadeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 64); // Tamaño 64 para un título grande
			if (arcadeFont != null) {
				title.setFont(arcadeFont);
			} else {
				title.setFont(Font.font("Monospace", FontWeight.BOLD, 64)); // Fallback si la fuente no se carga
			}
		} catch (Exception e) {
			title.setFont(Font.font("Monospace", FontWeight.BOLD, 64)); // Fallback si hay error al cargar la fuente
			System.err.println("Error al cargar fuente arcade: " + e.getMessage());
		}

		DropShadow shadow = new DropShadow(); // Sombra para resaltar el título
		shadow.setColor(Color.ORANGE);
		shadow.setRadius(15);
		shadow.setSpread(0.6);
		shadow.setOffsetX(4);
		shadow.setOffsetY(4);
		title.setEffect(shadow);
		title.setFill(Color.GOLD);

		return title;
	}

	private Button createMenuButton(String text, String styleId) { // styleId para diferenciar START y TUTORIAL
		Button button = new Button(text);
		button.setId(styleId);
		button.getStyleClass().add("arcade-button");
		button.setMinWidth(300);
		button.setMinHeight(70);

		button.setOnMouseEntered(e -> { // Efecto de hover: agrandar y resaltar
			button.setScaleX(1.15);
			button.setScaleY(1.15);
			Glow glow = new Glow(0.8); // Brillo intenso al hacer hover
			button.setEffect(glow);
		});

		button.setOnMouseExited(e -> { // Volver a normal al salir hover
			button.setScaleX(1.0);
			button.setScaleY(1.0);
			button.setEffect(null); // Quitar efecto de brillo
		});

		button.setOnMousePressed(e -> button.setTranslateY(2));
		button.setOnMouseReleased(e -> button.setTranslateY(0));

		return button;
	}
	
	private void musicTest() {
		try {
			Media media = new Media(getClass().getResource("\"C:\\Users\\Said\\Documents\\Repositorio\\down-kombat\\src\\main\\resources\\sounds\\Menu_Inicio.mp3\"").toExternalForm());
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(1.0);
			mediaPlayer.play();
			System.out.println("Música de prueba cargada y reproduciéndose correctamente");
		} catch (Exception e) {
			System.err.println("Error en musicTest: " + e.getMessage());
		}
	}
	
	

	private void loadStylesheet(Scene scene) {
		try {
			// Ruta correcta del CSS
			var url = getClass().getResource("/css/style.css");

			if (url == null) {
				System.err.println("CSS no encontrado en /css/style.css");
				return;
			}

			String css = url.toExternalForm();
			scene.getStylesheets().add(css);

			System.out.println("CSS cargado correctamente: " + css);

		} catch (Exception e) {
			System.err.println("Error cargando CSS: " + e.getMessage());
		}
	}
	
	
	
private void handleStart() {
	System.out.println("¡JUEGO INICIADO!");
}

private void handleTutorial() {
	System.out.println("MODO TUTORIAL ACTIVADO");
}

public static MediaPlayer getPlayer() {
	return player;
}

public static void setPlayer(MediaPlayer player) {
	MenuInicio.player = player;
}

}
