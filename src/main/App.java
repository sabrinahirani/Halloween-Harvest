package main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The main class for the app. Controls
 * the main application stage, and handles
 * navigation logic.
 */
public class App extends Application {

    /**
     * Main application window
     */
    private static Stage window;

    /**
     * The scene being displayed on the main stage
     */
    private static Scene scene;

    /**
     * Current location of the window, that is, the main scene
     * being displayed.
     */
    private static String location;

    /**
     * Main method.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads main stage and starts application.
     *
     * @param stage the main Stage
     */
    @Override
    public void start(Stage stage) throws FileNotFoundException, IOException {
        try {
            // First, set a scene to load initially
            Pane root = new Pane();
            root.setMinWidth(1000);
            root.setMinHeight(600);
            scene = new Scene(root);

            // Load that scene and show the window
            window = stage;
            window.setTitle("Halloween Harvest");
            // window.getIcons().add(WINDOW_ICON);
            window.setMinWidth(1000);
            window.setMinHeight(600 + 39); // Accommodate for title bar because JavaFX doesn't
            window.setScene(scene);
            window.show();

            // Then navigate to the Main screen, and start the application.
            Driver.init();
            App.navigate("Main");
        } catch (Exception e) {
            Log.info("Failed to load main window: " + e.getMessage());
            e.printStackTrace();
            window.close();
            System.exit(1);
        }
    }

    /**
     * Updates the scene on the application stage.
     *
     * @param content the scene to display.
     */
    private static void updateScene(Parent content) {
        scene.setRoot(content);
    }

    /**
     * @return current location of the main application stage
     */
    public static String getLocation() {
        return location;
    }

    /**
     * Sets the internal stage location variable.
     * This is controlled by the App class. To change
     * the application's stage location, use App.navigate()
     *
     * @param newLocation
     */
    private static void setLocation(String newLocation) {
        location = newLocation;
    }

    /**
     * Changes the scene displayed on the main application
     * stage based on the id given. The id can
     * be an entity ID, in which case the ID's corresponding
     * view will be loaded. The id may also be the string
     * "Home", in which case the Home scene will be loaded.
     *
     * @param newLocation specifies where to navigate
     * @throws IllegalArgumentException - if an invalid id is provided
     * @throws IOException - if loading FXML fails
     */
    public static void navigate(String newLocation) throws IllegalArgumentException, IOException {
        // If navigating to the same location, no need to update the scene.
        if (location != null && location.equals(newLocation)) return;
        // Otherwise, update App.location
        setLocation(newLocation);
        if (newLocation.equals("Main")) {
            Parent home = SceneUtil.loadFXML("Main", true);
            updateScene(home);
        } else {
            switch (newLocation) {
                default:
                    throw new IllegalArgumentException(
                        "Invalid location provided: " + newLocation);
            }
        }
    }

    public static Window getWindow() {
        return (Window)window;
    }
}
