package pl.jpranica.multipainterfx;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * Main controller class for the entire layout.
 */
public class MainController extends VistaContainer {
    @FXML
    private ListView<String> lvLocalCanvas;
    @FXML
    private ListView<String> lvRemoteCanvas;
    @FXML
    private TitledPane tpLocalCanvas;
    @FXML
    private TitledPane tpRemoteCanvas;
    @FXML
    private TabPane tbpane;

    public void init(){
        tpLocalCanvas.expandedProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(tpLocalCanvas.isExpanded()){
                    ObservableList<String> ols = FXCollections.observableArrayList();
                    ols.add("** Dodaj **");
                    ols.add("Płótno");
                    lvLocalCanvas.setItems(ols);
                }else {
                    lvLocalCanvas.getSelectionModel().clearSelection();
                }
            }
        });

        lvLocalCanvas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String s = lvLocalCanvas.getSelectionModel().getSelectedItem();
                if(s==null)
                    return;

                TabController tabCtrl = new TabController(s);
                tbpane.getTabs().add(tabCtrl.getTab());

                if(s=="** Dodaj **"){
                    try {
                        //Start server
                        Server server = Dialogs.host();
                        server.start();

                        //Connect
                        ServerConnection sc = new ServerConnection( new Socket("localhost", server.getServerPort()) );
                        PaintController pc = new PaintController(tabCtrl, sc);
                        new RemoteCanvasThread(sc, pc).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        tpRemoteCanvas.expandedProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(tpRemoteCanvas.isExpanded()){
                    ObservableList<String> ols = FXCollections.observableArrayList();
                    ols.add("** Dodaj **");
                    ols.add("Płótno");
                    lvRemoteCanvas.setItems(ols);
                }else {
                    lvRemoteCanvas.getSelectionModel().clearSelection();
                }
            }
        });

        lvRemoteCanvas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String s = lvRemoteCanvas.getSelectionModel().getSelectedItem();
                if(s==null)
                    return;

                TabController tabCtrl = new TabController(s);
                tbpane.getTabs().add(tabCtrl.getTab());

                if(s=="** Dodaj **"){
                    ServerConnection sc = Dialogs.connect();
                    PaintController pc = new PaintController(tabCtrl, sc);
                    new RemoteCanvasThread(sc, pc).start();
                }

            }
        });
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
        /*try {
            Image snapshot = canvas.snapshot(null, null);

            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }*/
    }
}