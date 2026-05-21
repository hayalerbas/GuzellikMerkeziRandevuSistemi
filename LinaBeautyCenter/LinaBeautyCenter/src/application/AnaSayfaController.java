package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.stage.Stage;

public class AnaSayfaController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button adminGirisButton;

    @FXML
    private Button kullaniciGirisButton;

    // SAYFA AÇ

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

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.show();
    }

    @FXML
    void openAdminGiris(ActionEvent event)
            throws IOException {

        sayfaAc(event, "AdminGiris.fxml");
    }

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
    void openKullaniciGiris(ActionEvent event)
            throws IOException {

        sayfaAc(event, "KullaniciGiris.fxml");
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
    void initialize() {

        assert adminGirisButton != null :
                "fx:id=\"adminGirisButton\" was not injected.";

        assert kullaniciGirisButton != null :
                "fx:id=\"kullaniciGirisButton\" was not injected.";
    }
}