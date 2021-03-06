package pl.jpranica.multipainterfx;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Main controller class for the entire layout.
 */
public class MainController extends VistaContainer {
    @FXML
    private TabPane tbpane;
    private List<PaintController> canvases = new ArrayList<>();

    @FXML
    private void onHost(){
        try {
            //Start server
            Server server = Dialogs.host();
            if(server==null){
                Dialogs.error("Wprowadzono niepoprawne dane!");
                return;
            }

            server.start();

            //Connect
            ServerConnection sc = new ServerConnection( new Socket("localhost", server.getServerPort()) );
            TabController tabCtrl = new TabController("Lokalne płótno");
            tbpane.getTabs().add(tabCtrl.getTab());
            PaintController pc = new PaintController(tabCtrl, sc);
            canvases.add(pc);
            new Thread(new RemoteCanvasThread(sc, pc)).start();

        } catch (IOException e) {
            Dialogs.error("Nie udało się postawić płótna.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onConnect(){
        try {
            ServerConnection sc = Dialogs.connect();
            if(sc==null){
                Dialogs.error("Wprowadzono niepoprawne dane!");
                return;
            }
            TabController tabCtrl = new TabController("Zdalne płótno");
            PaintController pc = new PaintController(tabCtrl, sc);
            tbpane.getTabs().add(tabCtrl.getTab());
            new Thread(new RemoteCanvasThread(sc, pc)).start();
        } catch (Exception e) {
            Dialogs.error("Nie udało się połaczyć.");
            e.printStackTrace();
        }
    }

    /** Holder of a switchable vista. */
    @FXML
    private StackPane vistaHolder;

    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }

    @FXML
    private void closeAppAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void onSave() {
        try {
            int i = 1;
            for(PaintController canvas : canvases) {
                Image snapshot = canvas.getCanvas().snapshot(null, null);

                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint" + i + ".png"));
                i++;
            }
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }
}