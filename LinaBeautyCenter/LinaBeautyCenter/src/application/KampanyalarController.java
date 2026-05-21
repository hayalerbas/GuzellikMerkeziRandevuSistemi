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

import javafx.stage.Stage;

public class KampanyalarController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

    private void popupAc(String baslik,
                         String aciklama) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/application/DetayPopup.fxml"));

            Parent root = loader.load();

            DetayPopupController controller =
                    loader.getController();

            controller.detaylariAyarla(
                    baslik,
                    aciklama);

            Stage stage = new Stage();

            stage.setTitle(baslik);

            stage.setScene(new Scene(root));

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    void detayCiltBakimi(ActionEvent event) {

        popupAc(
                "Cilt Bakımı",
                "• %20 indirim fırsatı\n"
                        + "• Derinlemesine bakım\n"
                        + "• Nem terapisi dahil\n\n"
                        + "Eski Fiyat: ₺4000\n"
                        + "Kampanyalı Fiyat: ₺3200"
        );
    }

    @FXML
    void detayLazerEpilasyon(ActionEvent event) {

        popupAc(
                "Lazer Epilasyon",
                "• %30 indirim fırsatı\n"
                        + "• Profesyonel lazer uygulaması\n\n"
                        + "Eski Fiyat: ₺15000\n"
                        + "Kampanyalı Fiyat: ₺10500"
        );
    }

    @FXML
    void detayKasKirpik(ActionEvent event) {

        popupAc(
                "Kaş & Kirpik",
                "• %25 indirim fırsatı\n"
                        + "• Kaş tasarımı dahil\n\n"
                        + "Eski Fiyat: ₺5000\n"
                        + "Kampanyalı Fiyat: ₺3750"
        );
    }

    @FXML
    void detayBolgeselIncelme(ActionEvent event) {

        popupAc(
                "Bölgesel İncelme",
                "• %15 indirim fırsatı\n"
                        + "• Vücut şekillendirme\n\n"
                        + "Eski Fiyat: ₺16000\n"
                        + "Kampanyalı Fiyat: ₺13600"
        );
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

    }
}