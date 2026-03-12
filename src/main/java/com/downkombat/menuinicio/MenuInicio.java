package com.downkombat.menuinicio;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuInicio extends Application {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static MediaPlayer player;               // música de fondo
    private MediaPlayer backgroundVideoPlayer;       // vídeo de fondo
    private Stage primaryStageRef;

    @Override
    public void start(Stage primaryStage) {
        // Guardar referencia del primaryStage para usarla en handlers
        this.primaryStageRef = primaryStage;

        StackPane root = new StackPane();

        musicTest(); // Prueba de música (usa la misma ruta que playBackgroundMusic)

        // ========== CAPA 1: FONDO (primero = detrás) ==========
        // Intentamos cargar el vídeo de fondo (ruta en resources: /video/fondoInicio.mp4)
        MediaView bgVideo = loadBackgroundVideo();
        if (bgVideo != null) {
            // Añadir el MediaView primero para que quede detrás de todo
            root.getChildren().add(bgVideo);
        } else {
            // Si no hay vídeo, intentar imagen; si tampoco, gradiente
            ImageView bgImage = loadBackgroundImage();
            if (bgImage != null) {
                root.getChildren().add(bgImage);
            } else {
                Rectangle background = createArcadeBackground();
                root.getChildren().add(background);
            }
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

        // Si hay MediaView, enlazar sus dimensiones a la escena para que ocupe todo el fondo
        if (bgVideo != null) {
            bgVideo.fitWidthProperty().bind(scene.widthProperty());
            bgVideo.fitHeightProperty().bind(scene.heightProperty());
            bgVideo.setPreserveRatio(false); // false para cubrir toda la escena sin dejar bandas
            bgVideo.setSmooth(true);
        }

        loadStylesheet(scene);

        primaryStage.setTitle("DAWN KOMBAT - MAIN MENU");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        playBackgroundMusic();
        System.out.println("Música de fondo cargada correctamente");
    }

    // ========== MÉTODOS DE CARGA DE RECURSOS ==========

    /**
     * Carga el vídeo de fondo desde resources/video/fondoInicio.mp4.
     * Devuelve un MediaView si se pudo crear correctamente, o null si falla.
     */
    private MediaView loadBackgroundVideo() {
        try {
            var url = getClass().getResource("/video/fondoInicio.mp4");
            if (url == null) {
                System.err.println("No se encontró /video/fondoInicio.mp4");
                return null;
            }

            Media media = new Media(url.toExternalForm());
            backgroundVideoPlayer = new MediaPlayer(media);

            // Manejo de errores para depuración
            backgroundVideoPlayer.setOnError(() -> {
                System.err.println("MediaPlayer error: " + backgroundVideoPlayer.getError());
            });
            media.setOnError(() -> {
                System.err.println("Media error: " + media.getError());
            });

            // Reproducir cuando esté listo; asegurar loop
            backgroundVideoPlayer.setOnReady(() -> {
                try {
                    backgroundVideoPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    backgroundVideoPlayer.setVolume(0.0); // silenciar por defecto; cambia si quieres audio
                    backgroundVideoPlayer.play();
                    System.out.println("Background video ready and playing");
                } catch (Exception ex) {
                    System.err.println("Error al reproducir vídeo: " + ex.getMessage());
                }
            });

            MediaView mediaView = new MediaView(backgroundVideoPlayer);
            mediaView.setPreserveRatio(false);
            mediaView.setSmooth(true);

            System.out.println("Vídeo de fondo cargado (MediaView creado)");
            return mediaView;

        } catch (MediaException me) {
            System.err.println("Error creando Media/MediaPlayer: " + me.getMessage());
            return null;
        } catch (Exception ex) {
            System.err.println("Error al cargar vídeo: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Fallback: carga la imagen de fondo si no hay vídeo.
     */
    private ImageView loadBackgroundImage() {
        try {
            var stream = getClass().getResourceAsStream("/images/fondo.png");
            if (stream == null) {
                System.err.println("No se encontró /images/fondo.png");
                return null;
            }

            Image bg = new Image(stream);

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

    private Rectangle createArcadeBackground() {
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.DARKSLATEBLUE),
                new Stop(0.3, Color.DARKBLUE),
                new Stop(0.7, Color.MIDNIGHTBLUE),
                new Stop(1, Color.DARKORANGE)
        );

        Rectangle bg = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT);
        bg.setFill(gradient);
        return bg;
    }

    // ========== MÚSICA DE FONDO ==========

    private void playBackgroundMusic() {
        try {
            stopMusic(); // Detener música previa si existe

            var url = getClass().getResource("/sounds/Menu_Inicio.mp3");
            if (url == null) {
                System.err.println("No se encontró /sounds/Menu_Inicio.mp3");
                return;
            }

            Media media = new Media(url.toExternalForm());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(1.0);
            player.play();

            System.out.println("Background music started");

        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    private void stopMusic() {
        if (player != null) {
            try {
                player.stop();
                player.dispose();
            } catch (Exception e) {
                System.err.println("Error al detener player: " + e.getMessage());
            } finally {
                player = null;
            }
        }
    }

    // ========== UI: menú, título y botones ==========

    private VBox createMenuBox() {
        VBox menuBox = new VBox(40);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateY(100); // Bajar para no tapar el logo

        Glow glow = new Glow(0.2);
        menuBox.setEffect(glow);

        return menuBox;
    }

    private Text createGameTitle() {
        Text title = new Text("DOWN\nKOMBAT");
        title.setId("game-title");

        try {
            Font arcadeFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 64);
            if (arcadeFont != null) {
                title.setFont(arcadeFont);
            } else {
                title.setFont(Font.font("Monospace", FontWeight.BOLD, 64));
            }
        } catch (Exception e) {
            title.setFont(Font.font("Monospace", FontWeight.BOLD, 64));
            System.err.println("Error al cargar fuente arcade: " + e.getMessage());
        }

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.ORANGE);
        shadow.setRadius(15);
        shadow.setSpread(0.6);
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        title.setEffect(shadow);
        title.setFill(Color.GOLD);

        return title;
    }

    private Button createMenuButton(String text, String styleId) {
        Button button = new Button(text);
        button.setId(styleId);
        button.getStyleClass().add("arcade-button");
        button.setMinWidth(300);
        button.setMinHeight(70);

        button.setOnMouseEntered(e -> {
            button.setScaleX(1.15);
            button.setScaleY(1.15);
            Glow glow = new Glow(0.8);
            button.setEffect(glow);
        });

        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            button.setEffect(null);
        });

        button.setOnMousePressed(e -> button.setTranslateY(2));
        button.setOnMouseReleased(e -> button.setTranslateY(0));

        return button;
    }

    // musicTest corregido: usa la misma ruta de recursos que playBackgroundMusic
    private void musicTest() {
        try {
            var url = getClass().getResource("/sounds/Menu_Inicio.mp3");
            if (url == null) {
                System.err.println("musicTest: no se encontró /sounds/Menu_Inicio.mp3");
                return;
            }
            Media media = new Media(url.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.0); // volumen 0 para solo probar carga sin molestar
            mediaPlayer.play();
            // detener inmediatamente para no interferir con la música real
            mediaPlayer.stop();
            mediaPlayer.dispose();
            System.out.println("musicTest: recurso de audio accesible");
        } catch (Exception e) {
            System.err.println("Error en musicTest: " + e.getMessage());
        }
    }

    // Cargar hoja de estilos desde resources/css/style.css
    private void loadStylesheet(Scene scene) {
        try {
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

    // ========== HANDLERS ==========

    private void handleTutorial() {
        System.out.println("MODO TUTORIAL ACTIVADO");

        // Asegurarse de pasar la referencia del primaryStage actual
        com.downkombat.tutorial.Tutorial tutorial = new com.downkombat.tutorial.Tutorial();
        tutorial.mostrar(this.primaryStageRef);
    }

    private void handleStart() {
        System.out.println("¡JUEGO INICIADO!");
    }

    public static MediaPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(MediaPlayer player) {
        MenuInicio.player = player;
    }

    // Asegurar liberar recursos al cerrar la app
    @Override
    public void stop() {
        // Liberar vídeo de fondo si existe
        if (backgroundVideoPlayer != null) {
            try {
                backgroundVideoPlayer.stop();
                backgroundVideoPlayer.dispose();
            } catch (Exception e) {
                System.err.println("Error liberando video: " + e.getMessage());
            } finally {
                backgroundVideoPlayer = null;
            }
        }
        stopMusic();
        Platform.exit();
    }
}