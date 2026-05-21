package application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.stage.Stage;

public class SSSController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label cevapCiltBakimi;

    @FXML
    private Label cevapHazirlik;

    @FXML
    private Label cevapLazer;

    @FXML
    private Label cevapOdeme;

    @FXML
    private Label cevapRandevu;

    @FXML
    private Button instagramButton;

    // SAYFA GEÇİŞ

    private void sayfaAc(ActionEvent event,
                         String fxmlAdi) throws IOException {

        Parent root =
                FXMLLoader.load(
                        getClass().getResource(
                                "/application/" + fxmlAdi));

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));

        stage.show();
    }

    // SORULAR

    @FXML
    void soruRandevu(ActionEvent event) {

        cevapRandevu.setVisible(
                !cevapRandevu.isVisible());
    }

    @FXML
    void soruHazirlik(ActionEvent event) {

        cevapHazirlik.setVisible(
                !cevapHazirlik.isVisible());
    }

    @FXML
    void soruLazer(ActionEvent event) {

        cevapLazer.setVisible(
                !cevapLazer.isVisible());
    }

    @FXML
    void soruCiltBakimi(ActionEvent event) {

        cevapCiltBakimi.setVisible(
                !cevapCiltBakimi.isVisible());
    }

    @FXML
    void soruOdeme(ActionEvent event) {

        cevapOdeme.setVisible(
                !cevapOdeme.isVisible());
    }

    // INSTAGRAM

    @FXML
    void openInstagram(ActionEvent event) {

        try {

            Desktop.getDesktop().browse(
                    new URI(
                            "https://instagram.com/lina_beautycenter_"));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // SAYFALAR

    @FXML
    void openAnaSayfa(ActionEvent event)
            throws IOException {

        sayfaAc(event, "AnaSayfa.fxml");
    }

    @FXML
    void openCiltTesti(ActionEvent event)
            throws IOException {

        sayfaAc(event, "CiltTesti.fxml");
    }

    @FXML
    void openFiyat(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Fiyat.fxml");
    }

    @FXML
    void openHakkimizda(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Hakkimizda.fxml");
    }

    @FXML
    void openIletisim(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Iletisim.fxml");
    }

    @FXML
    void openIslemler(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Islemler.fxml");
    }

    @FXML
    void openKampanyalar(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Kampanyalar.fxml");
    }

    @FXML
    void openRandevu(ActionEvent event)
            throws IOException {

        sayfaAc(event, "Randevu.fxml");
    }

    @FXML
    void openSSS(ActionEvent event)
            throws IOException {

        sayfaAc(event, "SSS.fxml");
    }

    @FXML
    void openAdminGiris(ActionEvent event)
            throws IOException {

        sayfaAc(event, "AdminGiris.fxml");
    }

    @FXML
    void initialize() {

        cevapRandevu.setVisible(false);

        cevapHazirlik.setVisible(false);

        cevapLazer.setVisible(false);

        cevapCiltBakimi.setVisible(false);

        cevapOdeme.setVisible(false);
    }
}