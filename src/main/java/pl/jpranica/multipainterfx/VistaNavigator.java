package pl.jpranica.multipainterfx;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Utility class for controlling navigation between vistas.
 *
 * All methods on the navigator are static to facilitate
 * simple access from anywhere in the application.
 */
public class VistaNavigator {

    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
    public static final String VISTA_PAINT = "/pl/jpranica/multipainterfx/fxml/paint.fxml";
    public static final String VISTA_MAIN = "/pl/jpranica/multipainterfx/fxml/main.fxml";
    public static final String VISTA_HOLDER = "/pl/jpranica/multipainterfx/fxml/vistaHolder.fxml";

    /**
     * Loads the vista specified by the fxml file into the
     * vistaHolder pane of the main application layout.
     *
     * Previously loaded vista for the same fxml file are not cached.
     * The fxml is loaded anew and a new vista node hierarchy generated
     * every time this method is invoked.
     *
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example:
     *   cache FXMLLoaders
     *   cache loaded vista nodes, so they can be recalled or reused
     *   allow a user to specify vista node reuse or new creation
     *   allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadVista(String fxml, VistaContainer vc) {
        try {
            FXMLLoader loader = new FXMLLoader(VistaNavigator.class.getResource(fxml));
            vc.setVista( loader.load() );

            VistaContainable child = loader.getController();
            child.init(vc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}