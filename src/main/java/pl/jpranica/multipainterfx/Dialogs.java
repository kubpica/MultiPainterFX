package pl.jpranica.multipainterfx;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class Dialogs {

    public static void error(String msg){
        error(msg, "Błąd");
    }

    public static void error(String msg, String title){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static Server host() {
        Dialog<Server> dialog = new Dialog<>();
        dialog.setTitle("Połącz");
        dialog.setHeaderText("This is a custom dialog. Enter info and \n" +
                "press Okay (or click title bar 'X' for cancel).");
        dialog.setResizable(true);

        Label label1 = new Label("Nazwa: ");
        Label label2 = new Label("Port: ");
        TextField text1 = new TextField();
        TextField text2 = new TextField();

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Połącz", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, Server>() {
            @Override
            public Server call(ButtonType b) {

                if (b == buttonTypeOk) {
                    return new Server(Integer.parseInt( text2.getText() ) );
                }

                return null;
            }
        });

        Optional<Server> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }

    public static ServerConnection connect() {
        Dialog<ServerConnection> dialog = new Dialog<>();
        dialog.setTitle("Połącz");
        dialog.setHeaderText("This is a custom dialog. Enter info and \n" +
                "press Okay (or click title bar 'X' for cancel).");
        dialog.setResizable(true);

        Label label1 = new Label("Adres: ");
        Label label2 = new Label("Port: ");
        TextField text1 = new TextField();
        TextField text2 = new TextField();

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Połącz", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, ServerConnection>() {
            @Override
            public ServerConnection call(ButtonType b) {

                if (b == buttonTypeOk) {
                    try {
                        Socket socket = new Socket(text1.getText(), Integer.parseInt( text2.getText() ) );
                        return new ServerConnection(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        });

        Optional<ServerConnection> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }
}
