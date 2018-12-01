package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {
    @FXML
    private void closeAppAction(ActionEvent event) {
        Platform.exit();
    }
}
