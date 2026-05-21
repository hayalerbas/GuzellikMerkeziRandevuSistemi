package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.stage.Stage;

public class BasariliPopupController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label aciklamaLabel;

    @FXML
    private Label baslikLabel;

    @FXML
    private Label durumIconLabel;

    @FXML
    private Button tamamButton;

    public void popupBilgi(String baslik, String aciklama) {

        baslikLabel.setText(baslik);
        aciklamaLabel.setText(aciklama);

        durumIconLabel.setText("✓");
        durumIconLabel.setStyle(
                "-fx-text-fill: #c28a1a;"
                        + "-fx-font-size: 35;"
                        + "-fx-font-weight: bold;"
        );
    }

    public void popupBilgi(String baslik, String aciklama, boolean basariliMi) {

        baslikLabel.setText(baslik);
        aciklamaLabel.setText(aciklama);

        if (basariliMi) {

            durumIconLabel.setText("✓");
            durumIconLabel.setStyle(
                    "-fx-text-fill: #c28a1a;"
                            + "-fx-font-size: 35;"
                            + "-fx-font-weight: bold;"
            );

        } else {

            durumIconLabel.setText("✖");
            durumIconLabel.setStyle(
                    "-fx-text-fill: #ff3b3b;"
                            + "-fx-font-size: 35;"
                            + "-fx-font-weight: bold;"
            );
        }
    }

    @FXML
    void popupKapat(ActionEvent event) {

        Stage stage =
                (Stage) tamamButton.getScene().getWindow();

        stage.close();
    }

    @FXML
    void initialize() {

        assert aciklamaLabel != null;
        assert baslikLabel != null;
        assert durumIconLabel != null;
        assert tamamButton != null;
    }
}