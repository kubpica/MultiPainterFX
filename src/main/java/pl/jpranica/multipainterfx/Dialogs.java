package pl.jpranica.multipainterfx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        dialog.setTitle("Postaw płótno");
        dialog.setHeaderText("Podaj port, na którym postawić płótno.");
        dialog.setResizable(true);

        Label label1 = new Label("Szerokość: ");
        Label label2 = new Label("Wysokość: ");
        Label label3 = new Label("Port: ");
        TextField text1 = new TextField();
        text1.setText("600");
        TextField text2 = new TextField();
        text2.setText("600");
        TextField text3 = new TextField();
        text3.setText("4545");
        text3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    text3.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        GridPane grid = new GridPane();
        /*grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);*/
        grid.add(label3, 1, 3);
        grid.add(text3, 2, 3);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Postaw", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.setResultConverter(new Callback<ButtonType, Server>() {
            @Override
            public Server call(ButtonType b) {

                if (b == buttonTypeOk && !text3.getText().isEmpty()) {
                    try{
                        return new Server(Integer.parseInt( text3.getText() ) );
                    }catch (NumberFormatException e){
                        Dialogs.error("Wprowadzono niedozwolone wartości!");
                        return null;
                    }
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
        dialog.setHeaderText("Podaj dane do połączenie ze zdalnym płótnem.");
        dialog.setResizable(true);

        Label label1 = new Label("Adres: ");
        Label label2 = new Label("Port: ");
        TextField text1 = new TextField();
        text1.setText("localhost");
        TextField text2 = new TextField();
        text2.setText("4545");
        text2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    text2.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

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

                if (b == buttonTypeOk && !text1.getText().isEmpty() && !text2.getText().isEmpty()) {
                    try {
                        Socket socket = new Socket(text1.getText(), Integer.parseInt( text2.getText() ) );
                        return new ServerConnection(socket);
                    } catch (IOException | NumberFormatException e) {
                        //e.printStackTrace();
                        return null;
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
