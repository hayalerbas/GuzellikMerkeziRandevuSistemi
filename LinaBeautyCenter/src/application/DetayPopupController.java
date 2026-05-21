package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.stage.Stage;

public class DetayPopupController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label aciklamaLabel;

    @FXML
    private Label baslikLabel;

    @FXML
    private Button kapatButton;

    public void detaylariAyarla(String baslik, String aciklama) {

        baslikLabel.setText(baslik);

        aciklamaLabel.setText(aciklama);
    }

    @FXML
    void popupKapat(ActionEvent event) {

        Stage stage =
                (Stage) kapatButton.getScene().getWindow();

        stage.close();
    }

    @FXML
    void initialize() {

        assert aciklamaLabel != null :
                "fx:id=\"aciklamaLabel\" was not injected.";

        assert baslikLabel != null :
                "fx:id=\"baslikLabel\" was not injected.";

        assert kapatButton != null :
                "fx:id=\"kapatButton\" was not injected.";
    }
}