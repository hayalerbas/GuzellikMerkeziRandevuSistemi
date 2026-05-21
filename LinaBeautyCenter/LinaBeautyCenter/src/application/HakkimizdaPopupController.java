package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.stage.Stage;

public class HakkimizdaPopupController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button kapatButton;

    @FXML
    void popupKapat(ActionEvent event) {

        Stage stage =
                (Stage) kapatButton.getScene().getWindow();

        stage.close();
    }

    @FXML
    void initialize() {

        assert kapatButton != null :
                "fx:id=\"kapatButton\" was not injected.";
    }
}